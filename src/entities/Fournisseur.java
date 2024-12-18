package entities;

import database.databaseUtilities.SqlEntity;

public class Fournisseur extends SqlEntity {
    private int numSiret;
    private String nomSociete;
    private String adresse;
    private String eMailPrincipal;

    public Fournisseur(String nomSociete, int numSiret, String adresse, String eMailPrincipal) {
        super("ams_fournisseur");
        this.nomSociete = nomSociete;
        this.numSiret = numSiret;
        this.adresse = adresse;
        this.eMailPrincipal = eMailPrincipal;

        this.createValues(this);
    }

    public String getNomSociete() {
        return nomSociete;
    }


    //doublon au cas ou
    public void setNomSociete(String nomSociete) {
        this.nomSociete = nomSociete;
    }

    public int getNumSiret() {
        return numSiret;
    }

    public void setNumSiret(int numSiret) {
        this.numSiret = numSiret;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String geteMailPrincipal() {
        return eMailPrincipal;
    }

    public void seteMailPrincipal(String eMailPrincipal) {
        this.eMailPrincipal = eMailPrincipal;
    }

    @Override
    public void setId(int id) {
    this.numSiret=id;
    }
}
