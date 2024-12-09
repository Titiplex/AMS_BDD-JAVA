package entities;

import database.databaseUtilities.SqlEntity;

import java.time.LocalDate;

public class LotAchat extends SqlEntity {

    private int id;
    private int idContrat;
    private int quantite;
    private LocalDate dateAchat;
    private LocalDate datePeremption;

    public LotAchat(int id, int idContrat, int quantite, LocalDate dateAchat, LocalDate datePeremption) {
        super("ams_lotachat");

        this.id = id;
        this.idContrat = idContrat;
        this.quantite = quantite;
        this.dateAchat = dateAchat;
        this.datePeremption = datePeremption;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public void setDateAchat(LocalDate dateAchat) {
        this.dateAchat = dateAchat;
    }

    public void setDatePeremption(LocalDate datePeremption) {
        this.datePeremption = datePeremption;
    }

    public int getId() {
        return id;
    }

    public int getContratId() {
        return idContrat;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDateAchat() {
        return dateAchat;
    }

    public LocalDate getDatePeremption() {
        return datePeremption;
    }

    @Override
    public void getStruct() {

        super.getStruct(LotAchat.tableName);

        // doubler les apostrophes pour éviter les pb de sql
        /*this.values = "(" + this.idContrat + ", " + this.quantite + ", '" + this.dateAchat.toString() + "', '" + this.datePeremption.toString() + "'" + ")";*/
    }
}
