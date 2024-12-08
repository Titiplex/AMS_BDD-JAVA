package entities;

import database.databaseUtilities.SqlEntity;

/**Entity linked to a @Fournisseur that holds the personal human informations them.
 *
 */
public class Contact extends SqlEntity {
	private int id;
	private String nom, prenom, fonction, numTel, eMail;

	public void setId(int id) {
		this.id = id;
	}

	/**Constructor for a Contact.
	 * 
	 * @param id
	 * @param nom
	 * @param prenom
	 * @param fonction
	 * @param numTel
	 * @param eMail
	 */
	public Contact(int id,String nom, String prenom, String fonction, String numTel, String eMail) {
		super();
		this.id=id;
		this.nom = nom;
		this.prenom = prenom;
		this.fonction = fonction;
		this.numTel = numTel;
		this.eMail = eMail;
	}
	
	public int getId() {
		return id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getFonction() {
		return fonction;
	}

	public void setFonction(String fonction) {
		this.fonction = fonction;
	}

	public String getNumTel() {
		return numTel;
	}

	public void setNumTel(String numTel) {
		this.numTel = numTel;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}
	
	@Override
	public void getStruct() {
		
		super.getStruct("Contact");

		// doubler les apostrophes pour éviter les pb de sql
		this.values = "('" +
				this.nom.replace("'", "''") + "', '" + 
				this.prenom.replace("'", "''") + "', '" + 
				this.fonction.replace("'", "''") + "', '" + 
				this.numTel.replace("'", "''") + "', " + 
				this.eMail.replace("'", "''") + "'" + 
				")";
	}
}
