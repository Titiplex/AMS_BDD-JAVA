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

public class ContratModifPopup {
    public ContratModifPopup(Contrat contrat) throws EmptyFieldException, PastDateException, UnorderedDatesException {
        // fenetre modal
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Ajouter un Contact");

        // Conteneur principal
        VBox popupRoot = new VBox(10);
        popupRoot.setStyle("-fx-padding: 15;");

        // Champs de saisie pour le contact
        Label startDateLabel = new Label("Date de début :");
        DatePicker startDatePicker = new DatePicker(contrat.getDateDebut());

        Label endDateLabel = new Label("Date de fin :");
        DatePicker endDatePicker = new DatePicker(contrat.getDateFin());

        Label quantiteMinLabel = new Label("Quantité minimale :");
        TextField quantiteMinField = new TextField(contrat.getQuantiteMin() + "");

        Label prixFixeLabel = new Label("Prix fixe :");
        TextField prixFixeField = new TextField(contrat.getPrixFixe() + "");

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
            int idProduit = produitComboBox.getValue().getId();

            // exceptions
            if (quantiteMin.isEmpty() || prixFixe.isEmpty() || startDate == null || endDate == null || idProduit == 0) {
                throw new EmptyFieldException("Creation contact");
            }

            if (startDate.isBefore(LocalDate.now()) || endDate.isBefore(LocalDate.now())) throw new PastDateException();

            if (startDate.isAfter(endDate)) throw new UnorderedDatesException();

            // Logique pour ajouter le contact à la base de données (ou la liste)
            Contrat newContrat = new Contrat(contrat.getId(), contrat.getnumSiret(), idProduit, Integer.parseInt(quantiteMin), startDate, endDate, Float.parseFloat(prixFixe));

            ContratDAO contratDAO = new ContratDAO();
            contratDAO.modifyEntity(newContrat);

            popupStage.close(); // Fermer la fenêtre popup
            Main.getInstance().recreateTab("Gestion");
        });

        // annuler
        Button cancelButton = new Button("Annuler");
        cancelButton.setOnAction(cancelEvent -> popupStage.close());

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setStyle("-fx-alignment: center;");

        popupRoot.getChildren().addAll(startDateLabel, startDatePicker, endDateLabel, endDatePicker, quantiteMinLabel, quantiteMinField, prixFixeLabel, prixFixeField, produitComboBox, buttonBox);

        // afficher
        Scene popupScene = new Scene(popupRoot, 500, 500);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
}
