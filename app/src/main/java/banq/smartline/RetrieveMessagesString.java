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
import java.util.Date;
import java.util.List;

import utilitaire.Utilitaire;

/**
 * Created by macbookpro on 28/03/2017.
 */

public class RetrieveMessagesString extends AsyncTask<String, Void, String> {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    Activity activity;
    static String reponse;

    public RetrieveMessagesString(Activity act,SharedPreferences.Editor edit){
        editor = edit;
        activity = act;
    }

    @Override
    protected void onPreExecute() {
//        System.out.println("Tokony misy SPINNER");
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
                System.out.println("Line = "+line);
                json += line;
            }

        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        reponse = json;
        return json;
    }

    @Override
    protected void onProgressUpdate(Void... progress) {
//        Log.i("ANDRANA 4","progress");
        progressDialog = new ProgressDialog(activity).show(activity,"","Veuillez patienter",false);
    }

    @Override
    public void onPostExecute(String test){
        Service.retourRate = test;
        reponse = test;
        String nomAct = activity.getClass().getSimpleName();
//        System.out.println("nomAct = " + nomAct);
//        System.out.println("Réponse oooo o oo o o o o oo o o = "+test);
        if(test!=null && nomAct.compareToIgnoreCase("menuprincipale")==0) {
            String[] tab = test.split(";");
            String id_client = tab[0].trim();
            String id_service = tab[1].trim();
            String temps_entre = tab[2].trim();
            String temps_sortie = tab[3].trim();
            java.util.Date entre = Utilitaire.stringDateUtil(temps_entre);
            java.util.Date sortie = Utilitaire.stringDateUtil(temps_sortie);
            long diff = sortie.getTime() - entre.getTime();
            String valeur_diff = Utilitaire.diffTempsDate(diff);
            Intent next = new Intent(activity.getApplicationContext(), Rate.class);
            if (tab.length > 1) {
//                System.out.println("000000000000 - id_client = " + id_client + " / id_service = " + id_service + " / temps_attente = " + valeur_diff);
                next.putExtra("id_client", id_client);
                next.putExtra("id_service", id_service);
                next.putExtra("temps_attente", valeur_diff);
            }
            editor.remove("faa");
            editor.commit();
            activity.startActivity(next);
        }
        progressDialog.dismiss();
//        System.out.println("ZAY VO VITA ILAY IZY");

    }
}