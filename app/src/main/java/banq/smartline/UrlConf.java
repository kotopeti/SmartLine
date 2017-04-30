package banq.smartline;

/**
 * Created by macbookpro on 28/03/2017.
 */

public class UrlConf {
    private String urlServeur;
//    public static final String ipserveur = "http://192.168.43.224:8888/";
    public static final String ipserveur = "https://nfc-smartline.000webhostapp.com/";


    public static final String lien = "";
    public static final String urlInsert = "insertClient.php";
    public static final String urlFillAttente = "getFilAttente.php";
    public static final String urlInsertFillAttente = "insertFilAttente.php";
    public static final String urlGetOutLine = "sortifFilAttente.php";
    public static final String urlNoterFile = "noterFile.php";

    public static final int idRetrait = 1;
    public static final int idEspece = 2;
    public static final int idCheque = 3;
    public static final int idAutreService = 4;
    public static final String fil = "filattente";
    public static final String urlSendNotif = "urlSendNotif.php";

    public static String getUrlSendNotif(){
        return ipserveur+lien+urlSendNotif;
    }

    public static String getUrlServeur() {
        return ipserveur+lien;
    }

    public static String getUrlInsert() {
        return ipserveur+urlInsert;
    }

    public static String getUrlFillAttente(){
        return ipserveur+lien+urlFillAttente;
    }

    public static String getUrlInsertFillAttente(){
        return ipserveur+lien+urlInsertFillAttente;
    }
    public static String getUrlQuitterFilAttente(){
        return ipserveur+lien+urlGetOutLine;
    }

    public static String getUrlNoterFile(){
        return ipserveur+lien+urlNoterFile;
    }

    static String reponseRetriveMessage;
}
