package entities;

import java.time.LocalDate;
import java.util.Date;

import database.databaseUtilities.SqlEntity;

public class Vente extends SqlEntity {

	private int id, idLotAchat, quantity;
	private float prixDuMoment;
	private LocalDate dateAchat;

	public Vente(int id, int idLotAchat, float prixDuMoment, LocalDate dateVente, int quantity) {
		super("ams_vente");
		this.id = id;
		this.idLotAchat = idLotAchat;
		this.prixDuMoment = prixDuMoment;
		this.dateAchat = dateVente;
		this.quantity = quantity;	
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
	
	public int getQuantity() {
		return quantity;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public void getStruct() {
		
		super.getStruct(Vente.tableName);

		// doubler les apostrophes pour éviter les pb de sql
		this.values = "(" +
				this.idLotAchat + ", '" + 
				this.prixDuMoment + ", '" + 
				this.quantity + ", '" + 
				this.dateAchat.toString() + "', " +
				")";
	}
}
