package tabs.popup;

import database.dataAccessObject.LotAchatDAO;
import entities.LotAchat;
import exceptions.EmptyFieldException;
import exceptions.entityAttributesExceptions.PastDateException;
import exceptions.entityAttributesExceptions.UnorderedDatesException;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;

import java.time.LocalDate;

public class UpdateLotAchatPopup {
    public UpdateLotAchatPopup(LotAchat lot) throws EmptyFieldException, PastDateException, UnorderedDatesException {
        // fenetre modal
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Modifier un lot");

        VBox popupRoot = new VBox(10);
        popupRoot.setStyle("-fx-padding: 15;");

        // champs
        Label quantityLabel = new Label("Quantite :");
        TextField quantityField = new TextField();
        quantityField.setText("" + lot.getQuantite());

        Label buyingDateLabel = new Label("Date Achat :");
        DatePicker buyingDatePicker = new DatePicker();
        buyingDatePicker.setValue(lot.getDateAchat());

        Label peremptionDateLabel = new Label("Date Péremption :");
        DatePicker peremptionDatePicker = new DatePicker();
        peremptionDatePicker.setValue(lot.getDatePeremption());

        // enregistrer
        Button saveButton = new Button("Enregistrer");
        saveButton.setOnAction(saveEvent -> {
            float quantity = Float.parseFloat(quantityField.getText());
            String quantityString = quantityField.getText();
            LocalDate buyingDate = buyingDatePicker.getValue();
            LocalDate peremptionDate = peremptionDatePicker.getValue();

            if (quantityString.isEmpty() || buyingDatePicker.getValue() == null || peremptionDatePicker.getValue() == null || quantity == 0) {
                throw new EmptyFieldException("Mis à jour lot");
            }

            if (buyingDate.isBefore(LocalDate.now()) || peremptionDate.isBefore(LocalDate.now())) throw new PastDateException();

            if (buyingDate.isAfter(peremptionDate)) throw new UnorderedDatesException();

            // ajouter
            lot.setDateAchat(buyingDate);
            lot.setDatePeremption(peremptionDate);
            lot.setQuantite(quantity);

            LotAchatDAO lotAchatDAO = new LotAchatDAO();
            lotAchatDAO.modifyEntity(lot);

            popupStage.close(); // Fermer la fenêtre popup
            Main.getInstance().recreateTab("Commandes");
        });

        // annuler
        Button cancelButton = new Button("Annuler");
        cancelButton.setOnAction(cancelEvent -> popupStage.close());

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setStyle("-fx-alignment: center;");

        popupRoot.getChildren().addAll(quantityLabel, quantityField, buyingDateLabel, buyingDatePicker, peremptionDateLabel, peremptionDatePicker, buttonBox);

        // afficher
        Scene popupScene = new Scene(popupRoot, 300, 300);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
}
