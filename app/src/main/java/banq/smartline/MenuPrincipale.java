package banq.smartline;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import utilitaire.Utilitaire;

public class MenuPrincipale extends AppCompatActivity {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public static String URL;
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    Activity mine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principale);
        mine = this;
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        Intent i = getIntent();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        Identification id = (Identification)i.getSerializableExtra("id");
        TextView welcome = (TextView)findViewById(R.id.textViewWelcome);
        welcome.setText("Bienvenue "+pref.getString("nom",null));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Menu principale");
        Button retrait = (Button) findViewById(R.id.buttonRetrait);
        Button espece = (Button) findViewById(R.id.buttonEspece);
        Button cheque = (Button) findViewById(R.id.buttonCheque);
        Button info = (Button) findViewById(R.id.buttonInfo);

        retrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Retrait.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        espece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Espece.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        cheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Cheque.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(), Info.class);
//                startActivity(i);
                /*Intent i = new Intent(getApplicationContext(),VotreTour.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);*/
                Alert.showLocationDialog(mine,"Excusez-nous","Ce service n'est pas encore dispo pour le moment");
            }
        });




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

        if (id == R.id.action_fil_attente) {
            if(pref.getString("faa",null)!=null) {
                Intent i = null;
                String service = pref.getString("faa",null);
                if(Integer.valueOf(service)==UrlConf.idRetrait){
                    i = new Intent(getApplicationContext(), Retrait.class);
                }else if(Integer.valueOf(service)==UrlConf.idAutreService){
                    i = new Intent(getApplicationContext(), Info.class);
                }else if(Integer.valueOf(service)==UrlConf.idCheque){
                    i = new Intent(getApplicationContext(), Cheque.class);
                }else if(Integer.valueOf(service)==UrlConf.idEspece){
                    i = new Intent(getApplicationContext(), Espece.class);
                }
                if(i!=null){
                    startActivity(i);
                }
            }else{
                Alert.showLocationDialog(this,"Attention","Vous n'avez pas de file d'attente actuellement");
            }


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null,null);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }

    public void onNewIntent(Intent intent)
    {
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
            if(message.equals("en2")){
                if(pref.getString("faa",null)==null){
                    Alert.showLocationDialog(this,"Attention","Vous n'appartenez à aucun file d'attente, veuillez choisir d'abord une file");
                }else{
                    try {

                        new Service().getOutLine(pref.getString("token",null),Integer.valueOf(pref.getString("faa",null)),editor,mine);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }

        }
    }

}
