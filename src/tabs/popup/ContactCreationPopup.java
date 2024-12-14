package tabs.popup;

import database.dataAccessObject.ContactDAO;
import entities.Contact;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;

public class ContactCreationPopup {
    public ContactCreationPopup() {
        // fenetre modal
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Ajouter un Contact");

        VBox popupRoot = new VBox(10);
        popupRoot.setStyle("-fx-padding: 15;");

        // champs
        Label nameLabel = new Label("Nom :");
        TextField nameField = new TextField();

        Label firstNameLabel = new Label("Prénom :");
        TextField firstNameField = new TextField();

        Label emailLabel = new Label("Email :");
        TextField emailField = new TextField();

        Label phoneLabel = new Label("Téléphone :");
        TextField phoneField = new TextField();

        Label fonctionLabel = new Label("Fonction :");
        TextField fonctionField = new TextField();

        // enregistrer
        Button saveButton = new Button("Enregistrer");
        saveButton.setOnAction(saveEvent -> {
            String name = nameField.getText();
            String firstName = firstNameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String fonction = fonctionField.getText();

            if (name.isEmpty() || firstName.isEmpty() || email.isEmpty() || phone.isEmpty() || fonction.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs.", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            // ajouter
            Contact newContact = new Contact(01, name, firstName, fonction, phone, email);

            ContactDAO contactDAO = new ContactDAO();
            contactDAO.insertInTable(newContact);

            popupStage.close(); // Fermer la fenêtre popup
            Main.getInstance().recreateTab("Gestion");
        });

        // annuler
        Button cancelButton = new Button("Annuler");
        cancelButton.setOnAction(cancelEvent -> popupStage.close());

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setStyle("-fx-alignment: center;");

        popupRoot.getChildren().addAll(nameLabel, nameField, firstNameLabel, firstNameField, fonctionLabel, fonctionField, emailLabel, emailField, phoneLabel, phoneField, buttonBox);

        // afficher
        Scene popupScene = new Scene(popupRoot, 300, 300);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
}
