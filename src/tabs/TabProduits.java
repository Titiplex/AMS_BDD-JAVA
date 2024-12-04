package tabs;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TabProduits implements TabTemplate {

	@Override
	public Node createTab() {
	    VBox root = new VBox(20); // Conteneur principal vertical
	    root.setStyle("-fx-padding: 10;");

	    // Titre principal
	    Label title = new Label("Liste complète des produits");
	    title.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

	    // Conteneur pour la liste des produits
	    VBox produitsContainer = new VBox(10);

	    // Exemple de données pour les produits
	    String[][] produits = {
	        {"Produit A", "50.00€", "67.00€", "100"},
	        {"Produit B", "30.00€", "40.20€", "50"},
	        {"Produit C", "20.00€", "26.80€", "200"}
	    };

	    // Ajouter chaque produit avec les détails
	    for (String[] produit : produits) {
	        HBox produitRow = new HBox(20);
	        produitRow.setStyle("-fx-padding: 5; -fx-border-color: lightgray; -fx-border-width: 1;");

	        // Informations sur le produit
	        VBox details = new VBox(5);
	        details.getChildren().addAll(
	            new Label("Nom : " + produit[0]),
	            new Label("Prix d'achat moyen : " + produit[1]),
	            new Label("Prix de vente : " + produit[2]),
	            new Label("Quantité disponible : " + produit[3])
	        );

	        // Bouton pour afficher plus d'informations
	        Button btnAfficher = new Button("Afficher");
	        btnAfficher.setOnAction(e -> afficherDetailsProduit(produit[0]));

	        // Ajouter les détails et le bouton au HBox
	        produitRow.getChildren().addAll(details, btnAfficher);
	        produitsContainer.getChildren().add(produitRow);
	    }

	    // Formulaire pour ajouter un produit
	    Label addProductTitle = new Label("Ajouter un nouveau produit");
	    addProductTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

	    HBox addProductForm = new HBox(10);
	    TextField nomField = new TextField();
	    nomField.setPromptText("Nom du produit");
	    TextField prixAchatField = new TextField();
	    prixAchatField.setPromptText("Prix d'achat");
	    TextField prixVenteField = new TextField();
	    prixVenteField.setPromptText("Prix de vente");
	    TextField quantiteField = new TextField();
	    quantiteField.setPromptText("Quantité");
	    Button btnAjouter = new Button("Ajouter");

	    // Gestionnaire d'événement pour le bouton d'ajout
	    btnAjouter.setOnAction(e -> ajouterProduit(
	        nomField.getText(),
	        prixAchatField.getText(),
	        prixVenteField.getText(),
	        quantiteField.getText()
	    ));

	    addProductForm.getChildren().addAll(nomField, prixAchatField, prixVenteField, quantiteField, btnAjouter);

	    // Ajouter toutes les sections au conteneur principal
	    root.getChildren().addAll(title, produitsContainer, addProductTitle, addProductForm);

	    return root;
	}

	// Méthodes d'exemple pour gérer les actions
	private void afficherDetailsProduit(String produitNom) {
	    // Exemple de données pour les descriptions et catégories
	    String description = "Ceci est une description détaillée du produit " + produitNom;
	    String categorie = "Catégorie de " + produitNom;

	    // Afficher les détails dans une boîte de dialogue
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle("Détails du produit");
	    alert.setHeaderText(produitNom);
	    alert.setContentText("Description : " + description + "\nCatégorie : " + categorie);
	    alert.showAndWait();
	}

	private void ajouterProduit(String nom, String prixAchat, String prixVente, String quantite) {
	    System.out.println("Produit ajouté : " + nom + " | Prix d'achat : " + prixAchat +
	            " | Prix de vente : " + prixVente + " | Quantité : " + quantite);
	}


}
