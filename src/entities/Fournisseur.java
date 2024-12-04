package entities;

import database.databaseUtilities.SqlEntity;

public class Fournisseur extends SqlEntity{
	private int numSiret;
	private String nomSociete, adresse, eMailPrincipal;

	public Fournisseur(String nomSociete, int numSiret, String adresse, String eMailPrincipal) {
		super();
		this.nomSociete = nomSociete;
		this.numSiret = numSiret;
		this.adresse = adresse;
		this.eMailPrincipal = eMailPrincipal;
	}

	public String getNomSociete() {
		return nomSociete;
	}

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
	public void getStruct() {
		
		super.getStruct("Fournisseur");

		// doubler les apostrophes pour éviter les pb de sql
		this.values = "(" +
				this.numSiret+ ", '" + 
				this.nomSociete.replace("'", "''") + "', '" + 
				this.adresse.replace("'", "''") + "', '" + 
				this.eMailPrincipal.replace("'", "''") + "'" + 
				")";
	}
}
