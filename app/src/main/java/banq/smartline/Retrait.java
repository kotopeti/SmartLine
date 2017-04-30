package banq.smartline;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class Retrait extends AppCompatActivity {
    Button valider;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2;
    Activity mine;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ProgressDialog pd;
    static SwipeRefreshLayout refresh;
    ListView androidListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrait);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Retrait d'argent");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        mine = this;
        androidListView  = (ListView)findViewById(R.id.liste_fil_attente_retrait);
        try{

            new Service().getListeFilAttent(this,UrlConf.idRetrait,androidListView,pref.getString("token",null));

        }catch(Exception e){
            e.printStackTrace();
        }

        refresh = (SwipeRefreshLayout)findViewById(R.id.swiperefresh_retrait);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try{
                    new Service().getListeFilAttent(mine,UrlConf.idRetrait,androidListView,pref.getString("token",null));
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        });
        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_apropos);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_file_attente);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked
                if(pref.getString("faa",null)==null){
                    Alert.showLocationDialog(mine,"Attention","Vous n'avez pas de file actuellement");
                    return;
                }else{
                    try {
                        new Service().getOutLine(pref.getString("token",null),Integer.valueOf(pref.getString("faa",null)),editor,mine);
                        editor = pref.edit();
                        editor.remove("faa");
                        editor.commit();
                        Intent next = new Intent(getApplicationContext(),Rate.class);
                        startActivity(next);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }


            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(pref.getString("faa",null)!=null){
                    Alert.showLocationDialog(mine,"Attention","Vous ne pouvez pas accéder dans 2 files d'attente en même temps");
                    return;
                }else{
                    try {
                        ListView androidListView = (ListView) findViewById(R.id.liste_fil_attente_retrait);
                        new Service().putOnLine(mine,pref.getString("token",null),UrlConf.idRetrait, androidListView);
                        editor = pref.edit();
                        editor.putString("faa", String.valueOf(UrlConf.idRetrait));
                        editor.commit();
                        /*Intent next = new Intent(getApplicationContext(),Retrait.class);
                        startActivity(next);*/
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                /*Intent i = new Intent(getApplicationContext(),FilAttente.class);
                startActivity(i);*/

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        overridePendingTransition(R.anim.back_in,R.anim.back_out);
        return true;
    }

}
