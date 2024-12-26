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
        }

        return listFournisseurs;
    }

    @Override
    public void insertInTable(Fournisseur entity) {
        String query = "INSERT INTO ams_fournisseur (numsiret, nom_societe, adresse, email) VALUES " + entity.getValues();
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Fournisseur getById(int numSiret) {
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
        }
        return fournisseur;
    }

    @Override
    public void deleteEntity(Fournisseur entity) {
        String query = "DELETE FROM ams_fournisseur WHERE idlotachat = " + entity.getNumSiret();
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifyEntity(Fournisseur entity) {
        String query = "UPDATE ams_fournisseur SET numsiret = " + entity.getNumSiret()
                + ", adresse = '" + entity.getAdresse() + "'"
                + ", nom_societe = '" + entity.getNomSociete() + "'"
                + ", email = '" + entity.geteMailPrincipal() + "'"
                + " WHERE numsiret = " + entity.getNumSiret();
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
