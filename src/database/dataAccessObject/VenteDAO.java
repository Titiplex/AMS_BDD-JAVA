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
                float quantity = rs.getFloat("quantite");

                listVentes.add(new Vente(id, idLotAchat, prixDuMoment, dateAchat, quantity));
            }

        } catch (SQLException e) {
            e.printStackTrace();
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
            float quantity = rs.getFloat("quantity");

            vente = new Vente(rsId, idLot, prixDuMoment, dateAchat, quantity);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vente;
    }

    @Override
    public void insertInTable(Vente entity) {
        String query = "INSERT INTO ams_vente (idlotachat, prixdumoment, datevente, quantite) VALUES " + entity.getValues() + " RETURNING idvente";
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) entity.setId(rs.getInt("idvente"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void modifyEntity(Vente entity) {
        String query = "UPDATE ams_contrat SET idvente = " + entity.getIdLotAchat()
                + ", quantite = " + entity.getQuantity()
                + ", datevente = '" + entity.getDateAchat() + "'"
                + ", prixdumoment = " + entity.getPrixDuMoment()
                + " WHERE idvente = " + entity.getId();
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteEntity(Vente entity) {
        String query = "DELETE FROM ams_vente WHERE idvente = " + entity.getId();
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
