package banq.smartline;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import utilitaire.Utilitaire;

/**
 * Created by macbookpro on 28/03/2017.
 */

public class RetrieveMessagesJson extends AsyncTask<String, Void, String> {
    Activity activity;
    ListView listView;
    String myToken;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    public RetrieveMessagesJson(Activity act,ListView lv,String token){
        activity = act;
        listView = lv;
        myToken = token;
    }

    public RetrieveMessagesJson(Activity act,ListView lv){
        activity = act;
        listView = lv;
    }

    @Override
    protected void onPreExecute() {
        System.out.println("Tokony misy SPINNER");
        progressDialog = new ProgressDialog(activity).show(activity,"","Veuillez patienter",false);
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
                json += line + System.getProperty("line.separator");
            }

        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }

        Log.i("ANDRANA 3", json);
        return json;
    }

    @Override
    protected void onProgressUpdate(Void... progress) {
//        Log.i("ANDRANA 4","progress");
        progressDialog = new ProgressDialog(activity).show(activity,"","Veuillez patienter",false);
    }

    @Override
    public void onPostExecute(String test){
        Log.i("ANDRANA", test);
//        System.out.println("---------------   ON POST EXECUTE   --------------- ");

        try {
            String service = null;
            System.out.println("reponse = "+test);
            JSONArray fil_attente_array = new JSONArray(test);
            List<VueFilAttente> listTemp = new ArrayList<VueFilAttente>();
            String[] items = new String[fil_attente_array.length()];
            for (int i = 0; i < fil_attente_array.length(); i++) {
                JSONObject obj = new JSONObject(fil_attente_array.getString(i));
                VueFilAttente temp = new VueFilAttente();
                temp.setId_service(obj.getString(Service.id_service));
                temp.setTemps_entre(Utilitaire.stringDate(obj.getString(Service.temps_entre)));
                temp.setId_client(Integer.valueOf(obj.getString(Service.id_client)));
                temp.setNom(obj.getString(Service.nom));
                temp.setToken(obj.getString(Service.token));
                temp.setSexe(obj.getString(Service.sexe));
                listTemp.add(temp);

            }
            VueFilAttente[]list = new VueFilAttente[listTemp.size()];
            listTemp.toArray(list);

            if(list!=null) {
                service = list[0].getId_service();
                System.out.println("Fill = " + list.length);
                String [] tabNom = new String[list.length];
                Integer[] tabImage = new Integer[list.length];
                String [] temps = new String[list.length];
                String [] token = new String[list.length];

                for (int i = 0; i < list.length; i++) {
                    tabNom[i] = list[i].getNom();
                    if (list[i].getSexe().equals("1")) {
                        tabImage[i] = R.drawable.male;
                    } else {
                        tabImage[i] = R.drawable.female;
                    }
                    if(myToken!=null && myToken.compareToIgnoreCase(list[i].getToken())==0){
                        token[i] = list[i].getToken();
                    }else{
                        token[i]=null;
                    }
                    temps[i] = Utilitaire.datetoHeure(list[i].getTemps_entre());
                }
                AndroidListAdapter androidListAdapter = new AndroidListAdapter(activity, tabImage, tabNom,temps,token);
                //ListView androidListView = (ListView) activity.findViewById(R.id.liste_fil_attente);
                listView.setAdapter(androidListAdapter);


                /*pref = activity.getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

                if(pref.getString("attente",null)==null){
                    editor = pref.edit();
                    editor.putString("attente",service);
                    editor.commit();
                }*/

            }

        }catch(Exception e){
            e.printStackTrace();
        }
        if(Retrait.refresh.isRefreshing()){
            Retrait.refresh.setRefreshing(false);
        }
        progressDialog.dismiss();
    }
}