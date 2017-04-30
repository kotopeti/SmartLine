package banq.smartline;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class VotreTour extends AppCompatActivity {
    TextView countdown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_votre_tour);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("C'est votre tour!");
        countdown = (TextView)findViewById(R.id.textView_countdown);
        TextView dispo = (TextView)findViewById(R.id.textView_dispo2);
        dispo.setText("pour passer au guichet"+(UrlConf.idRetrait));
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                countdown.setText("" + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                countdown.setText("Votre tour est passé");
            }

        }.start();

    }

}
