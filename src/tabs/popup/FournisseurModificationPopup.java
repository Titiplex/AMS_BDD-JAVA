package tabs.popup;

import database.dataAccessObject.FournisseurDAO;
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

public class FournisseurModificationPopup {

    public FournisseurModificationPopup(Fournisseur fournisseur) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Modifier un fournisseur");

        VBox popupRoot = new VBox(10);
        popupRoot.setStyle("-fx-padding: 15;");

        // champs
        Label siretLabel = new Label("Numéro de SIRET :");
        Label siretField = new Label(String.valueOf(fournisseur.getNumSiret()));

        Label nomLabel = new Label("Nom de la société :");
        TextField nomField = new TextField(fournisseur.getNomSociete());

        Label adresseLabel = new Label("Adresse :");
        TextField adresseField = new TextField(fournisseur.getAdresse());

        Label emailLabel = new Label("E-mail principal :");
        TextField emailField = new TextField(fournisseur.geteMailPrincipal());

        // Bouton pour enregistrer les modifications
        Button saveButton = new Button("Enregistrer");
        saveButton.setOnAction(saveEvent -> {

            // Validation des champs
            if (nomField.getText().isEmpty() || adresseField.getText().isEmpty() || emailField.getText().isEmpty()) {
                throw new EmptyFieldException("Modification Fournisseur");
            }

            // Mise à jour de l'objet fournisseur
            fournisseur.setNomSociete(nomField.getText());
            fournisseur.setAdresse(adresseField.getText());
            fournisseur.seteMailPrincipal(emailField.getText());

            // Mise à jour dans la base de données
            FournisseurDAO fournisseurDAO = new FournisseurDAO();
            fournisseurDAO.modifyEntity(fournisseur);

            // Fermer la fenêtre et rafraîchir l'onglet
            popupStage.close();
            Main.getInstance().recreateTab("Gestion");

        });

        // Bouton pour annuler
        Button cancelButton = new Button("Annuler");
        cancelButton.setOnAction(cancelEvent -> popupStage.close());

        // Conteneur pour les boutons
        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setStyle("-fx-alignment: center;");

        // Ajouter les champs et les boutons à la fenêtre
        popupRoot.getChildren().addAll(siretLabel, siretField, nomLabel, nomField, adresseLabel, adresseField, emailLabel, emailField, buttonBox);

        // Afficher la fenêtre
        Scene popupScene = new Scene(popupRoot, 400, 300);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
}
