package banq.smartline;

import java.sql.Date;

/**
 * Created by macbookpro on 31/03/2017.
 */

public class VueFilAttente {
    private String id_service;
    private Date temps_entre;
    private int id_client;
    private String nom;
    private String token;
    private String sexe;

    public VueFilAttente(){

    }

    public VueFilAttente(String id_Serv,Date temp,int id_cli,String nom,String tok,String sex){
        this.setId_service(id_Serv);
        this.setTemps_entre(temp);
        this.setId_client(id_cli);
        this.setNom(nom);
        this.setToken(tok);
        this.setSexe(sex);
    }


    public String getId_service() {
        return id_service;
    }

    public void setId_service(String id_service) {
        this.id_service = id_service;
    }

    public Date getTemps_entre() {
        return temps_entre;
    }

    public void setTemps_entre(Date temps_entre) {
        this.temps_entre = temps_entre;
    }

    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
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

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }
}
