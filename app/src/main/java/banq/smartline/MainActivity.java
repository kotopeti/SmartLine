package banq.smartline;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    NfcAdapter nfcAdapter;
    CoordinatorLayout coordinatorLayout;
    PendingIntent pendingIntent;
    String URL;
    private static boolean inscrit = false;
    private Identification id;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String refreshedToken;
    Activity mine;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Smart Line");
        mine  = this;

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent =  PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if (!nfcAdapter.isEnabled())
        {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Votre NFC n'est pas activé. Veuillez activer la fonction NFC de votre mobile", Snackbar.LENGTH_LONG);
            snackbar.show();
        }else{
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Merci d'avoir choisi notre service SmartLine", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }

        if(isOnline()==false){
            showLocationDialog();
            return;
        }
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        System.out.println("refreshedToken = " + refreshedToken);
        URL = UrlConf.getUrlServeur()+"checkClient.php?token="+refreshedToken;


    }



    public boolean isInscrit() {
        return inscrit;
    }

    public void setInscrit(boolean inscrit) {
        this.inscrit = inscrit;
    }

    public Identification getId() {
        return id;
    }

    public void setId(Identification id) {
        this.id = id;
    }

    private class RetrieveMessages extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            System.out.println("Tokony misy SPINNER");
            pd = new ProgressDialog(mine).show(mine,"","Veuillez patienter",false);
        }

        @Override
        protected String doInBackground(String... urls) {
            HttpClient client = new DefaultHttpClient();
            String json = "";
            try {
                String line = "";
                HttpGet request = new HttpGet(urls[0]);
                HttpResponse response = client.execute(request);
                response = client.execute(request);
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                while ((line = rd.readLine()) != null) {
                    json+=line;
                }

            } catch (IllegalArgumentException e1) {
                e1.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onProgressUpdate(Void... progress) {
            System.out.println("Tokony misy SPINNER");
            pd = new ProgressDialog(mine).show(mine,"","Veuillez patienter",false);
        }

        @Override
        public void onPostExecute(String test) {
            System.out.println("Vita tompoko maty spinner SPINNER");
            pd.dismiss();

            if(test!=null && test.length()>0) {
                if (test.compareToIgnoreCase("false") == 0) {
                    System.out.println("  ------------- mahita FALSE   ------------- "+test);
                    setInscrit(false);

                } else {
                    setInscrit(true);
                    System.out.println("  ------------- mahita zavatra HAFA   -------------"+test);
                    String[] tabTest = test.split(";");
                    Identification idf = new Identification();
                    idf.setNom(tabTest[0]);
                    idf.setToken(refreshedToken);
                    idf.setSexe(Integer.valueOf(tabTest[1]));
                    setId(idf);
                }

                if(isInscrit()==false){
                    Intent i = new Intent(getApplicationContext(), Register.class);
                    startActivity(i);
                    return;
                }
                Intent i = new Intent(getApplicationContext(), MenuPrincipale.class);
                i.putExtra("id",getId());
                editor.putString("token",getId().getToken());
                editor.putString("nom",getId().getNom());
                editor.putInt("sexe",getId().getSexe());
                editor.commit();
                startActivity(i);

            }else{
                Alert.showLocationDialog(mine,"Erreur","Une érreur s'est produit, veuillez rescanner le TAG. Assurer-vous que votre connection internet marche.");
            }
            System.out.println("test = " + test);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this,pendingIntent, null,null);
    }


    @Override
    public void onPause()
    {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onNewIntent(Intent intent)
    {
        if(isOnline()==false){
            Alert.showLocationDialog(mine,"Erreur","Vous n'êtes pas connécté à internet. Veuillez vous assurez que votre data marche");
            return;
        }
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action) || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action))
        {
            processNfcIntent(intent) ;
        }
    }


    public void processNfcIntent (Intent intent)
    {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        byte[] id =tag.getId();
        String[] technologies = tag.getTechList();
        int content = tag.describeContents();
        Ndef ndef = Ndef.get(tag);
        boolean isWritable = ndef.isWritable();
        boolean canMakeReadOnly = ndef.canMakeReadOnly();
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage[] msgs;
        String message = null;
        if (rawMsgs != null)
        {
            msgs = new NdefMessage[rawMsgs.length];
            System.out.println("msgs = " + msgs);
            System.out.println(" length = "+rawMsgs.length);
            for (int i = 0; i < rawMsgs.length; i++)
            {
                msgs[i] = (NdefMessage) rawMsgs[i];
                NdefRecord record = msgs[i].getRecords()[i];
                byte[] idRec = record.getId();
                short tnf = record.getTnf();
                byte[] type = record.getType();
                message = new String (record.getPayload());
                System.out.println("message = '" + message+"'");

            }
            message = message.trim();
            if(message.equals("en1")){
                System.out.println("NAHITA TAG 1");
                this.runOnUiThread(new Runnable() {
//                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try  {
                            System.out.println("Niditra ato am RUN");
                            Identification idf = new Identification();
                            System.out.println("URL volohany = "+URL);
                            new RetrieveMessages().execute(URL);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
//                thread.start();



            }else{
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Veuillez scaner le TAG à l'entrer", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), Apropos.class);
            startActivity(i);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void showLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.titre_alert));
        builder.setMessage(getString(R.string.contenue_alert));

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                    }
                });
        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }
}
