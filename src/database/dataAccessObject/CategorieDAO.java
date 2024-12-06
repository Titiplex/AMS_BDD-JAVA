package database.dataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.ConnectDatabase;
import database.databaseUtilities.DAOInterface;
import entities.Categorie;

public class CategorieDAO implements DAOInterface<Categorie> {
	@Override
	public List<Categorie> listAll() {
		List<Categorie> listCategories = new ArrayList<>();

		String query = "SELECT * from ams_categorie";

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void modifyEntity(Categorie entity) {
		// TODO Auto-generated method stub
	}

	@Override
	public Categorie getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteEntity(Categorie entity) {
		// TODO Auto-generated method stub
		
	}

}
