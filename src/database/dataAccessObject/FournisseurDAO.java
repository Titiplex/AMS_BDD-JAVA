package database.dataAccessObject;

import database.databaseUtilities.ConnectDatabase;
import database.databaseUtilities.DAOInterface;
import entities.Fournisseur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FournisseurDAO implements DAOInterface<Fournisseur> {

    @Override
    public List<Fournisseur> listAll() {
        List<Fournisseur> listFournisseurs = new ArrayList<>();

        String query = "SELECT * from ams_fournisseur";

        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int numSiretRs = rs.getInt("numsiret");
                String nomSociete = rs.getString("nom_societe");
                String adresse = rs.getString("adresse");
                String eMailPrincipal = rs.getString("email");

                listFournisseurs.add(new Fournisseur(nomSociete, numSiretRs, adresse, eMailPrincipal));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectDatabase.closeConnection();
        }

        return listFournisseurs;
    }

    @Override
    public void insertInTable(Fournisseur entity) {
        String query = "INSERT INTO ams_fournisseur (numsiret, nom_societe, adresse, email) VALUES " + entity.getValues();
        String queryID = "SELECT idlotachat FROM ams_lotachat WHERE numsiret = " + entity.getNumSiret()
                + " AND adresse = '" + entity.getAdresse() + "'"
                + " AND nom_societe = '" + entity.getNomSociete() + "'"
                + " AND email = '" + entity.geteMailPrincipal() + "'";
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.executeQuery();
            stmt = conn.prepareStatement(queryID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) entity.setNumSiret(rs.getInt("numsiret"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectDatabase.closeConnection();
        }
    }

    @Override
    public Fournisseur getById(int numSiret) {
        // TODO Auto-generated method stub
        String query = "SELECT * FROM ams_fournisseur WHERE numsiret = " + numSiret;
        Fournisseur fournisseur = null;
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // on part du principe que les identifiants sont uniques
            if (rs.next()) {
                int numSiretRs = rs.getInt("numsiret");
                String nomSociete = rs.getString("nom_societe");
                String adresse = rs.getString("adresse");
                String eMailPrincipal = rs.getString("email");

                fournisseur = new Fournisseur(nomSociete, numSiretRs, adresse, eMailPrincipal);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectDatabase.closeConnection();
        }
        return fournisseur;
    }

    @Override
    public void deleteEntity(Fournisseur entity) {
        String query = "DELETE FROM ams_fournisseur WHERE idlotachat = " + entity.getNumSiret();
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
    public void modifyEntity(Fournisseur entity) {
        String query = "UPDATE ams_lotachat SET numsiret = " + entity.getNumSiret()
                + " AND adresse = '" + entity.getAdresse() + "'"
                + " AND nom_societe = '" + entity.getNomSociete() + "'"
                + " AND email = '" + entity.geteMailPrincipal() + "'"
                + " WHERE numsiret = " + entity.getNumSiret();
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectDatabase.closeConnection();
        }
    }
}
