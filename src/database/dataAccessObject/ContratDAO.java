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
import entities.Contrat;

public class ContratDAO implements DAOInterface<Contrat> {


    @Override
    public List<Contrat> listAll() {
        List<Contrat> listContrats = new ArrayList<>();

        String query = "SELECT * from ams_contrat";

        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int rsId = rs.getInt("idcontrat");
                int fournisseurId = rs.getInt("idfournisseur");
                int idProduit = rs.getInt("idproduit");
                int quantiteMin = rs.getInt("quantitemin");
                Date dateDebut = rs.getDate("datedebut");
                Date dateFin = rs.getDate("datefin");
                double prixFixe = rs.getDouble("prixfixe");

                listContrats.add(new Contrat(rsId, fournisseurId, idProduit, quantiteMin, dateDebut, dateFin, prixFixe));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectDatabase.closeConnection();
        }
        return listContrats;
    }

    @Override
    public void insertInTable(Contrat entity) {
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
    public void modifyEntity(Contrat entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public Contrat getById(int id) {
        // TODO Auto-generated method stub
        String query = "SELECT * FROM ams_contrat WHERE idcontrat = " + id;
        Contrat contrat = null;
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // on part du principe que les identifiants sont uniques
                int rsId = rs.getInt("idcontrat");
                int fournisseurId = rs.getInt("idfournisseur");
                int idProduit = rs.getInt("idproduit");
                int quantiteMin = rs.getInt("quantitemin");
                Date dateDebut = rs.getDate("datedebut");
                Date dateFin = rs.getDate("datefin");
                double prixFixe = rs.getDouble("prixfixe");

                contrat = new Contrat(rsId, fournisseurId, idProduit, quantiteMin, dateDebut, dateFin, prixFixe);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectDatabase.closeConnection();
        }
        return contrat;
    }

    @Override
    public void deleteEntity(Contrat entity) {
        // TODO Auto-generated method stub

    }

}
