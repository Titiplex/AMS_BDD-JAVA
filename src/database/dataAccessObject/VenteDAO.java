package database.dataAccessObject;

import database.databaseUtilities.ConnectDatabase;
import database.databaseUtilities.DAOInterface;
import entities.Vente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VenteDAO implements DAOInterface<Vente> {

    @Override
    public List<Vente> listAll() {
        List<Vente> listVentes = new ArrayList<>();

        String query = "SELECT * from ams_vente";

        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("idvente");
                int idLotAchat = rs.getInt("idlotachat");
                float prixDuMoment = rs.getFloat("prixdumoment");
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
        String query = "SELECT * FROM Vente WHERE id = " + id;
        Vente vente = null;
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // on part du principe que les identifiants sont uniques
            int rsId = rs.getInt("id");
            int idLot = rs.getInt("idLotAchat");
            float prixDuMoment = rs.getFloat("prixDuMoment");
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
        String query = "INSERT INTO ams_vente (idlotachat, prixdumoment, quantite, datevente) VALUES " + entity.getValues();
        String queryID = "SELECT idlotachat FROM ams_lotachat WHERE idvente = " + entity.getIdLotAchat()
                + " AND quantite = " + entity.getQuantity()
                + " AND datevente = '" + entity.getDateAchat() + "'"
                + " AND prixdumoment = " + entity.getPrixDuMoment();
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.executeQuery();
            stmt = conn.prepareStatement(queryID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) entity.setId(rs.getInt("idvente"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectDatabase.closeConnection();
        }
    }


    @Override
    public void modifyEntity(Vente entity) {
        String query = "UPDATE ams_lotachat SET idvente = " + entity.getIdLotAchat()
                + " AND quantite = " + entity.getQuantity()
                + " AND datevente = '" + entity.getDateAchat() + "'"
                + " AND prixdumoment = " + entity.getPrixDuMoment()
                + " WHERE idvente = " + entity.getId();
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

    @Override
    public void deleteEntity(Vente entity) {
        String query = "DELETE FROM ams_vente WHERE idvente = " + entity.getId();
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

}
