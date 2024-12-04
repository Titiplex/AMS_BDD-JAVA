package entities;

import java.util.Date;

import database.databaseUtilities.SqlEntity;

public class Contrat extends SqlEntity {
	
	private int id, numSiret, idProduit, quantiteMin;
	private Date dateDebut, dateFin;
	private double prixFixe;
	public Contrat(int id, int numSiret, int idProduit, int quantiteMin, Date dateDebut, Date dateFin, double prixFixe) {
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
	public int getIdProduit() {
		return idProduit;
	}
	public int getQuantiteMin() {
		return quantiteMin;
	}
	public Date getDateDebut() {
		return dateDebut;
	}
	public Date getDateFin() {
		return dateFin;
	}
	public double getPrixFixe() {
		return prixFixe;
	}
	
	@Override
	public void getStruct() {
		
		super.getStruct("Contrat");

		// doubler les apostrophes pour éviter les pb de sql
		this.values = "(" +
				this.id+ ", '" + 
				this.numSiret + ", " + 
				this.idProduit + ", " + 
				this.quantiteMin + ", '" + 
				this.dateDebut.toString() + "', '" + 
				this.dateFin.toString() + "', " +
				this.prixFixe +
				")";
	}
}
