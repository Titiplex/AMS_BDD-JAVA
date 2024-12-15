package entities;

import database.databaseUtilities.SetterInterface;
import database.databaseUtilities.SqlEntity;
import exceptions.entityAttributesExceptions.MesureTypeException;
import exceptions.entityAttributesExceptions.ProductNameLengthException;


public class Produit extends SqlEntity implements SetterInterface<Produit> {
    // id déterminé automtiquement par sql
    private int id;
    private String nom;
    private float prixVenteActuel;
    private String mesure;
    private String description;

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
            validateMesure(mesure);
            this.mesure = mesure;
            this.nom = nom;
            this.description = description;
        } catch (MesureTypeException | ProductNameLengthException e) {
            e.printStackTrace();
        } finally {
            this.createValues(this);
        }
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
        if (nom.length() > 15) {
            throw new ProductNameLengthException();
        }
    }

    /**
     * Validates the measure type.
     *
     * @param mesure
     * @throws MesureTypeException if the measure type isn't compatible.
     */
    private void validateMesure(String mesure) throws MesureTypeException {
        if (!mesure.equals("U") && !mesure.equals("kg") && !mesure.equals("L"))
            throw new MesureTypeException();
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setter(Produit produit) {
        // TODO Auto-generated method stub

    }
}
