package database.dataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.ConnectDatabase;
import database.databaseUtilities.DAOInterface;
import entities.Contact;

public class ContactDAO implements DAOInterface<Contact> {

	@Override
	public List<Contact> listAll() {
		List<Contact> listContacts = new ArrayList<>();

		String query = "SELECT * from Contact";

		try {
			Connection conn = ConnectDatabase.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				int rsId = rs.getInt("id");
				String nom = rs.getString("nom");
				String prenom = rs.getString("prenom");
				String fonction = rs.getString("fonction");
				String numTel = rs.getString("numTel");
				String eMail = rs.getString("eMail");

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
		String query = "SELECT * FROM Contact WHERE id = "+ id;
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

}
