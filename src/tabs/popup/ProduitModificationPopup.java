package tabs.popup;

import database.dataAccessObject.CategorieDAO;
import database.dataAccessObject.ProduitDAO;
import entities.Categorie;
import entities.Produit;
import exceptions.EmptyFieldException;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import main.Main;

import java.util.Comparator;
import java.util.List;

public class ProduitModificationPopup {

    public ProduitModificationPopup(Produit produit) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Modifier un produit");

        VBox popupRoot = new VBox(10);
        popupRoot.setStyle("-fx-padding: 15;");

        // fields
        Label nomLabel = new Label("Nom :");
        TextField nomField = new TextField(produit.getNom());

        Label descriptionLabel = new Label("Description :");
        TextField descriptionField = new TextField(produit.getDescription());

        Label mesureLabel = new Label("Mesure :");
        TextField mesureField = new TextField(produit.getMesure());

        Label prixVenteLabel = new Label("Prix de vente actuel :");
        TextField prixVenteField = new TextField(String.format("%.2f", produit.getPrixVenteActuel()));

        Label categorieLabel = new Label("Catégorie :");
        ComboBox<Categorie> categorieComboBox = new ComboBox<>();
        CategorieDAO categorieDAO = new CategorieDAO();
        List<Categorie> categorieList = categorieDAO.listAll();
        categorieList.sort(Comparator.comparing(Categorie::getCategorie));
        categorieComboBox.getItems().addAll(categorieList);
        categorieComboBox.setConverter(new StringConverter<>() {

            @Override
            public Categorie fromString(String arg0) {
                return null;
            }

            @Override
            public String toString(Categorie arg0) {
                return (arg0 == null) ? "" : arg0.getCategorie();
            }

        });
        categorieComboBox.getSelectionModel().select(categorieDAO.getById(produit.getId()));

        Button saveButton = new Button("Enregistrer");
        saveButton.setOnAction(saveEvent -> {
            // valider
            if (nomField.getText().isEmpty() || descriptionField.getText().isEmpty() ||
                    mesureField.getText().isEmpty() || prixVenteField.getText().isEmpty() ||
                    categorieComboBox.getValue() == null) {
                throw new EmptyFieldException("Modification Produit");
            }

            // maj produit
            produit.setNom(nomField.getText());
            produit.setDescription(descriptionField.getText());
            produit.setMesure(mesureField.getText());
            produit.setPrixVenteActuel(Float.parseFloat(prixVenteField.getText()));

            // save
            ProduitDAO produitDAO = new ProduitDAO();
            produitDAO.modifyEntity(produit);

            // maj
            categorieDAO.setTemporaryID(produit.getId());
            categorieDAO.modifyEntity(categorieComboBox.getValue());

            popupStage.close(); // Fermer la fenêtre popup
            Main.getInstance().recreateTab("Produits");
        });

        Button cancelButton = new Button("Annuler");
        cancelButton.setOnAction(cancelEvent -> popupStage.close());

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setStyle("-fx-alignment: center;");

        popupRoot.getChildren().addAll(
                nomLabel, nomField,
                descriptionLabel, descriptionField,
                mesureLabel, mesureField,
                prixVenteLabel, prixVenteField,
                categorieLabel, categorieComboBox,
                buttonBox
        );

        Scene popupScene = new Scene(popupRoot, 400, 400);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
}
