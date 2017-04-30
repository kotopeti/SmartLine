package banq.smartline;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by macbookpro on 28/03/2017.
 */

public class RetrieveMessages extends AsyncTask<String, Void, String> {
    ProgressDialog progressDialog;
    Activity activity;
    Context context;
    public RetrieveMessages(){

    }

    public RetrieveMessages(Activity act){
        activity = act;
    }

    public RetrieveMessages(Activity act,Context cont){
        activity = act;
        context = cont;
    }


    @Override
    protected void onPreExecute() {
        System.out.println("Tokony misy SPINNER");
        progressDialog = new ProgressDialog(Register.context).show(activity,"","Veuillez patienter",false);
    }

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
        UrlConf.reponseRetriveMessage = json;
        return json;
    }

    @Override
    protected void onProgressUpdate(Void... progress) {
        progressDialog.show(activity,"","Veuillez patienter",false);
    }

    @Override
    public void onPostExecute(String result) {

        if(progressDialog.isShowing()){

            progressDialog.cancel();
            progressDialog.dismiss();
        }
        System.out.println("ATO POST EXCECUTE GET OUT  = "+result);
        UrlConf.reponseRetriveMessage = result;
        Register.reponse = result;
        String retour = result;
        if(retour!=null){
            System.out.println(" valiny farany ======= "+retour);
            Intent i = new Intent(activity.getApplicationContext(),MenuPrincipale.class);
            String[] tab = retour.split(";");
            Identification id = new Identification();
            id.setNom(tab[0]);
            id.setSexe(Integer.valueOf(tab[2]));
            id.setToken( FirebaseInstanceId.getInstance().getToken());
            i.putExtra("id",id);
            Register.editor.putString("token",id.getToken());
            Register.editor.putString("nom",id.getNom());
            Register.editor.putInt("sexe",id.getSexe());
            Register.editor.commit();
            activity.startActivity(i);
        }



    }
}