package database.dataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.databaseUtilities.ConnectDatabase;
import database.databaseUtilities.DAOInterface;
import entities.Categorie;

public class CategorieDAO implements DAOInterface<Categorie> {
	private int temporaryID;

	public void setTemporaryID(int temporaryID) {
		this.temporaryID = temporaryID;
	}

	@Override
	public List<Categorie> listAll() {
		List<Categorie> listCategories = new ArrayList<>();

		String query = "SELECT DISTINCT categorie from ams_produit_categorie";

		try {
			Connection conn = ConnectDatabase.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				String singleCategorie  = rs.getString("categorie");

				listCategories.add(new Categorie(singleCategorie));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectDatabase.closeConnection();
		}
		return listCategories;
	}

	@Override
	public void insertInTable(Categorie entity) {
		String query = "INSERT INTO ams_produit_categorie (idproduit, categorie) VALUES (" + this.temporaryID + ", " + entity.getCategorie() + ") RETURNING idproduit";
		try {
			Connection conn = ConnectDatabase.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectDatabase.closeConnection();
		}
	}

	@Override
	public void modifyEntity(Categorie entity) {
		String query = "UPDATE ams_lotachat SET categorie = " + entity.getCategorie()
				+ "WHERE idproduit = " + this.temporaryID + " AND categorie = " + entity.getCategorie();
		try {
			Connection conn = ConnectDatabase.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			ConnectDatabase.closeConnection();
		}
	}

	@Override
	public Categorie getById(int id) {
		String query = "SELECT categorie from ams_produit_categorie WHERE idproduit = " + id;

		Categorie categorie = null;
		try {
			Connection conn = ConnectDatabase.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			if(rs.next()) {
				categorie  = new Categorie(rs.getString("categorie"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectDatabase.closeConnection();
		}
		return categorie;
	}

	@Override
	public void deleteEntity(Categorie entity) {
		String query = "DELETE FROM ams_produit_categorie WHERE idproduit = " + this.temporaryID;
		try {
			Connection conn = ConnectDatabase.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectDatabase.closeConnection();
		}
	}
}
