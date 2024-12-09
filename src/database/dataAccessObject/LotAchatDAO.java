package database.dataAccessObject;

import database.databaseUtilities.ConnectDatabase;
import database.databaseUtilities.DAOInterface;
import database.databaseUtilities.JoinDAOInterface;
import entities.Contact;
import entities.LotAchat;
import entities.Produit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LotAchatDAO implements JoinDAOInterface<LotAchat, Produit> {

    @Override
    public List<LotAchat> listAll() {
        List<LotAchat> listLotAchats = new ArrayList<>();

        String query = "SELECT * from ams_lotachat";

        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int rsId = rs.getInt("idlotachat");
                int contratId = rs.getInt("idcontrat");
                double quantite = rs.getDouble("quantite");
                LocalDate dateAchat = rs.getDate("dateachat").toLocalDate();
                LocalDate datePeremption = rs.getDate("dateperemption").toLocalDate();

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
        String query = "INSERT INTO ams_lotachat (idcontrat, quantite, dateachat, dateperemption) VALUES " + entity.getValues();
        String queryID = "SELECT idlotachat FROM ams_lotachat WHERE idcontrat = " + entity.getContratId()
                + " AND quantite = " + entity.getQuantite()
                + " AND dateachat = '" + entity.getDateAchat() + "'"
                + " AND dateperemption = '" + entity.getDatePeremption() + "'";
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.executeQuery();
            stmt = conn.prepareStatement(queryID);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) entity.setId(rs.getInt("idlotachat"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectDatabase.closeConnection();
        }
    }

    @Override
    public void modifyEntity(LotAchat entity) {
		String query = "UPDATE ams_lotachat SET idcontrat = " + entity.getContratId()
                + ", quantite = " + entity.getQuantite()
                + ", dateachat = '" + entity.getDateAchat() + "'"
                + ", dateperemption = '" + entity.getDatePeremption() + "'"
                + " WHERE idlotachat = " + entity.getId();
        try{
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
    public LotAchat getById(int id) {
        String query = "SELECT * FROM ams_lotachat WHERE idlotachat = " + id;
        LotAchat lotAchat = null;
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // on part du principe que les identifiants sont uniques
                int rsId = rs.getInt("idlotachat");
                int contratId = rs.getInt("idcontrat");
                double quantite = rs.getDouble("quantite");
                LocalDate dateAchat = rs.getDate("dateachat").toLocalDate();
                LocalDate datePeremption = rs.getDate("dateperemption").toLocalDate();

                lotAchat = new LotAchat(rsId, contratId, quantite, dateAchat, datePeremption);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectDatabase.closeConnection();
        }
        return lotAchat;
    }

    @Override
    public void deleteEntity(LotAchat entity) {
		String query = "DELETE FROM ams_lotachat WHERE idlotachat = " + entity.getId();
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
    public List<LotAchat> listAllFromParameter(Produit joinEntity) {
        List<LotAchat> listlots = new ArrayList<>();

        String query = "SELECT * from ams_lotachat l join ams_contrat c on l.idlotachat = c.idcontrat where idproduit=" + joinEntity.getId();

        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // on part du principe que les identifiants sont uniques
                int rsId = rs.getInt("idlotachat");
                int contratId = rs.getInt("idcontrat");
                double quantite = rs.getDouble("quantite");
                LocalDate dateAchat = rs.getDate("dateachat").toLocalDate();
                LocalDate datePeremption = rs.getDate("dateperemption").toLocalDate();

                listlots.add(new LotAchat(rsId, contratId, quantite, dateAchat, datePeremption));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectDatabase.closeConnection();
        }
        return listlots;
    }
}
