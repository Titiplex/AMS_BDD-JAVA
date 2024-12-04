package tabs;

import java.time.LocalDate;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TabCommandes implements TabTemplate {

    @Override
    public Node createTab() {
        VBox root = new VBox(20); // Conteneur principal vertical
        root.setStyle("-fx-padding: 10;");

        // Titre de la section
        Label title = new Label("Liste des commandes du jour");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        // Conteneur pour la liste des commandes
        VBox commandesContainer = new VBox(10);

        // Exemple de commandes
        String[][] commandes = {
                {"Produit A", "Quantité : 10", "Fournisseur : X"},
                {"Produit B", "Quantité : 5", "Fournisseur : Y"},
                {"Produit C", "Quantité : 20", "Fournisseur : Z"}
        };

        // Ajouter chaque commande avec ses actions
        for (String[] commande : commandes) {
            HBox commandeRow = new HBox(10);
            commandeRow.setStyle("-fx-padding: 5; -fx-border-color: lightgray; -fx-border-width: 1;");

            // Infos
            VBox details = new VBox(5);
            details.getChildren().addAll(
                    new Label(commande[0]), // Produit
                    new Label(commande[1]), // Quantité
                    new Label(commande[2])  // Fournisseur
            );

            // Boutons
            Button btnValider = new Button("Valider");
            Button btnSupprimer = new Button("Supprimer");
            Button btnReporter = new Button("Reporter");
            Button btnModifier = new Button("Modifier");

            btnValider.setOnAction(e -> validerCommande(commande[0]));
            btnSupprimer.setOnAction(e -> supprimerCommande(commande[0]));
            btnReporter.setOnAction(e -> reporterCommande(commande[0]));
            btnModifier.setOnAction(e -> modifierCommande(commande[0]));

            VBox actions = new VBox(5);
            actions.getChildren().addAll(btnValider, btnSupprimer, btnReporter, btnModifier);

            // Ajouter les détails et les actions à la ligne de commande
            commandeRow.getChildren().addAll(details, actions);
            commandesContainer.getChildren().add(commandeRow);
        }

        // Formulaire pour ajouter une nouvelle commande
        Label addCommandTitle = new Label("Ajouter une nouvelle commande");
        addCommandTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        HBox addCommandForm = new HBox(10);
        TextField produitField = new TextField();
        produitField.setPromptText("Produit");
        TextField fournisseurField = new TextField();
        fournisseurField.setPromptText("Fournisseur");
        TextField quantiteField = new TextField();
        quantiteField.setPromptText("Quantité");
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Date");
        Button btnAjouter = new Button("Ajouter");

        // Gestionnaire d'événement pour le bouton d'ajout
        btnAjouter.setOnAction(e -> ajouterCommande(
                produitField.getText(),
                fournisseurField.getText(),
                quantiteField.getText(),
                datePicker.getValue()
        ));

        addCommandForm.getChildren().addAll(produitField, fournisseurField, quantiteField, datePicker, btnAjouter);

        // Ajouter toutes les sections au conteneur principal
        root.getChildren().addAll(title, commandesContainer, addCommandTitle, addCommandForm);

        return root;
    }

    // Méthodes d'exemple pour gérer les actions
    private void validerCommande(String produit) {
        System.out.println("Commande validée : " + produit);
    }

    private void supprimerCommande(String produit) {
        System.out.println("Commande supprimée : " + produit);
    }

    private void reporterCommande(String produit) {
        System.out.println("Commande reportée : " + produit);
    }

    private void modifierCommande(String produit) {
        System.out.println("Modification de la commande : " + produit);
    }

    private void ajouterCommande(String produit, String fournisseur, String quantite, LocalDate date) {
        System.out.println("Nouvelle commande ajoutée : " + produit + " | " + fournisseur + " | " + quantite + " | " + date);
    }


}
