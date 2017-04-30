package banq.smartline;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.net.URLEncoder;

public class Rate extends AppCompatActivity {
    Activity mine;
    RatingBar rate;
    String id_service;
    String id_client;
    String temps_attente;
    String URL;
    String UrlNotif;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mine = this;
        Bundle b = getIntent().getExtras();
        getSupportActionBar().setTitle("Notez notre service");
        rate = (RatingBar)findViewById(R.id.ratingBar);
        rate.setNumStars(5);
        Button brate = (Button)findViewById(R.id.button_rate);
        TextView temps = (TextView)findViewById(R.id.textView_merci_temps_attente);
        temps_attente = b.getString("temps_attente");
        System.out.println("temps ato am rate = "+temps_attente);
        if(temps_attente!=null){
            temps.setText(temps_attente);
        }
        id_client = b.getString("id_client");
        id_service = b.getString("id_service");
        UrlNotif = UrlConf.getUrlSendNotif()+"?service="+id_service;
        brate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    mine.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            try  {
                                URL = UrlConf.getUrlNoterFile()+"?service="+URLEncoder.encode(id_service,"UTF-8")+"&id_client="+URLEncoder.encode(id_client,"UTF-8")+"&rate="+rate.getRating()+"&comm="+URLEncoder.encode(temps_attente,"UTF-8");

                                Identification idf = new Identification();
                                new banq.smartline.RetrieveMessageNospinner().execute(URL);
//                                String vurlNotif = URLEncoder.encode(UrlNotif,"UTF-8");
                                System.out.println("Url envoi de notifiction === "+UrlNotif);
                                new banq.smartline.RetrieveMessageNospinner().execute(UrlNotif);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    Intent i = new Intent(getApplicationContext(),MenuPrincipale.class);
                    startActivity(i);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });


    }

}
