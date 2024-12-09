package tabs;

import database.dataAccessObject.ContratDAO;
import database.dataAccessObject.LotAchatDAO;
import database.dataAccessObject.ProduitDAO;
import database.dataAccessObject.VenteDAO;
import entities.LotAchat;
import entities.Produit;
import entities.Vente;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import tabs.tabUtilities.TabTemplate;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class TabVentes implements TabTemplate {

    @Override
    public Node createTab() {
        // TODO
        VBox root = new VBox(20);
        root.setStyle("-fx-padding: 10;");

        Label title = new Label("Gestion des Ventes");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        ProduitDAO produitDAO = new ProduitDAO();
        VenteDAO venteDAO = new VenteDAO();
        LotAchatDAO lotAchatDAO = new LotAchatDAO();
        ContratDAO contratDAO = new ContratDAO();
        List<Produit> produitList = produitDAO.listAll();
        List<Vente> venteList = venteDAO.listAll();

        // ajouter vente
        HBox formBox = new HBox(20);
        formBox.setStyle("-fx-padding: 10;");

        // produit
        Label produitLabel = new Label("Produit:");
        ComboBox<Produit> produitComboBox = new ComboBox<>();
        for (Produit produit : produitList) produitComboBox.getItems().add(produit);
        produitComboBox.setConverter(new StringConverter<>() {

            @Override
            public Produit fromString(String arg0) {
                return null;
            }

            @Override
            public String toString(Produit arg0) {
                return (arg0 == null) ? "" : arg0.getNom();
            }

        });

        Label quantiteLabel = new Label("Quantité:");
        TextField quantiteField = new TextField();

        // boutton
        Button addVenteButton = new Button("Ajouter Vente");
        addVenteButton.setOnAction(e -> {

            Produit produit = produitComboBox.getValue();

            List<LotAchat> listLotsProduit = lotAchatDAO.listAllFromParameter(produit);
            listLotsProduit.sort(Comparator.comparing(lot -> LocalDate.parse("" + lot.getDatePeremption())));

            createVente(listLotsProduit.get(0), produit, LocalDate.now(), Integer.parseInt(quantiteField.getText()));
        });

        formBox.getChildren().addAll(produitLabel, produitComboBox, quantiteLabel, quantiteField, addVenteButton);

        // liste ventes journée
        VBox ventesBox = new VBox(10);
        ventesBox.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-width: 1;");
        Label ventesTitle = new Label("Ventes de la journée");
        ventesTitle.setStyle("-fx-font-weight: bold;");

        ListView<String> ventesListView = new ListView<>();
        ventesListView.getItems().add("ID Vente\t\t\tProduit\t\t\tQuantité\t\t\tPrix Moment");

        venteList.sort(Comparator.comparing(vente -> LocalDate.parse("" + vente.getDateAchat())));
        for (Vente vente : venteList) {
            if (vente.getDateAchat().equals(LocalDate.now()))
                ventesListView.getItems().addAll(vente.getId() + "\t\t\t\t"
                        + produitDAO.getById(contratDAO.getById(lotAchatDAO.getById(vente.getIdLotAchat()).getContratId()).getIdProduit()).getNom() + "\t\t\t"
                        + vente.getQuantity() + "\t\t\t"
                        + vente.getPrixDuMoment());
        }
        ventesBox.getChildren().addAll(ventesTitle, ventesListView);

        root.getChildren().addAll(title, formBox, ventesBox);

        return root;
    }

    private void createVente(LotAchat lot, Produit produit, LocalDate date, int quantite) {
        VenteDAO venteDAO = new VenteDAO();
        venteDAO.insertInTable(new Vente(1, lot.getId(), produit.getPrixVenteActuel(), date, quantite));
    }
}
