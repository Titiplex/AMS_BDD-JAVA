package entities;

import database.databaseUtilities.SqlEntity;

import java.time.LocalDate;

public class Contrat extends SqlEntity {

    private int id;
    private int idProduit;
    private int numSiret;
    private float quantiteMin;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private float prixFixe;

    public Contrat(){
        super();
        id=0;
        idProduit=0;
        numSiret=0;
        quantiteMin=0;
        dateDebut=null;
        dateFin=null;
        prixFixe=0;
    }

    public Contrat(int id, int numSiret, int idProduit, float quantiteMin, LocalDate dateDebut, LocalDate dateFin, float prixFixe) {
        super("ams_contrat");
        this.id = id;
        this.numSiret = numSiret;
        this.idProduit = idProduit;
        this.quantiteMin = quantiteMin;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.prixFixe = prixFixe;

        this.createValues(this);
    }

    public int getId() {
        return id;
    }

    public int getnumSiret() {
        return numSiret;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public float getQuantiteMin() {
        return quantiteMin;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public float getPrixFixe() {
        return prixFixe;
    }
}
