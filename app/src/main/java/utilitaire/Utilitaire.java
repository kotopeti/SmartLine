package utilitaire;

import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by macbookpro on 30/03/2017.
 */

public class Utilitaire {
    public static String formaterAr(double montant) {
        try {
            if (montant == 0) {
                return "0";
            }
            NumberFormat nf = NumberFormat.getInstance(Locale.FRENCH);
            //nf = new DecimalFormat("### ###,##");
            //nf.setMaximumFractionDigits(2);
            nf.setMinimumFractionDigits(2);
            String s = nf.format(montant);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formaterAr(String montant) {
        return formaterAr(stringToDouble(montant));
    }

    public static double stringToDouble(String s) {
        double d1;
        try {
            String ns = replaceVirgule(s);
            ns = enleverEspace(ns);
            //ns=ns.trim();
            Double ger = new Double(ns);
            double d = ger.doubleValue();
            return d;
        } catch (NumberFormatException ex) {
            d1 = 0.0D;
            //System.out.println(" ============== D1 ====== " + d1);
        }
        return d1;
    }

    public static String replaceVirgule(String s) {

        //s = s.replace('\'', '\''');
        s = s.replace(',', '.');

        return s;
    }

    public static String enleverEspace(String s) {
        String ch = "";
        int l = s.length();
        char c;
        for (int i = 0; i < l; i++) {
            c = s.charAt(i);
            if (c != ' ') {
                ch += c;
            }
        }
        return ch;
    }

    public static java.sql.Date stringDate(String daty) {
        if (daty == null || daty.compareTo("") == 0) {
            return null;
        }
        java.sql.Date sqlDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date date = sdf.parse(daty);

            sqlDate = new Date(date.getTime());
        } catch (Exception e) {
            System.out.println("Error stringDate :" + e.getMessage());
        }
        return sqlDate;
    }

    public static String datetostring(java.sql.Date d) {
        String daty = null;
        SimpleDateFormat dateJava = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        daty = dateJava.format(d);
        return daty;
    }


    public static String datetoHeure(java.sql.Date d) {
        String daty = null;
        SimpleDateFormat dateJava = new SimpleDateFormat("HH:mm:ss");
        daty = dateJava.format(d);
        return daty;
    }

    public static java.util.Date stringDateUtil(String daty) {
        if (daty == null || daty.compareTo("") == 0) {
            return null;
        }
        java.util.Date sqlDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date date = sdf.parse(daty);

            sqlDate = new Date(date.getTime());
        } catch (Exception e) {
            System.out.println("Error stringDate :" + e.getMessage());
        }
        return sqlDate;
    }

    public static String diffTempsDate(long diff){
        long heure = TimeUnit.MILLISECONDS.toHours(diff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        long sec = diff / 1000 % 60;
        String h = "";
        String min = "";
        String s ="";
        if(heure>0)h = heure+" heure(s) ";
        if(minutes>0)min = minutes+" minute(s) ";
        if(sec>0)s = sec+" seconde(s)";
        return h+min+s;
    }
}
