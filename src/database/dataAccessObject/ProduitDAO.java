package database.dataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.ConnectDatabase;
import database.databaseUtilities.DAOInterface;
import entities.Produit;

public class ProduitDAO implements DAOInterface<Produit> {

    @Override
    public List<Produit> listAll() {
        // TODO Auto-generated method stub

        List<Produit> listProduits = new ArrayList<>();

        String query = "SELECT * from Produits";

        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String description = rs.getString("description");
                String categorie = rs.getString("categorie");
                String mesure = rs.getString("mesure");
                float prixVenteActuel = rs.getFloat("prixVenteActuel");

                listProduits.add(new Produit(id, prixVenteActuel, nom, description, categorie, mesure));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectDatabase.closeConnection();
        }
        return listProduits;
    }

    @Override
    public void insertInTable(Produit entity) {
        // TODO Auto-generated method stub
        String query = "INSERT ..." + entity.getValues();

        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectDatabase.closeConnection();
        }
    }

    @Override
    public Produit getById(int id) {
        // TODO Auto-generated method stub
        String query = "SELECT * FROM Produit WHERE id = \""+id+"\"";
        Produit produit = null;
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // on part du principe que les identifiants sont uniques
            int rsId = rs.getInt("id");
            String nom = rs.getString("nom");
            String description = rs.getString("description");
            String categorie = rs.getString("categorie");
            String mesure = rs.getString("mesure");
            float prixVenteActuel = rs.getFloat("prixVenteActuel");

            produit = new Produit(rsId, prixVenteActuel, nom, description, categorie, mesure);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectDatabase.closeConnection();
        }
        return produit;
    }

    @Override
    public void modifyEntity(Produit entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteEntity(Produit entity) {
        // TODO Auto-generated method stub

    }
}
