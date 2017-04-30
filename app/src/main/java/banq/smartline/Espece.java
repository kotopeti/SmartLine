package banq.smartline;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class Espece extends AppCompatActivity {
    AndroidListAdapter androidListAdapter;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Bundle b;
    Activity mine;
    ListView androidListView;
    String androidListViewStrings[] = {"Android ListView Example", "Android Custom ListView Example", "Custom ListView Example",
            "Android List Adapter", "Custom Adapter ListView", "ListView Tutorial",
            "ListView with Image and Text", "Custom ListView Text and Image", "ListView Custom Tutorial"};

    Integer image_id[] = {R.drawable.male, R.drawable.female, R.drawable.female,
            R.drawable.male, R.drawable.male, R.drawable.female,
            R.drawable.male, R.drawable.female, R.drawable.male};
    String[] tabNom ;
    Integer[] tabImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espece);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Versement éspèce");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        b = getIntent().getExtras();
        mine = this;
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        androidListView = (ListView)findViewById(R.id.liste_fil_attente);

        try{
            new Service().getListeFilAttent(this,UrlConf.idEspece,androidListView,pref.getString("token",null));


        }catch(Exception e){
            e.printStackTrace();
        }

        Retrait.refresh = (SwipeRefreshLayout)findViewById(R.id.swiperefresh_espece);
        Retrait.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try{
                    new Service().getListeFilAttent(mine,UrlConf.idEspece,androidListView,pref.getString("token",null));
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
                        Intent next = new Intent(getApplicationContext(),MenuPrincipale.class);
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
                        new Service().putOnLine(mine,pref.getString("token",null),UrlConf.idEspece, androidListView);
                        editor = pref.edit();
                        editor.putString("faa", String.valueOf(UrlConf.idEspece));
                        editor.commit();

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
