package database.dataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import database.ConnectDatabase;
import database.databaseUtilities.DAOInterface;
import entities.LotAchat;

public class LotAchatDAO implements DAOInterface<LotAchat> {

	@Override
	public List<LotAchat> listAll() {
		List<LotAchat> listLotAchats = new ArrayList<>();

		String query = "SELECT * from ams_lotachat";

		try {
			Connection conn = ConnectDatabase.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				int rsId = rs.getInt("id");
				int contratId = rs.getInt("contratId");
				double quantite = rs.getDouble("quantite");
				Date dateAchat = rs.getDate("dateAchat");
				Date datePeremption = rs.getDate("datePeremption");

				listLotAchats.add(new LotAchat(rsId, contratId, quantite, dateAchat, datePeremption));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectDatabase.closeConnection();
		}
		return listLotAchats;
	}

	@Override
	public void insertInTable(LotAchat entity) {
		// TODO Auto-generated method stub
		//Attention getValue de retourner numero de siret du fournisseur pour la table
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
	public void modifyEntity(LotAchat entity) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public LotAchat getById(int id) {
		// TODO Auto-generated method stub
		String query = "SELECT * FROM LotAchat WHERE id = "+ id;
		LotAchat lotAchat = null;
		try {
			Connection conn = ConnectDatabase.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			// on part du principe que les identifiants sont uniques
			int rsId = rs.getInt("id");
			int contratId = rs.getInt("contratId");
			double quantite = rs.getDouble("quantite");
			Date dateAchat = rs.getDate("dateAchat");
			Date datePeremption = rs.getDate("datePeremption");
			
			lotAchat = new LotAchat(rsId, contratId, quantite, dateAchat, datePeremption);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectDatabase.closeConnection();
		}
		return lotAchat;
	}

	@Override
	public void deleteEntity(LotAchat entity) {
		// TODO Auto-generated method stub
		
	}

}
