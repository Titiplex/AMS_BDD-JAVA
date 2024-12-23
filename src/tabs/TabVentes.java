package tabs;

import database.dataAccessObject.ContratDAO;
import database.dataAccessObject.LotAchatDAO;
import database.dataAccessObject.ProduitDAO;
import database.dataAccessObject.VenteDAO;
import entities.LotAchat;
import entities.Produit;
import entities.Vente;
import exceptions.EmptyFieldException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import main.Main;
import tabs.tabUtilities.TabTemplate;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class TabVentes implements TabTemplate {

    @Override
    public Node createTab() throws EmptyFieldException {
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
            if (quantiteField.getText().isEmpty() || produit == null) throw new EmptyFieldException("Creation Vente");

            List<LotAchat> listLotsProduit = lotAchatDAO.listAllFromParameter(produit);
            listLotsProduit.sort(Comparator.comparing(lot -> LocalDate.parse("" + lot.getDatePeremption())));

            createVente(listLotsProduit.getFirst(), produit, LocalDate.now(), Integer.parseInt(quantiteField.getText()));
        });

        formBox.getChildren().addAll(produitLabel, produitComboBox, quantiteLabel, quantiteField, addVenteButton);

        // liste ventes journée

        Label ventesTitle = new Label("Ventes de la journée");
        ventesTitle.setStyle("-fx-font-weight: bold;");

        TableView<Vente> tableView = new TableView<>();

        TableColumn<Vente, Integer> column1 = new TableColumn<>("ID Vente");
        column1.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));

        TableColumn<Vente, String> column2 = new TableColumn<>("Produit");
        column2.setCellValueFactory(cellData -> new SimpleStringProperty(
                produitDAO.getById(
                                contratDAO.getById(
                                        lotAchatDAO.getById(cellData.getValue().getIdLotAchat())
                                                .getContratId()).getIdProduit())
                        .getNom()));

        TableColumn<Vente, Float> column3 = new TableColumn<>("Quantité");
        column3.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getQuantity()));

        TableColumn<Vente, String> column4 = new TableColumn<>("Prix unitaire sur le moment");
        column4.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getPrixDuMoment())));

        tableView.getColumns().addAll(column1, column2, column3, column4);

        ObservableList<Vente> data = FXCollections.observableArrayList();

        venteList.sort(Comparator.comparing(vente -> LocalDate.parse("" + vente.getDateAchat())));
        for (Vente vente : venteList) {
            if (vente.getDateAchat().equals(LocalDate.now()))
                data.add(vente);
        }

        tableView.setItems(data);

        root.getChildren().addAll(title, formBox, ventesTitle, tableView);

        return root;
    }

    private void createVente(LotAchat lot, Produit produit, LocalDate date, int quantite) {
        VenteDAO venteDAO = new VenteDAO();
        venteDAO.insertInTable(new Vente(1, lot.getId(), produit.getPrixVenteActuel(), date, quantite));
        Main.getInstance().recreateTab("Ventes");
        Main.getInstance().recreateTab("Resultats");
        Main.getInstance().recreateTab("Stock");
    }
}
