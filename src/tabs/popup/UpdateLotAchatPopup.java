package tabs.popup;

import database.dataAccessObject.LotAchatDAO;
import entities.LotAchat;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

public class UpdateLotAchatPopup {
    public UpdateLotAchatPopup(LotAchat lot) {
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
            int quantity = Integer.parseInt(quantityField.getText());
            String quantityString = quantityField.getText();
            LocalDate buyingDate = buyingDatePicker.getValue();
            LocalDate peremptionDate = peremptionDatePicker.getValue();

            if (quantityString.isEmpty() || buyingDatePicker.getValue() == null || peremptionDatePicker.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs.", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            // ajouter
            lot.setDateAchat(buyingDate);
            lot.setDatePeremption(peremptionDate);
            lot.setQuantite(quantity);

            LotAchatDAO lotAchatDAO = new LotAchatDAO();
            lotAchatDAO.modifyEntity(lot);

            popupStage.close(); // Fermer la fenêtre popup
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
