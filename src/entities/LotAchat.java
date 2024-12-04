package entities;

import java.util.Date;

import database.databaseUtilities.SqlEntity;

public class LotAchat extends SqlEntity{

	private int id, idContrat;
	private double quantite;
	private Date dateAchat, datePeremption;

	public LotAchat(int id, int idContrat, double quantite, Date dateAchat, Date datePeremption) {

		this.id = id;
		this.idContrat = idContrat;
		this.quantite = quantite;
		this.dateAchat = dateAchat;
		this.datePeremption = datePeremption;
	}

	public int getId() {
		return id;
	}
	public int getContratId() {
		return idContrat;
	}
	public double getQuantite() {
		return quantite;
	}
	public Date getDateAchat() {
		return dateAchat;
	}
	public Date getDatePeremption() {
		return datePeremption;
	}
	
	@Override
	public void getStruct() {
		
		super.getStruct("LotAchat");

		// doubler les apostrophes pour éviter les pb de sql
		this.values = "(" +
				this.id+ ", " + 
				this.idContrat + ", " + 
				this.quantite + ", " + 
				this.dateAchat.toString() + "', '" +
				this.datePeremption.toString() + "'" +
				")";
	}
}
