package tabs.popup;

import database.dataAccessObject.ContratDAO;
import database.dataAccessObject.FournisseurDAO;
import database.dataAccessObject.ProduitDAO;
import entities.Contrat;
import entities.Fournisseur;
import entities.Produit;
import exceptions.EmptyFieldException;
import exceptions.entityAttributesExceptions.PastDateException;
import exceptions.entityAttributesExceptions.UnorderedDatesException;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import main.Main;

import java.time.LocalDate;
import java.util.List;

public class ContratCreationPopup {
    public ContratCreationPopup() throws EmptyFieldException, PastDateException, UnorderedDatesException {
        // fenetre modal
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
                return null;
            }

            @Override
            public String toString(Fournisseur arg0) {
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
                return null;
            }

            @Override
            public String toString(Produit arg0) {
                return (arg0 == null) ? "" : arg0.getNom();
            }

        });

        // enregistrer
        Button saveButton = new Button("Enregistrer");
        saveButton.setOnAction(saveEvent -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            String quantiteMin = quantiteMinField.getText();
            String prixFixe = prixFixeField.getText();
            int numSiret = fournisseurComboBox.getValue().getNumSiret();
            int idProduit = produitComboBox.getValue().getId();

            // exceptions
            if (quantiteMin.isEmpty() || prixFixe.isEmpty() || startDate == null || endDate == null || numSiret == 0 || idProduit == 0) {
                throw new EmptyFieldException("Creation contact");
            }

            if (startDate.isBefore(LocalDate.now()) || endDate.isBefore(LocalDate.now())) throw new PastDateException();

            if (startDate.isAfter(endDate)) throw new UnorderedDatesException();

            // Logique pour ajouter le contact à la base de données (ou la liste)
            Contrat newContrat = new Contrat(01, numSiret, idProduit, Integer.parseInt(quantiteMin), startDate, endDate, Float.parseFloat(prixFixe));

            ContratDAO contratDAO = new ContratDAO();
            contratDAO.insertInTable(newContrat);

            popupStage.close(); // Fermer la fenêtre popup
            Main.getInstance().recreateTab("Gestion");
        });

        // annuler
        Button cancelButton = new Button("Annuler");
        cancelButton.setOnAction(cancelEvent -> popupStage.close());

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setStyle("-fx-alignment: center;");

        popupRoot.getChildren().addAll(startDateLabel, startDatePicker, endDateLabel, endDatePicker, quantiteMinLabel, quantiteMinField, prixFixeLabel, prixFixeField, fournisseurComboBox, produitComboBox, buttonBox);

        // afficher
        Scene popupScene = new Scene(popupRoot, 300, 300);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
}
