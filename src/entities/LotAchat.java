package entities;

import database.databaseUtilities.SqlEntity;

import java.time.LocalDate;

public class LotAchat extends SqlEntity {

    private int id;
    private int idContrat;
    private float quantite;
    private LocalDate dateAchat;
    private LocalDate datePeremption;

    public LotAchat() {
        super();
        id=0;
        idContrat=0;
        quantite=0;
        dateAchat=LocalDate.now();
        datePeremption=LocalDate.now();
    }

    public LotAchat(int id, int idContrat, float quantite, LocalDate dateAchat, LocalDate datePeremption) {
        super("ams_lotachat");

        this.id = id;
        this.idContrat = idContrat;
        this.quantite = quantite;
        this.dateAchat = dateAchat;
        this.datePeremption = datePeremption;

        this.createValues(this);
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

    public float getQuantite() {
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

}
