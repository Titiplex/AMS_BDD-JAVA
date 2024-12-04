package tabs;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDate;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TabStock implements TabTemplate {

	@Override
	public Node createTab() {
	    VBox root = new VBox(20); // Conteneur principal vertical
	    root.setStyle("-fx-padding: 10;");

	    // Titre principal
	    Label title = new Label("Liste des lots en stock");
	    title.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

	    // Conteneur pour la liste des lots
	    VBox lotsContainer = new VBox(10);

	    // Exemple de lots (données fictives pour l'affichage)
	    String[][] lots = {
	        {"Produit A", "2024-12-01", "50"},
	        {"Produit A", "2024-11-25", "30"},
	        {"Produit B", "2024-11-28", "100"},
	        {"Produit B", "2024-12-10", "60"},
	        {"Produit C", "2024-11-20", "20"}
	    };

	    // Trier les lots par date d'échéance
	    Arrays.sort(lots, Comparator.comparing(lot -> LocalDate.parse(lot[1])));


	    // Grouper les lots par produit
	    Map<String, List<String[]>> groupedLots = Arrays.stream(lots)
	        .collect(Collectors.groupingBy(lot -> lot[0]));

	    // Afficher chaque produit avec ses lots
	    for (String produit : groupedLots.keySet()) {
	        VBox produitSection = new VBox(10);
	        produitSection.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-width: 1;");

	        Label produitTitle = new Label(produit);
	        produitTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

	        VBox lotsList = new VBox(5);
	        for (String[] lot : groupedLots.get(produit)) {
	            HBox lotRow = new HBox(20);

	            Label dateLabel = new Label("Date d'échéance : " + lot[1]);
	            Label quantityLabel = new Label("Quantité restante : " + lot[2]);

	            lotRow.getChildren().addAll(dateLabel, quantityLabel);
	            lotsList.getChildren().add(lotRow);
	        }

	        produitSection.getChildren().addAll(produitTitle, lotsList);
	        lotsContainer.getChildren().add(produitSection);
	    }

	    // Ajouter tout au conteneur principal
	    root.getChildren().addAll(title, lotsContainer);

	    return root;
	}
}
