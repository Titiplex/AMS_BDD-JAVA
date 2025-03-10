package database.dataAccessObject;

import database.databaseUtilities.ConnectDatabase;
import database.databaseUtilities.JoinDAOInterface;
import entities.Contact;
import entities.Fournisseur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO implements JoinDAOInterface<Contact, Fournisseur> {

    @Override
    public List<Contact> listAll() {
        List<Contact> listContacts = new ArrayList<>();

        String query = "SELECT * from ams_contact";

        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int rsId = rs.getInt("idcontact");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String fonction = rs.getString("fonction");
                String numTel = rs.getString("tel");
                String eMail = rs.getString("email");

                listContacts.add(new Contact(rsId, nom, prenom, fonction, numTel, eMail));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listContacts;
    }

    @Override
    public void insertInTable(Contact entity) {
        String query = "INSERT INTO ams_contact (nom, prenom, fonction, tel, email) VALUES " + entity.getValues() + " RETURNING idcontact";
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) entity.setId(rs.getInt("idcontact"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifyEntity(Contact entity) {
        String query = "UPDATE ams_contact SET nom = '" + entity.getNom() + "'"
                + ", prenom = '" + entity.getPrenom() + "'"
                + ", fonction = '" + entity.getFonction() + "'"
                + ", tel = '" + entity.getNumTel() + "'"
                + ", email = '" + entity.geteMail() + "'"
                + " WHERE idcontact = " + entity.getId();
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Contact getById(int id) {
        String query = "SELECT * FROM ams_contact WHERE idcontact = " + id;
        Contact contact = null;
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // on part du principe que les identifiants sont uniques
            if (rs.next()) {
                int rsId = rs.getInt("idcontact");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String fonction = rs.getString("fonction");
                String numTel = rs.getString("tel");
                String eMail = rs.getString("email");
                contact = new Contact(rsId, nom, prenom, fonction, numTel, eMail);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contact;
    }

    @Override
    public void deleteEntity(Contact entity) {
        String query = "DELETE FROM ams_contact WHERE idcontact = " + entity.getId();
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Selects Contacts associated with a Fournisseur
     *
     * @param joinEntity
     * @return
     */
    @Override
    public List<Contact> listAllFromParameter(Fournisseur joinEntity) {
        List<Contact> listContacts = new ArrayList<>();

        String query = "SELECT * from ams_fournisseur_contact join ams_contact using(idcontact) where idfournisseur=" + joinEntity.getNumSiret();

        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int rsId = rs.getInt("idcontact");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String fonction = rs.getString("fonction");
                String numTel = rs.getString("tel");
                String eMail = rs.getString("email");

                listContacts.add(new Contact(rsId, nom, prenom, fonction, numTel, eMail));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listContacts;
    }

    @Override
    public void insertInTable(Contact entity, Fournisseur joinEntity) {
        String query = "INSERT INTO ams_contact (nom, prenom, fonction, tel, email) VALUES " + entity.getValues() + " RETURNING idcontact";
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                entity.setId(rs.getInt("idcontact"));
                query = "INSERT INTO ams_fournisseur_contact (idcontact, idfournisseur) VALUES (" + entity.getId() + ", " + joinEntity.getNumSiret() + ")";
                stmt = conn.prepareStatement(query);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
