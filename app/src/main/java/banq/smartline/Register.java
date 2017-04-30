package banq.smartline;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.firebase.iid.FirebaseInstanceId;

public class Register extends AppCompatActivity {
    CoordinatorLayout coordinatorLayout;
    AppCompatActivity mine;
    EditText edittext_nom;
    RadioGroup radio_register_sex;
    static ProgressDialog progressDialog;
    String nom;
    int sexe;
    SharedPreferences pref;
    static SharedPreferences.Editor editor;
    static Context context;
    static String reponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mine = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Enregistrement");
        context = getApplicationContext();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        Button save = (Button)findViewById(R.id.button_valider_enregistrement);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()==false){
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Votre mobile n'est pas connécté. Veuillez vous assurer que vous disposez d'internet.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    showLocationDialog();
                    return;
                }else{
                    edittext_nom = (EditText)findViewById(R.id.edittext_nom_client);
                    nom = edittext_nom.getText().toString();
                    radio_register_sex = (RadioGroup) findViewById(R.id.radio_register_sex);
                    int selectedId = radio_register_sex.getCheckedRadioButtonId();
                    if (selectedId == R.id.radioMale)
                        sexe = 1;
                    else
                        sexe = 0;
                    if (nom.isEmpty()) {
                        Alert.showLocationDialog(mine,"Erreur","Erreur nom obligatoire");
                        edittext_nom.requestFocus();
                        return;
                    }
                    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                    if(refreshedToken==null){
                        showLocationDialog();
                        return;
                    }
                    try {
                        Service.saveClient(getApplicationContext(),mine,nom,refreshedToken,sexe);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

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

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
