package entities;

import database.databaseUtilities.SqlEntity;

import java.time.LocalDate;

public class Vente extends SqlEntity {

    private int id;
    private int idLotAchat;
    private float prixDuMoment;
    private LocalDate dateAchat;
    private float quantity;

    public Vente() {
        super();
        id=0;
        idLotAchat=0;
        prixDuMoment=0;
        dateAchat=LocalDate.now();
        quantity=0;
    }

    public Vente(int id, int idLotAchat, float prixDuMoment, LocalDate dateVente, float quantity) {
        super("ams_vente");
        this.id = id;
        this.idLotAchat = idLotAchat;
        this.prixDuMoment = prixDuMoment;
        this.dateAchat = dateVente;
        this.quantity = quantity;

        this.createValues(this);
    }

    public int getId() {
        return id;
    }

    public int getIdLotAchat() {
        return idLotAchat;
    }

    public float getPrixDuMoment() {
        return prixDuMoment;
    }

    public LocalDate getDateAchat() {
        return dateAchat;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setId(int id) {
        this.id = id;
    }

}
