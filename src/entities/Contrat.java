package entities;

import database.databaseUtilities.SqlEntity;

import java.time.LocalDate;

public class Contrat extends SqlEntity {

    private int id, numSiret, idProduit, quantiteMin;
    private LocalDate dateDebut, dateFin;
    private float prixFixe;

    public Contrat(int id, int numSiret, int idProduit, int quantiteMin, LocalDate dateDebut, LocalDate dateFin, float prixFixe) {
        super("ams_contrat");
        this.id = id;
        this.numSiret = numSiret;
        this.idProduit = idProduit;
        this.quantiteMin = quantiteMin;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.prixFixe = prixFixe;
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

    public int getQuantiteMin() {
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

    @Override
    public void getStruct() {

        super.getStruct("ams_contrat");

        // doubler les apostrophes pour éviter les pb de sql
        this.values = "(" +
                this.numSiret + ", " +
                this.idProduit + ", " +
                this.quantiteMin + ", '" +
                this.dateDebut.toString() + "', '" +
                this.dateFin.toString() + "', " +
                this.prixFixe +
                ")";
    }
}
