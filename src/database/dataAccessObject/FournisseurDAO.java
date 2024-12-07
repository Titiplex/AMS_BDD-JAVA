package database.dataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.databaseUtilities.ConnectDatabase;
import database.databaseUtilities.DAOInterface;
import entities.Fournisseur;

public class FournisseurDAO implements DAOInterface<Fournisseur> {

	@Override
	public List<Fournisseur> listAll() {
		List<Fournisseur> listFournisseurs = new ArrayList<>();

		String query = "SELECT * from ams_fournisseur";

		try {
			Connection conn = ConnectDatabase.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
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
	public Fournisseur getById(int numSiret) {
		// TODO Auto-generated method stub
		String query = "SELECT * FROM Fournisseur WHERE id = "+ numSiret;
		Fournisseur fournisseur = null;
		try {
			Connection conn = ConnectDatabase.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			// on part du principe que les identifiants sont uniques
			int numSiretRs = rs.getInt("numSiret");
			String nomSociete = rs.getString("nomSociete");
			String adresse = rs.getString("adresse");
			String eMailPrincipal = rs.getString("eMailPrincipal");
			
			fournisseur = new Fournisseur(nomSociete, numSiretRs, adresse, eMailPrincipal);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectDatabase.closeConnection();
		}
		return fournisseur;
	}

	@Override
	public void deleteEntity(Fournisseur entity) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void modifyEntity(Fournisseur entity) {
		// TODO Auto-generated method stub
		
	}

}
