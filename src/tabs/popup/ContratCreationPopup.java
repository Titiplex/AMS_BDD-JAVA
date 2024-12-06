package tabs.popup;

import database.dataAccessObject.ContactDAO;
import database.dataAccessObject.ContratDAO;
import database.dataAccessObject.FournisseurDAO;
import database.dataAccessObject.ProduitDAO;
import entities.Contact;
import entities.Contrat;
import entities.Fournisseur;
import entities.Produit;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.Date;
import java.util.List;

public class ContratCreationPopup {
    public ContratCreationPopup() {
        // Créer une fenêtre modale pour ajouter un contrat
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Ajouter un Contact");

        // Conteneur principal
        VBox popupRoot = new VBox(10);
        popupRoot.setStyle("-fx-padding: 15;");

        // Champs de saisie pour le contact
        Label startDateLabel = new Label("Date de début :");
        DatePicker startDatePicker = new DatePicker();

        Label endDateLabel = new Label("Date de fin :");
        DatePicker endDatePicker = new DatePicker();

        Label quantiteMinLabel = new Label("Quantité minimale :");
        TextField quantiteMinField = new TextField();

        Label prixFixeLabel = new Label("Prix fixe :");
        TextField prixFixeField = new TextField();

        // on recup les fournisseurs
        FournisseurDAO fournisseurDao = new FournisseurDAO();
        List<Fournisseur> listFournisseurs = fournisseurDao.listAll();

        // on sélectionne dans une liste déroulante
        ComboBox<Fournisseur> fournisseurComboBox = new ComboBox<>();
        fournisseurComboBox.getItems().addAll(listFournisseurs);
        fournisseurComboBox.setConverter(new StringConverter<>() {

            @Override
            public Fournisseur fromString(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String toString(Fournisseur arg0) {
                // TODO Auto-generated method stub
                return (arg0 == null) ? "" : arg0.getNomSociete();
            }

        });

        // on recup les produits
        ProduitDAO produitDao = new ProduitDAO();
        List<Produit> listProduits = produitDao.listAll();

        // on sélectionne dans une liste déroulante
        ComboBox<Produit> produitComboBox = new ComboBox<>();
        produitComboBox.getItems().addAll(listProduits);
        produitComboBox.setConverter(new StringConverter<>() {

            @Override
            public Produit fromString(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String toString(Produit arg0) {
                // TODO Auto-generated method stub
                return (arg0 == null) ? "" : arg0.getNom();
            }

        });

        // Bouton pour enregistrer
        Button saveButton = new Button("Enregistrer");
        saveButton.setOnAction(saveEvent -> {
            Date startDate = new Date(startDatePicker.getValue().toString());
            Date endDate = new Date(endDatePicker.getValue().toString());
            String quantiteMin = quantiteMinField.getText();
            String prixFixe = prixFixeField.getText();
            int numSiret = fournisseurComboBox.getValue().getNumSiret();
            int idProduit = produitComboBox.getValue().getId();

            if (quantiteMin.isEmpty() || prixFixe.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs.", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            // Logique pour ajouter le contact à la base de données (ou la liste)
            Contrat newContrat = new Contrat(01, numSiret, idProduit, Integer.parseInt(quantiteMin), startDate, endDate, Integer.parseInt(prixFixe));

            ContratDAO contratDAO = new ContratDAO();
            contratDAO.insertInTable(newContrat);

            popupStage.close(); // Fermer la fenêtre popup
        });

        // Bouton pour annuler
        Button cancelButton = new Button("Annuler");
        cancelButton.setOnAction(cancelEvent -> popupStage.close());

        // Layout pour les boutons
        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setStyle("-fx-alignment: center;");

        // Ajouter les éléments au conteneur principal
        popupRoot.getChildren().addAll(startDateLabel, startDatePicker, endDateLabel, endDatePicker, quantiteMinLabel, quantiteMinField, prixFixeLabel, prixFixeField, fournisseurComboBox, produitComboBox, buttonBox);

        // Afficher la fenêtre popup
        Scene popupScene = new Scene(popupRoot, 300, 300);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
}
