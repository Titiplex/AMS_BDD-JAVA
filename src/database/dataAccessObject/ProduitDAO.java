package database.dataAccessObject;

import database.databaseUtilities.ConnectDatabase;
import database.databaseUtilities.DAOInterface;
import entities.Produit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProduitDAO implements DAOInterface<Produit> {

    @Override
    public List<Produit> listAll() {
        List<Produit> listProduits = new ArrayList<>();

        String query = "SELECT * from ams_produit";

        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("idproduit");
                if (id != 1) {
                    String nom = rs.getString("nom");
                    String description = rs.getString("description");
                    String mesure = rs.getString("mesure");
                    float prixVenteActuel = rs.getFloat("prixventeactuel");

                    listProduits.add(new Produit(id, prixVenteActuel, nom, description, mesure));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listProduits;
    }

    @Override
    public void insertInTable(Produit entity) {
        String query = "INSERT INTO ams_produit (nom, prixventeactuel, mesure, description) VALUES " + entity.getValues() + " RETURNING idproduit";
        System.out.println(query);
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) entity.setId(rs.getInt("idproduit"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Produit getById(int id) {
        String query = "SELECT * FROM ams_produit WHERE idproduit = " + id;
        Produit produit = null;
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // on part du principe que les identifiants sont uniques
                int rsId = rs.getInt("idproduit");
                String nom = rs.getString("nom");
                String description = rs.getString("description");
                String mesure = rs.getString("mesure");
                float prixVenteActuel = rs.getFloat("prixventeactuel");

                produit = new Produit(rsId, prixVenteActuel, nom, description, mesure);
            } else System.out.println("aucun produit avec l'id " + id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produit;
    }

    @Override
    public void modifyEntity(Produit entity) {
        String query = "UPDATE ams_produit SET nom = '" + entity.getNom() + "',"
                + " description = '" + entity.getDescription() + "',"
                + " mesure = '" + entity.getMesure() + "',"
                + " prixventeactuel = " + entity.getPrixVenteActuel()
                + " WHERE idproduit = " + entity.getId();
        System.out.println(query);
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteEntity(Produit entity) {
        String query = "DELETE FROM ams_produit WHERE idproduit = " + entity.getId();
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
