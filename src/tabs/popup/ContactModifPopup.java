package tabs.popup;

import database.dataAccessObject.ContactDAO;
import entities.Contact;
import entities.Fournisseur;
import exceptions.EmptyFieldException;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;

public class ContactModifPopup {
    public ContactModifPopup(Contact contact) throws EmptyFieldException {
        // fenetre modal
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Modifier un Contact");

        VBox popupRoot = new VBox(10);
        popupRoot.setStyle("-fx-padding: 15;");

        // champs
        Label nameLabel = new Label("Nom :");
        TextField nameField = new TextField(contact.getNom());

        Label firstNameLabel = new Label("Prénom :");
        TextField firstNameField = new TextField(contact.getPrenom());

        Label emailLabel = new Label("Email :");
        TextField emailField = new TextField(contact.geteMail());

        Label phoneLabel = new Label("Téléphone :");
        TextField phoneField = new TextField(contact.getNumTel());

        Label fonctionLabel = new Label("Fonction :");
        TextField fonctionField = new TextField(contact.getFonction());

        // enregistrer
        Button saveButton = new Button("Enregistrer");
        saveButton.setOnAction(saveEvent -> {
            String name = nameField.getText();
            String firstName = firstNameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String fonction = fonctionField.getText();

            if (name.isEmpty() || firstName.isEmpty() || email.isEmpty() || phone.isEmpty() || fonction.isEmpty()) {
                throw new EmptyFieldException("Creation contact");
            }

            // ajouter
            Contact newContact = new Contact(contact.getId(), name, firstName, fonction, phone, email);

            ContactDAO contactDAO = new ContactDAO();
            contactDAO.modifyEntity(newContact);

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
        Scene popupScene = new Scene(popupRoot, 500, 500);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
}
