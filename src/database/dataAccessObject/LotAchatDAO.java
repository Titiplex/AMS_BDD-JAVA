package database.dataAccessObject;

import database.databaseUtilities.ConnectDatabase;
import database.databaseUtilities.JoinDAOInterface;
import entities.LotAchat;
import entities.Produit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static tabs.tabUtilities.TabUtilitiesMethodes.getRemainingLot;

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
                float quantite = rs.getFloat("quantite");
                LocalDate dateAchat = rs.getDate("dateachat").toLocalDate();
                LocalDate datePeremption = rs.getDate("dateperemption").toLocalDate();

                LotAchat lot = new LotAchat(rsId, contratId, quantite, dateAchat, datePeremption);
                if (getRemainingLot(lot) != 0) listLotAchats.add(lot);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listLotAchats;
    }

    @Override
    public void insertInTable(LotAchat entity) {
        String query = "INSERT INTO ams_lotachat (idcontrat, quantite, dateachat, dateperemption) VALUES " + entity.getValues() + " RETURNING idlotachat";
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) entity.setId(rs.getInt("idlotachat"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifyEntity(LotAchat entity) {
        String query = "UPDATE ams_lotachat SET idcontrat = " + entity.getContratId()
                + ", quantite = " + entity.getQuantite()
                + ", dateachat = '" + entity.getDateAchat() + "'"
                + ", dateperemption = '" + entity.getDatePeremption() + "'"
                + " WHERE idlotachat = " + entity.getId();
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
                float quantite = rs.getFloat("quantite");
                LocalDate dateAchat = rs.getDate("dateachat").toLocalDate();
                LocalDate datePeremption = rs.getDate("dateperemption").toLocalDate();

                lotAchat = new LotAchat(rsId, contratId, quantite, dateAchat, datePeremption);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lotAchat;
    }

    @Override
    public void deleteEntity(LotAchat entity) {
        String query = "DELETE FROM ams_lotachat WHERE idlotachat = " + entity.getId();
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<LotAchat> listAllFromParameter(Produit joinEntity) {
        List<LotAchat> listLots = new ArrayList<>();

        String query = "SELECT * from ams_lotachat l join ams_contrat c on l.idcontrat = c.idcontrat where idproduit=" + joinEntity.getId();

        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // on part du principe que les identifiants sont uniques
                int rsId = rs.getInt("idlotachat");
                int contratId = rs.getInt("idcontrat");
                Float quantite = rs.getFloat("quantite");
                LocalDate dateAchat = rs.getDate("dateachat").toLocalDate();
                LocalDate datePeremption = rs.getDate("dateperemption").toLocalDate();

                LotAchat lot = new LotAchat(rsId, contratId, quantite, dateAchat, datePeremption);
                if (getRemainingLot(lot) != 0) listLots.add(lot);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listLots;
    }

    @Override
    public void insertInTable(LotAchat entity, Produit joinEntity) {

    }
}
