package tabs;

import java.util.List;

import database.dataAccessObject.ContactDAO;
import database.dataAccessObject.FournisseurDAO;
import entities.Contact;
import entities.Fournisseur;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

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
				return arg0.getNomSociete();
			}
	    	
	    });
	    
	    VBox fournisseurInfoBox = new VBox(10);
	    fournisseurInfoBox.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-width: 1;");
	    Label fournisseurInfoTitle = new Label("Informations sur le fournisseur");
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

	    ListView<String> contactsListView = new ListView<>();
	    listContacts.forEach(contact -> {
	    	// A FAIRE ATTENTION, BOX AVEC LES INFOS EN POPUP ?
	    	contactsListView.getItems().add(contact.getNom() + " " + contact.getPrenom() + " - ");
	    });

	    Button addContactButton = new Button("Ajouter un contact");
	    addContactButton.setOnAction(e -> {
	        // Logique pour ajouter un contact
	        // Ouvrir un dialogue ou une fenêtre modale pour saisir un nouveau contact
	    });

	    contactsBox.getChildren().addAll(contactsTitle, contactsListView, addContactButton);

	    // Produits proposés
	    VBox produitsBox = new VBox(10);
	    produitsBox.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-width: 1;");
	    Label produitsTitle = new Label("Produits proposés");
	    produitsTitle.setStyle("-fx-font-weight: bold;");

	    ListView<String> produitsListView = new ListView<>();
	    produitsListView.getItems().addAll("Produit A", "Produit B", "Produit C");

	    produitsBox.getChildren().addAll(produitsTitle, produitsListView);

	    // Contrats passés
	    VBox contratsBox = new VBox(10);
	    contratsBox.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-width: 1;");
	    Label contratsTitle = new Label("Contrats passés");
	    contratsTitle.setStyle("-fx-font-weight: bold;");

	    ListView<String> contratsListView = new ListView<>();
	    contratsListView.getItems().addAll("Contrat 1", "Contrat 2", "Contrat 3");

	    Button addContratButton = new Button("Ajouter un contrat");
	    addContratButton.setOnAction(e -> {
	        // Logique pour ajouter un contrat
	        // Ouvrir un formulaire pour saisir les informations du contrat
	    });

	    contratsBox.getChildren().addAll(contratsTitle, contratsListView, addContratButton);

	    // Ajouter tout au conteneur principal
	    root.getChildren().addAll(title, fournisseurComboBox, fournisseurInfoBox, contactsBox, produitsBox, contratsBox);

	    return root;
	}


}
