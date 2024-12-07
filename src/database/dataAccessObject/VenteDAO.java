package database.dataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import database.ConnectDatabase;
import database.databaseUtilities.DAOInterface;
import entities.Vente;

public class VenteDAO implements DAOInterface<Vente> {

	@Override
	public List<Vente> listAll() {
		List<Vente> listVentes = new ArrayList<>();

		String query = "SELECT * from ams_vente";

		try {
			Connection conn = ConnectDatabase.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				int id = rs.getInt("idvente");
				int idLotAchat = rs.getInt("idlotachat");
				int prixDuMoment = rs.getInt("prixdumoment");
				LocalDate dateAchat = rs.getDate("datevente").toLocalDate();
				int quantity = rs.getInt("quantite");

				listVentes.add(new Vente(id, idLotAchat, prixDuMoment, dateAchat, quantity));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectDatabase.closeConnection();
		}
		return listVentes;
	}
	
	
	@Override
	public Vente getById(int id) {
		// TODO Auto-generated method stub
		String query = "SELECT * FROM Vente WHERE id = "+ id;
		Vente vente = null;
		try {
			Connection conn = ConnectDatabase.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			// on part du principe que les identifiants sont uniques
			int rsId = rs.getInt("id");
			int idLot = rs.getInt("idLotAchat");
			int prixDuMoment = rs.getInt("prixDuMoment");
			LocalDate dateAchat = rs.getDate("dateAchat").toLocalDate();
			int quantity = rs.getInt("quantity");
			
			vente = new Vente(rsId, idLot, prixDuMoment, dateAchat, quantity);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectDatabase.closeConnection();
		}
		return vente;
	}

	@Override
	public void insertInTable(Vente entity) {
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
	public void modifyEntity(Vente entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteEntity(Vente entity) {
		// TODO Auto-generated method stub
		
	}

}
