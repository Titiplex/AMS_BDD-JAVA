package tabs;

import database.dataAccessObject.ContactDAO;
import database.dataAccessObject.ContratDAO;
import database.dataAccessObject.FournisseurDAO;
import database.dataAccessObject.ProduitDAO;
import entities.Contact;
import entities.Contrat;
import entities.Fournisseur;
import entities.Produit;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import tabs.popup.ContactCreationPopup;
import tabs.popup.ContratCreationPopup;
import tabs.tabUtilities.TabTemplate;

import java.util.ArrayList;
import java.util.List;

public class TabGestion implements TabTemplate {

    @Override
    public ScrollPane createTab() {
        VBox root = new VBox(20); // Conteneur principal vertical
        root.setStyle("-fx-padding: 10;");
        Label title = new Label("Gestion des Fournisseurs");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

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

        VBox fournisseurInfoBox = new VBox(10);
        final VBox[] contactsBox = {new VBox(10)};
        final VBox[] contratsBox = {new VBox(10)};
        final VBox[] produitsBox = {new VBox(10)};
        fournisseurInfoBox.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-width: 1;");
        Label fournisseurInfoTitle = new Label("Information sur le fournisseur");
        fournisseurInfoTitle.setStyle("-fx-font-weight: bold;");

        // on recup les données dynamiquement pour les fournisseurs
        Label fournisseurName = new Label();
        Label fournisseurSiret = new Label();
        Label fournisseurAdresse = new Label();
        Label fournisseurEmail = new Label();

        fournisseurComboBox.setOnAction(e -> {
            Fournisseur fournisseur = fournisseurComboBox.getValue();
            fournisseurName.setText("Nom de la société : " + fournisseur.getNomSociete());
            fournisseurSiret.setText("Numéro SIRET : " + fournisseur.getNumSiret());
            fournisseurAdresse.setText("Adresse : " + fournisseur.getAdresse());
            fournisseurEmail.setText("Email : " + fournisseur.geteMailPrincipal());

            // Contacts
            contactsBox[0] = new VBox(10);
            contactsBox[0].setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-width: 1;");
            Label contactsTitle = new Label("Contacts associés");
            contactsTitle.setStyle("-fx-font-weight: bold;");

            ContactDAO contactDAO = new ContactDAO();
            List<Contact> listContacts = contactDAO.listAllFromParameter(fournisseur);

            contactsBox[0].getChildren().add(contactsTitle);
            for (Contact contact : listContacts) {
                // A FAIRE ATTENTION, BOX AVEC LES INFOS EN POPUP ?
                HBox hBox = new HBox(20);
                hBox.getChildren().add(new Label(contact.getNom()));
                contactsBox[0].getChildren().add(hBox);
            }

            Button addContactButton = new Button("Ajouter un contact");
            addContactButton.setOnAction(a -> {
                // ouvrir popup pour ajouter contact
                new ContactCreationPopup();
            });

            contactsBox[0].getChildren().add(addContactButton);

            // Produits proposés
            produitsBox[0] = new VBox(10);
            produitsBox[0].setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-width: 1;");
            Label produitsTitle = new Label("Produits proposés");
            produitsTitle.setStyle("-fx-font-weight: bold;");

            ProduitDAO produitDAO = new ProduitDAO();
            List<Produit> listProduits = new ArrayList<>();

            produitsBox[0].getChildren().add(produitsTitle);

            // On recup les contrats passés, serviront à update les produits du fournisseur
            contratsBox[0] = new VBox(10);
            contratsBox[0].setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-width: 1;");
            Label contratsTitle = new Label("Contrats passés");
            contratsTitle.setStyle("-fx-font-weight: bold;");

            ContratDAO contratDAO = new ContratDAO();
            List<Contrat> contratList = contratDAO.listAll();

            contratsBox[0].getChildren().add(contratsTitle);
            for (Contrat contrat : contratList) {
                HBox hBox = new HBox(20);
                hBox.getChildren().addAll(new Label(contrat.getDateDebut().toString()),
                        new Label(contrat.getDateFin().toString()),
                        new Label(produitDAO.getById(contrat.getIdProduit()).getNom()),
                        new Label("" + contrat.getPrixFixe()),
                        new Label("" + contrat.getQuantiteMin()));

                contratsBox[0].getChildren().add(hBox);
                listProduits.add(produitDAO.getById(contrat.getIdProduit()));
            }

            Button addContratButton = new Button("Ajouter un contrat");
            addContratButton.setOnAction(b -> {
                // popup creation contrat
                new ContratCreationPopup();
            });

            contratsBox[0].getChildren().add(addContratButton);

            // on update la vbox des produits
            for (Produit produit : listProduits) {
                HBox hBox = new HBox(20);
                hBox.getChildren().addAll(new Label(produit.getNom()));
                produitsBox[0].getChildren().add(hBox);
            }

            root.getChildren().addAll(contactsBox[0], produitsBox[0], contratsBox[0]);
        });

        fournisseurInfoBox.getChildren().addAll(fournisseurInfoTitle, fournisseurName, fournisseurSiret, fournisseurAdresse, fournisseurEmail);

		// ajouter fournisseur
		HBox addFournisseurBox = new HBox(20);

		Label numSiretLabel = new Label("Numéro de Siret:");
		TextField numSiretField = new TextField();
		Label nomSocieteLabel = new Label("Nom:");
		TextField nomSocieteField = new TextField();
		Label adresseLabel = new Label("Adresse:");
		TextField adresseField = new TextField();
		Label emailLabel = new Label("E-mail:");
		TextField emailField = new TextField();

		Button addFournisseurButton = new Button("Ajouter");
		addFournisseurButton.setOnAction(a -> {
			createFournisseur(
					Integer.parseInt(numSiretField.getText()),
					nomSocieteField.getText(),
					adresseField.getText(),
					emailField.getText()
			);
		});

		addFournisseurBox.getChildren().addAll(numSiretLabel, numSiretField, nomSocieteLabel, nomSocieteField, adresseLabel, adresseField, emailLabel, emailField, addFournisseurButton);

        root.getChildren().addAll(title, addFournisseurBox, fournisseurComboBox, fournisseurInfoBox);

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

	private void createFournisseur(int numSiret, String nomSociete, String adresse, String email) {
		FournisseurDAO fournisseurDAO = new FournisseurDAO();
		fournisseurDAO.insertInTable(new Fournisseur(
				nomSociete,
				numSiret,
				adresse,
				email
		));
	}
}
