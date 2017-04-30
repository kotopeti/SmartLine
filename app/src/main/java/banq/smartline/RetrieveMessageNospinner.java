package banq.smartline;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by macbookpro on 29/04/2017.
 */

public class RetrieveMessageNospinner extends AsyncTask<String, Void, String> {
    ProgressDialog progressDialog;
    Activity activity;

    public RetrieveMessageNospinner(){

    }

    public RetrieveMessageNospinner(Activity act){
        activity = act;
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

        return json;
    }

    @Override
    protected void onProgressUpdate(Void... progress) {
    }

    @Override
    public void onPostExecute(String result) {



    }
}