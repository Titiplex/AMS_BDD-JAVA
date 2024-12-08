package entities;

import java.time.LocalDate;
import java.util.Date;

import database.databaseUtilities.SqlEntity;

public class Vente extends SqlEntity {

	private int id, idLotAchat, prixDuMoment, quantity;
	private LocalDate dateAchat;

	public Vente(int id, int idLotAchat, int prixDuMoment, LocalDate dateVente, int quantity) {
		super();
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

	public int getPrixDuMoment() {
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
		
		super.getStruct("Vente");

		// doubler les apostrophes pour éviter les pb de sql
		this.values = "(" +
				this.idLotAchat + ", '" + 
				this.prixDuMoment + ", '" + 
				this.quantity + ", '" + 
				this.dateAchat.toString() + "', " +
				")";
	}
}
