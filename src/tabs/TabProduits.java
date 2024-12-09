package tabs;

import database.dataAccessObject.CategorieDAO;
import database.dataAccessObject.ContratDAO;
import database.dataAccessObject.LotAchatDAO;
import database.dataAccessObject.ProduitDAO;
import entities.Categorie;
import entities.Contrat;
import entities.LotAchat;
import entities.Produit;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import tabs.tabUtilities.TabTemplate;

import java.util.Comparator;
import java.util.List;

import static tabs.tabUtilities.TabUtilitiesMethodes.getRemainingLot;

public class TabProduits implements TabTemplate {

    @Override
    public ScrollPane createTab() {
        VBox root = new VBox(20); // Conteneur principal vertical
        root.setStyle("-fx-padding: 10;");
        ProduitDAO produitDAO = new ProduitDAO();
        List<Produit> produitList = produitDAO.listAll();

        Label title = new Label("Liste complète des produits");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        // Conteneur pour la liste des produits
        VBox produitsContainer = new VBox(10);

        // string des produits
        String[][] produits = new String[produitList.size()][4];

        for (int i = 0; i < produitList.size(); i++) {
            Produit produit = produitList.get(i);
            produits[i][0] = "" + produit.getId();
            produits[i][1] = produit.getNom();
            produits[i][2] = "" + calculatePrixMoyen(produit.getId());
            produits[i][3] = "" + produit.getPrixVenteActuel();
        }

        // produit avec details liste
        for (String[] produit : produits) {
            HBox produitRow = new HBox(20);
            produitRow.setStyle("-fx-padding: 5; -fx-border-color: lightgray; -fx-border-width: 1;");

            // Informations sur le produit
            VBox details = new VBox(5);
            details.getChildren().addAll(
                    new Label("Nom : " + produit[1]),
                    new Label("Prix d'achat moyen : " + produit[2]),
                    new Label("Prix de vente : " + produit[3])
            );

            // Bouton pour afficher plus d'informations
            Button btnAfficher = new Button("Afficher");
            btnAfficher.setOnAction(e -> afficherDetailsProduit(Integer.parseInt(produit[0])));

            // Ajouter les détails et le bouton au HBox
            produitRow.getChildren().addAll(details, btnAfficher);
            produitsContainer.getChildren().add(produitRow);
        }

        // ajout produit
        Label addProductTitle = new Label("Ajouter un nouveau produit");
        addProductTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        HBox addProductForm = new HBox(10);
        TextField nom = new TextField();
        nom.setPromptText("Nom du produit");
        TextField description = new TextField();
        description.setPromptText("Description");
        TextField mesure = new TextField();
        mesure.setPromptText("Mesure (U ou kg)");
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
            Produit produit = new Produit(
                    // TODO récupérer l'id du produit après insertion
                    1,
                    Float.parseFloat(prixActuel.getText()),
                    nom.getText(),
                    description.getText(),
                    mesure.getText()
            );
            produitDAO.insertInTable(produit);
            categorieDAO.setTemporaryID(produit.getId());
            categorieDAO.insertInTable(categorieComboBox.getValue());
        });

        addProductForm.getChildren().addAll(nom, description, mesure, prixActuel, categorieComboBox, btnAjouter);

        // Ajouter toutes les sections au conteneur principal
        root.getChildren().addAll(title, addProductTitle, addProductForm, produitsContainer);

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
        alert.setContentText("Description : " + produit.getDescription() + "\nCatégorie : " + categorie.getCategorie());
        alert.showAndWait();
    }

    /**
     * Calculate the average price of a product (considering stock)
     *
     * @param produitId
     * @return
     */
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
}