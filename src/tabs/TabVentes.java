package tabs;

import java.time.LocalDate;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tabs.tabUtilities.TabTemplate;

public class TabVentes implements TabTemplate {

	@Override
	public Node createTab() {
		// TODO
	    VBox root = new VBox(20); // Conteneur principal vertical
	    root.setStyle("-fx-padding: 10;");

	    // Titre principal
	    Label title = new Label("Gestion des Ventes");
	    title.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

	    // Formulaire pour ajouter une vente
	    HBox formBox = new HBox(20);
	    formBox.setStyle("-fx-padding: 10;");

	    Label produitLabel = new Label("Produit:");
	    ComboBox<String> produitComboBox = new ComboBox<>();
	    produitComboBox.getItems().addAll("Produit A", "Produit B", "Produit C"); // Liste des produits à récupérer dynamiquement

	    Label quantiteLabel = new Label("Quantité:");
	    TextField quantiteField = new TextField();

	    Label lotLabel = new Label("Lot d'origine:");
	    ComboBox<String> lotComboBox = new ComboBox<>();
	    lotComboBox.getItems().addAll("Lot 1", "Lot 2", "Lot 3"); // Liste des lots à récupérer dynamiquement
	    
	 // Liste des ventes de la journée
	    VBox ventesBox = new VBox(10);
	    ventesBox.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-width: 1;");
	    Label ventesTitle = new Label("Ventes de la journée");
	    ventesTitle.setStyle("-fx-font-weight: bold;");

	    ListView<String> ventesListView = new ListView<>();
	    ventesBox.getChildren().addAll(ventesTitle, ventesListView);

	    // boutton
	    Button addVenteButton = new Button("Ajouter Vente");
	    addVenteButton.setOnAction(e -> {
	        // Récupérer les valeurs du formulaire
	        String produit = produitComboBox.getValue();
	        int quantite = Integer.parseInt(quantiteField.getText());
	        String lot = lotComboBox.getValue();
	        double prixEffectif = getPrixEffectif(produit); // Exemple de méthode pour récupérer le prix effectif en cours
	        LocalDate dateVente = LocalDate.now();

	        // Ajouter la vente à la liste des ventes
	        ventesListView.getItems().add("Produit: " + produit + ", Quantité: " + quantite + ", Lot: " + lot + ", Prix: " + prixEffectif + ", Date: " + dateVente);
	    });

	    formBox.getChildren().addAll(produitLabel, produitComboBox, quantiteLabel, quantiteField, lotLabel, lotComboBox, addVenteButton);

	    // Ajouter tout au conteneur principal
	    root.getChildren().addAll(title, formBox, ventesBox);

	    return root;
	}

	// Méthode fictive pour obtenir le prix effectif d'un produit
	private double getPrixEffectif(String produit) {
	    // Exemple simple : selon le produit, retourner un prix fixe
	    switch (produit) {
	        case "Produit A":
	            return 100.0;
	        case "Produit B":
	            return 150.0;
	        case "Produit C":
	            return 200.0;
	        default:
	            return 0.0;
	    }
	}


}
