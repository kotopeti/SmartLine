package banq.smartline;

import java.io.Serializable;

/**
 * Created by macbookpro on 29/03/2017.
 */

public class Identification implements Serializable {
    private String nom;
    private String token;
    private int sexe;

    public Identification(){

    }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getSexe() {
        return sexe;
    }

    public void setSexe(int sexe) {
        this.sexe = sexe;
    }
}
