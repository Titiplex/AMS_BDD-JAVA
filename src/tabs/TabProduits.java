package tabs;

import database.dataAccessObject.CategorieDAO;
import database.dataAccessObject.ContratDAO;
import database.dataAccessObject.LotAchatDAO;
import database.dataAccessObject.ProduitDAO;
import entities.Categorie;
import entities.Contrat;
import entities.LotAchat;
import entities.Produit;
import exceptions.EmptyFieldException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import main.Main;
import tabs.popup.ProduitModificationPopup;
import tabs.tabUtilities.TabTemplate;

import java.util.Comparator;
import java.util.List;

import static tabs.tabUtilities.TabUtilitiesMethodes.getRemainingLot;

public class TabProduits implements TabTemplate {

    @Override
    public ScrollPane createTab() throws EmptyFieldException {
        VBox root = new VBox(20); // Conteneur principal vertical
        root.setStyle("-fx-padding: 10;");
        ProduitDAO produitDAO = new ProduitDAO();
        List<Produit> produitList = produitDAO.listAll();

        Label title = new Label("Liste complète des produits");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        // Conteneur pour la liste des produits

        TableView<Produit> tableView = new TableView<>();

        TableColumn<Produit, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));

        TableColumn<Produit, String> nomColumn = new TableColumn<>("Nom");
        nomColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNom()));

        TableColumn<Produit, String> prixMoyenColumn = new TableColumn<>("Prix d'achat unitaire moyen");
        prixMoyenColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("%.2f", calculatePrixMoyen(data.getValue().getId())))
        );

        TableColumn<Produit, String> prixVenteColumn = new TableColumn<>("Prix de vente unitaire");
        prixVenteColumn.setCellValueFactory(data -> new SimpleStringProperty(String.format("%.2f", data.getValue().getPrixVenteActuel())));

        TableColumn<Produit, Void> afficherColumn = new TableColumn<>("Afficher le produit");
        afficherColumn.setCellFactory(param -> new TableCell<>() {
            private final Button afficherButton = new Button("Afficher");

            {
                afficherButton.setOnAction(event -> {
                    Produit produit = getTableView().getItems().get(getIndex());
                    afficherDetailsProduit(produit.getId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(afficherButton);
                }
            }
        });

        TableColumn<Produit, Void> modifierColumn = new TableColumn<>("Modifier");
        modifierColumn.setCellFactory(param -> new TableCell<>() {
            private final Button modifierButton = new Button("Modifier");

            {
                modifierButton.setOnAction(event -> {
                    new ProduitModificationPopup(getTableView().getItems().get(getIndex()));
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(modifierButton);
                }
            }
        });

        tableView.getColumns().addAll(idColumn, nomColumn, prixMoyenColumn, prixVenteColumn, afficherColumn, modifierColumn);
        tableView.getItems().addAll(produitList);

        // ajout produit
        Label addProductTitle = new Label("Ajouter un nouveau produit");
        addProductTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        HBox addProductForm = new HBox(10);
        TextField nom = new TextField();
        nom.setPromptText("Nom du produit");
        TextField description = new TextField();
        description.setPromptText("Description");
        TextField mesure = new TextField();
        mesure.setPromptText("Mesure (U / kg / L)");
        TextField prixActuel = new TextField();
        prixActuel.setPromptText("Prix de vente actuel");

        // les categories
        CategorieDAO categorieDAO = new CategorieDAO();
        List<Categorie> categorieList = categorieDAO.listAll();
        categorieList.sort(Comparator.comparing(Categorie::getCategorie));

        ComboBox<Categorie> categorieComboBox = new ComboBox<>();
        categorieComboBox.getItems().addAll(categorieList);
        categorieComboBox.setConverter(new StringConverter<>() {

            @Override
            public Categorie fromString(String arg0) {
                return null;
            }

            @Override
            public String toString(Categorie arg0) {
                return (arg0 == null) ? "" : arg0.getCategorie();
            }

        });

        Button btnAjouter = new Button("Ajouter");

        // on ajoute
        btnAjouter.setOnAction(e -> {
            if (nom.getText().isEmpty() || description.getText().isEmpty() || mesure.getText().isEmpty() || prixActuel.getText().isEmpty() || categorieComboBox.getValue() == null)
                throw new EmptyFieldException("Creation Produit");
            Produit produit = new Produit(
                    1,
                    Float.parseFloat(prixActuel.getText()),
                    nom.getText(),
                    description.getText(),
                    mesure.getText()
            );
            ajouterProduit(produit, categorieComboBox);
        });

        addProductForm.getChildren().addAll(nom, description, mesure, prixActuel, categorieComboBox, btnAjouter);

        // Ajouter toutes les sections au conteneur principal
        root.getChildren().addAll(title, addProductTitle, addProductForm, tableView);

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    /**
     * Alert to display details of a product
     *
     * @param id
     */
    private void afficherDetailsProduit(int id) {

        ProduitDAO produitDAO = new ProduitDAO();
        CategorieDAO categorieDAO = new CategorieDAO();
        Produit produit = produitDAO.getById(id);

        Categorie categorie = categorieDAO.getById(id);

        // Afficher les détails dans une boîte de dialogue
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails du produit");
        alert.setHeaderText(produit.getNom());
        alert.setContentText("Description : " + produit.getDescription() + "\nCatégorie : " + categorie.getCategorie() + "\nMesure : " + produit.getMesure());
        alert.showAndWait();
    }

    private double calculatePrixMoyen(int produitId) {
        LotAchatDAO lotAchatDAO = new LotAchatDAO();
        ContratDAO contratDAO = new ContratDAO();

        double numberOfProduct = 0, totalPriceOfProduct = 0;

        List<LotAchat> lotAchatList = lotAchatDAO.listAll();
        for (LotAchat lotAchat : lotAchatList) {
            Contrat contrat = contratDAO.getById(lotAchat.getContratId());
            if (contrat.getIdProduit() == produitId) {
                double remaining = getRemainingLot(lotAchat);
                numberOfProduct += remaining;
                ;
                totalPriceOfProduct += (remaining * contrat.getPrixFixe());
            }
        }
        if (numberOfProduct == 0) return 0;
        return totalPriceOfProduct / numberOfProduct;
    }

    private void ajouterProduit(Produit produit, ComboBox<Categorie> categorieComboBox) {
        ProduitDAO produitDAO = new ProduitDAO();
        CategorieDAO categorieDAO = new CategorieDAO();
        produitDAO.insertInTable(produit);
        categorieDAO.setTemporaryID(produit.getId());
        categorieDAO.insertInTable(categorieComboBox.getValue());
        Main.getInstance().recreateTab("Produits");
    }
}