package entities;

import database.databaseUtilities.SetterInterface;
import database.databaseUtilities.SqlEntity;
import exceptions.MesureTypeException;
import exceptions.ProductNameLengthException;


public class Produit extends SqlEntity implements SetterInterface<Produit> {
    // id déterminé automtiquement par sql
    private static final int MAX_NAME_LENGTH = 15;
    private int id;
    private String nom, description, mesure;
    private float prixVenteActuel;

    /**
     * @param id
     * @param prixVenteActuel
     * @param nom
     * @param description
     * @param mesure
     */
    public Produit(int id, float prixVenteActuel, String nom, String description, String mesure) {
        super("ams_produit");
        try {
            this.id = id;
            validateProductName(nom);
            this.prixVenteActuel = prixVenteActuel;
            //validateMesure(mesure);
            this.mesure = mesure;
            this.nom = nom;
            this.description = description;
        } catch (ProductNameLengthException e) {
            System.out.println("Erreur de construction : " + e.getMessage());
        }
		/*
		catch (MesureTypeException e) {
			System.out.println("Erreur Achat : " + e.getMessage());
		}
		 */
    }

    public void setter(String nom, String description, String categorie, String mesure, float prixVenteActuel) {
        this.setNom(nom);
        this.setDescription(description);
        this.setMesure(mesure);
        this.setPrixVenteActuel(prixVenteActuel);
        Produit produit = new Produit(this.getId(), this.getPrixVenteActuel(), this.getNom(), this.getDescription(), this.getMesure());
    }

    private void setMesure(String mesure) {
        try {
            validateMesure(mesure);
            this.mesure = mesure;
        } catch (MesureTypeException e) {
            System.out.println("Erreur Achat : " + e.getMessage());
        }
    }

    public String getMesure() {
        return this.mesure;
    }

    public int getId() {
        return id;
    }

    public float getPrixVenteActuel() {
        return prixVenteActuel;
    }

    private void setPrixVenteActuel(float prixVenteActuel) {
        this.prixVenteActuel = prixVenteActuel;
    }

    public String getNom() {
        return nom;
    }

    private void setNom(String nom) {
        try {
            validateProductName(nom);
            this.nom = nom;
        } catch (ProductNameLengthException e) {
            System.out.println("Erreur de modification : " + e.getMessage());
        }
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    /**
     * Verifies the name of the product entity
     *
     * @param nom
     * @throws ProductNameLengthException if the name is more than 15 characters
     */
    private void validateProductName(String nom) throws ProductNameLengthException {
        if (nom.length() > MAX_NAME_LENGTH) {
            throw new ProductNameLengthException("Le nom du produit " + nom + " ne peut excéder " + MAX_NAME_LENGTH + " caractères.");
        }
    }

    /**
     * Validates the measure type.
     *
     * @param mesure
     * @throws MesureTypeException if the measure type isn't compatible.
     */
    private void validateMesure(String mesure) throws MesureTypeException {
        if (mesure != "U" || mesure != "kg")
            throw new MesureTypeException("Mesure \"" + mesure + "\" du produit incompatible !");
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void getStruct() {

        super.getStruct(Produit.tableName);

        // doubler les apostrophes pour éviter les pb de sql
        this.values = "('" +
                this.nom.replace("'", "''") + "', " +
                this.prixVenteActuel + ", '" +
                this.mesure.replace("'", "''") + "', '" +
                this.description.replace("'", "''") + "'" +
                ")";
    }

    @Override
    public void setter(Produit produit) {
        // TODO Auto-generated method stub

    }
}
