package tabs;

import java.util.List;

import database.dataAccessObject.ContactDAO;
import database.dataAccessObject.ContratDAO;
import database.dataAccessObject.FournisseurDAO;
import database.dataAccessObject.ProduitDAO;
import entities.Contact;
import entities.Contrat;
import entities.Fournisseur;
import entities.Produit;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import tabs.popup.ContactCreationPopup;
import tabs.popup.ContratCreationPopup;

public class TabGestion implements TabTemplate {

	@Override
	public Node createTab() {
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
	    });

	    fournisseurInfoBox.getChildren().addAll(fournisseurInfoTitle, fournisseurName, fournisseurSiret, fournisseurAdresse, fournisseurEmail);

	    // Contacts
	    VBox contactsBox = new VBox(10);
	    contactsBox.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-width: 1;");
	    Label contactsTitle = new Label("Contacts associés");
	    contactsTitle.setStyle("-fx-font-weight: bold;");
	    
	    ContactDAO contactDAO = new ContactDAO();
	    List<Contact> listContacts = contactDAO.listAll();

	    contactsBox.getChildren().add(contactsTitle);
	    listContacts.forEach(contact -> {
	    	// A FAIRE ATTENTION, BOX AVEC LES INFOS EN POPUP ?
			HBox hBox = new HBox(20);
			hBox.getChildren().add(new Label(contact.getValues()));
			contactsBox.getChildren().add(hBox);
	    });

	    Button addContactButton = new Button("Ajouter un contact");
	    addContactButton.setOnAction(e -> {
	        // ouvrir popup pour ajouter contact
			ContactCreationPopup contactPopup = new ContactCreationPopup();
	    });

	    contactsBox.getChildren().add(addContactButton);

	    // Produits proposés
	    VBox produitsBox = new VBox(10);
	    produitsBox.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-width: 1;");
	    Label produitsTitle = new Label("Produits proposés");
	    produitsTitle.setStyle("-fx-font-weight: bold;");

		ProduitDAO produitDAO = new ProduitDAO();
		List<Produit> listProduits = produitDAO.listAll();

	    produitsBox.getChildren().add(produitsTitle);

		for(Produit produit : listProduits) {
			HBox hBox = new HBox(20);
			hBox.getChildren().addAll(new Label(produit.getNom()), new Label(produit.getCategorie()));
			produitsBox.getChildren().add(hBox);
		}

	    // Contrats passés
	    VBox contratsBox = new VBox(10);
	    contratsBox.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-width: 1;");
	    Label contratsTitle = new Label("Contrats passés");
	    contratsTitle.setStyle("-fx-font-weight: bold;");

		ContratDAO contratDAO = new ContratDAO();
	    List<Contrat> contratList = contratDAO.listAll();

		contratsBox.getChildren().add(contratsTitle);
		for(Contrat contrat : contratList) {
			HBox hBox = new HBox(20);
			hBox.getChildren().addAll(new Label(contrat.getDateDebut().toString()),
					new Label(contrat.getDateFin().toString()),
					new Label(produitDAO.getById(contrat.getIdProduit()).getNom()),
					new Label("" + contrat.getPrixFixe()),
					new Label("" + contrat.getQuantiteMin()));

			contratsBox.getChildren().add(hBox);
		}

	    Button addContratButton = new Button("Ajouter un contrat");
	    addContratButton.setOnAction(e -> {
	        // popup creation contrat
			ContratCreationPopup contratPopup = new ContratCreationPopup();
	    });

	    contratsBox.getChildren().add(addContratButton);

	    // Ajouter tout au conteneur principal
	    root.getChildren().addAll(title, fournisseurComboBox, fournisseurInfoBox, contactsBox, produitsBox, contratsBox);

	    return root;
	}


}
