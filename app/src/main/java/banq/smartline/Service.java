package banq.smartline;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ListView;

import org.json.JSONArray;

import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by macbookpro on 29/03/2017.
 */

public class Service {
    public static String retourSaveClient = null;
    public static String listeFilAttent = null;
    public static JSONArray fil_attente_array = null;
    public static String objet = "VueFilAttente";
    public static String id_service = "id_service";
    public static String temps_entre = "temps_entre";
    public static String id_client = "id_client";
    public static String token = "token";
    public static String sexe = "sexe";
    public static String nom = "nom";
    public static VueFilAttente[] fil;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public static String retourRate;


    public static String saveClient(final Context context, final Activity activity, String nom, String token, int sexe)throws Exception{

        try{
            String test = UrlConf.getUrlInsert()+"?nom="+nom+"&token="+token+"&sexe="+sexe;
            URL url= new URL(test);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());

            final String URL = uri.toASCIIString();
            System.out.println("URL = " + URL);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try  {
                        Identification idf = new Identification();
                        new banq.smartline.RetrieveMessages(activity,context).execute(URL);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        return UrlConf.reponseRetriveMessage;
    }

    public void getListeFilAttent(final Activity activity, int id_fil, final ListView lv,final String token)throws Exception{
        try{
            final String URL = UrlConf.getUrlFillAttente()+"?service="+id_fil;
            System.out.println("URL = " + URL);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Identification idf = new Identification();
                        System.out.println("AVANT do in backgraoud");
                        new banq.smartline.RetrieveMessagesJson(activity, lv, token).execute(URL);
                        //System.out.println("APRES do in backgraoud = "+test);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });



        }catch(Exception e){
            e.printStackTrace();
        }

    }


    public void putOnLine(final Activity activity,final String token,int service,final ListView lv)throws Exception{
        try{
            final String URL = UrlConf.getUrlInsertFillAttente()+"?service="+service+"&token="+token;
            System.out.println("URL = " + URL);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try  {
                        Identification idf = new Identification();
                        System.out.println("AVANT do in backgraoud");
                        new banq.smartline.RetrieveMessagesJson(activity,lv,token).execute(URL);

                        //System.out.println("APRES do in backgraoud = "+test);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public String getOutLine(String token, int service, final SharedPreferences.Editor edit, final Activity activity)throws Exception{
        try{
            final String URL = UrlConf.getUrlQuitterFilAttente()+"?service="+service+"&token="+token;
            System.out.println("URL = " + URL);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try  {
                        Identification idf = new Identification();
                        System.out.println("AVANT do in backgraoud getOUt");
                        new banq.smartline.RetrieveMessagesString(activity,edit).execute(URL);
                        System.out.println("APRES do in backgraoud getOUt "+retourRate);
//                        retourRate = RetrieveMessagesString.reponse;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }catch(Exception e){
            e.printStackTrace();
        }
        return retourRate;
    }
}
