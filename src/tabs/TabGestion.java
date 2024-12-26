package tabs;

import database.dataAccessObject.ContactDAO;
import database.dataAccessObject.ContratDAO;
import database.dataAccessObject.FournisseurDAO;
import database.dataAccessObject.ProduitDAO;
import entities.Contact;
import entities.Contrat;
import entities.Fournisseur;
import entities.Produit;
import exceptions.EmptyFieldException;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import main.Main;
import tabs.popup.*;
import tabs.tabUtilities.TabTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TabGestion implements TabTemplate {

    @Override
    public ScrollPane createTab() throws EmptyFieldException {
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
                return null;
            }

            @Override
            public String toString(Fournisseur arg0) {
                return (arg0 == null) ? "" : arg0.getNomSociete();
            }

        });

        VBox fournisseurInfoBox = new VBox(10);
        TableView<String[]> contactsTable = createTableList("Id", "Nom", "Fonction", "Email", "Téléphone", "contact");
        TableView<String[]> contratsTable = createTableList("Id", "Dates", "Nom Produit", "Prix fixe", "Quantité Min", "contrat");

        final VBox[] contratsBox = {new VBox(10)};
        final VBox[] contactsBox = {new VBox(10)};
        final VBox[] produitsBox = {new VBox(10)};
        final Button[] updateFournisseurButton = new Button[1];

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

            updateFournisseurButton[0] = new Button("Modifier");
            updateFournisseurButton[0].setOnAction(a -> new FournisseurModificationPopup(fournisseur));
            fournisseurInfoBox.getChildren().add(updateFournisseurButton[0]);

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
                contactsTable.getItems().add(new String[] {
                        contact.getId() + "",
                        contact.getNom() + ", " + contact.getPrenom(),
                        contact.getFonction(),
                        contact.geteMail(),
                        contact.getNumTel()
                });
            }

            contactsBox[0].getChildren().add(contactsTable);

            Button addContactButton = new Button("Ajouter un contact");
            addContactButton.setOnAction(a -> {
                // ouvrir popup pour ajouter contact
                new ContactCreationPopup(fournisseur);
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
                contratsTable.getItems().add(new String[] {
                        contrat.getId() + "",
                        contrat.getDateDebut().toString() + "/" + contrat.getDateFin().toString(),
                        produitDAO.getById(contrat.getIdProduit()).getNom(),
                        "" + contrat.getPrixFixe(),
                        "" + contrat.getQuantiteMin()
                });

                listProduits.add(produitDAO.getById(contrat.getIdProduit()));
            }

            contratsBox[0].getChildren().add(contratsTable);

            Button addContratButton = new Button("Ajouter un contrat");
            addContratButton.setOnAction(b -> {
                // popup creation contrat
                new ContratCreationPopup(fournisseur);
            });

            contratsBox[0].getChildren().add(addContratButton);

            TableView<String> produitTable = new TableView<>();
            TableColumn<String, String> produitName = new TableColumn<>("Nom");
            produitName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
            produitTable.getColumns().add(produitName);

            // on update la vbox des produits
            for (Produit produit : listProduits) {
                produitTable.getItems().add(produit.getNom());
            }

            produitsBox[0].getChildren().add(produitTable);

            root.getChildren().addAll(contactsBox[0], contratsBox[0], produitsBox[0]);
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
            if (numSiretField.getText().isEmpty() || nomSocieteField.getText().isEmpty() || adresseField.getText().isEmpty() || emailField.getText().isEmpty())
                throw new EmptyFieldException("Creation Fournisseur");
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
        Main.getInstance().recreateTab("Gestion");
    }

    private TableView<String[]> createTableList(String col1, String col2, String col3, String col4, String col5, String entity) {

        TableView<String[]> tableView = new TableView<>();

        TableColumn<String[], String> tableCol1 = new TableColumn<>(col1);
        tableCol1.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));

        TableColumn<String[], String> tableCol2 = new TableColumn<>(col2);
        tableCol2.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1]));

        TableColumn<String[], String> tableCol3 = new TableColumn<>(col3);
        tableCol3.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[2]));

        TableColumn<String[], String> tableCol4 = new TableColumn<>(col4);
        tableCol4.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[3]));

        TableColumn<String[], String> tableCol5 = new TableColumn<>(col5);
        tableCol5.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[4]));

        TableColumn<String[], Void> modifierColumn = new TableColumn<>("Modifier");
        modifierColumn.setCellFactory(param -> new TableCell<>() {
            private final Button modifierButton = new Button("Modifier");

            {
                modifierButton.setOnAction(event -> {
                    String[] liste = getTableView().getItems().get(getIndex());

                    if (Objects.equals(entity, "contact")) {
                        ContactDAO contactDAO = new ContactDAO();
                        new ContactModifPopup(contactDAO.getById(Integer.parseInt(liste[0])));
                    } else if (Objects.equals(entity, "contrat")) {
                        ContratDAO contratDAO = new ContratDAO();
                        new ContratModifPopup(contratDAO.getById(Integer.parseInt(liste[0])));
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(modifierButton);
                }
            }
        });

        tableView.getColumns().addAll(tableCol1, tableCol2, tableCol3, tableCol4, modifierColumn);

        return tableView;
    }
}
