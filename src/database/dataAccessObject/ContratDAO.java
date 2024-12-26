package database.dataAccessObject;

import database.databaseUtilities.ConnectDatabase;
import database.databaseUtilities.DAOInterface;
import entities.Contrat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
                float quantiteMin = rs.getFloat("quantitemin");
                LocalDate dateDebut = rs.getDate("datedebut").toLocalDate();
                LocalDate dateFin = rs.getDate("datefin").toLocalDate();
                float prixFixe = rs.getFloat("prixfixe");

                listContrats.add(new Contrat(rsId, fournisseurId, idProduit, quantiteMin, dateDebut, dateFin, prixFixe));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listContrats;
    }

    @Override
    public void insertInTable(Contrat entity) {
        String query = "INSERT INTO ams_contrat (idfournisseur, idproduit, quantitemin, datedebut, datefin, prixfixe) VALUES " + entity.getValues() + "RETURNING idcontrat";
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) entity.setId(rs.getInt("idcontrat"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifyEntity(Contrat entity) {
        String query = "UPDATE ams_contrat SET idfournisseur = " + entity.getnumSiret()
                + ", idproduit = " + entity.getIdProduit()
                + ", quantitemin" + entity.getQuantiteMin()
                + ", datedebut = '" + entity.getDateDebut() + "'"
                + ", datefin = '" + entity.getDateFin() + "'"
                + ", prixfixe = " + entity.getPrixFixe()
                + " WHERE idcontrat = " + entity.getId();
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Contrat getById(int id) {
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
                float quantiteMin = rs.getFloat("quantitemin");
                LocalDate dateDebut = rs.getDate("datedebut").toLocalDate();
                LocalDate dateFin = rs.getDate("datefin").toLocalDate();
                float prixFixe = rs.getFloat("prixfixe");

                contrat = new Contrat(rsId, fournisseurId, idProduit, quantiteMin, dateDebut, dateFin, prixFixe);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contrat;
    }

    @Override
    public void deleteEntity(Contrat entity) {
        String query = "DELETE FROM ams_contrat WHERE idcontrat = " + entity.getId();
        try {
            Connection conn = ConnectDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
