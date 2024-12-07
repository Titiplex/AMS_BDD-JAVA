package database.dataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.databaseUtilities.ConnectDatabase;
import database.databaseUtilities.JoinDAOInterface;
import entities.Contact;
import entities.Fournisseur;

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
        } finally {
            ConnectDatabase.closeConnection();
        }
        return listContacts;
    }

    @Override
    public void insertInTable(Contact entity) {
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
    public void modifyEntity(Contact entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public Contact getById(int id) {
        // TODO Auto-generated method stub
        String query = "SELECT * FROM Contact WHERE id = " + id;
        Contact contact = null;
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // on part du principe que les identifiants sont uniques
            int rsId = rs.getInt("id");
            String nom = rs.getString("nom");
            String prenom = rs.getString("prenom");
            String fonction = rs.getString("fonction");
            String numTel = rs.getString("numTel");
            String eMail = rs.getString("eMail");
            contact = new Contact(rsId, nom, prenom, fonction, numTel, eMail);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectDatabase.closeConnection();
        }
        return contact;
    }

    @Override
    public void deleteEntity(Contact entity) {
        // TODO Auto-generated method stub

    }

    /**
     * Selects Contacts associated with a Fournisseur
     *
     * @param joinEntity
     * @return
     */
    @Override
    public List<Contact> listAllFromJoin(Fournisseur joinEntity) {
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
        } finally {
            ConnectDatabase.closeConnection();
        }
        return listContacts;
    }
}
