package banq.smartline;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by macbookpro on 29/03/2017.
 */

public class Alert {
    public static void showLocationDialog(Activity act, String titre, String contenue) {
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle(titre);
        builder.setMessage(contenue);

        String positiveText = "ok";
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
}
