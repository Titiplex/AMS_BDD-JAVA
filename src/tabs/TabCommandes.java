package tabs;

import database.dataAccessObject.ContratDAO;
import database.dataAccessObject.FournisseurDAO;
import database.dataAccessObject.LotAchatDAO;
import database.dataAccessObject.ProduitDAO;
import entities.Contrat;
import entities.Fournisseur;
import entities.LotAchat;
import entities.Produit;
import exceptions.EmptyFieldException;
import exceptions.entityAttributesExceptions.LotAchatQuantityException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import main.Main;
import tabs.popup.UpdateLotAchatPopup;
import tabs.tabUtilities.TabTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabCommandes implements TabTemplate {

    @Override
    public ScrollPane createTab() throws EmptyFieldException {
        VBox root = new VBox(20); // Conteneur principal vertical
        root.setStyle("-fx-padding: 10;");

        Label title = new Label("Liste des commandes du jour");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");
        Label subTitle = new Label("Les commandes pour le lendemain sont passées automatiquement à minuit" +
                "\nValidez pour passer une commande ultérieure directement aujourd'hui");

        LotAchatDAO lotAchatDAO = new LotAchatDAO();
        ProduitDAO produitDAO = new ProduitDAO();
        ContratDAO contratDAO = new ContratDAO();
        FournisseurDAO fournisseurDAO = new FournisseurDAO();
        List<LotAchat> lotAchatList = lotAchatDAO.listAll();
        LocalDate today = LocalDate.now();

        TableView<LotAchat> tableView = new TableView<>();

        TableColumn<LotAchat, String> dateAchatColumn = new TableColumn<>("Date d'achat");
        dateAchatColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateAchat().toString()));

        TableColumn<LotAchat, String> produitNomColumn = new TableColumn<>("Nom du produit");
        produitNomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                produitDAO.getById(
                                contratDAO.getById(cellData.getValue().getContratId())
                                        .getIdProduit())
                        .getNom()));

        TableColumn<LotAchat, Float> quantityColumn = new TableColumn<>("Quantité");
        quantityColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getQuantite()));

        TableColumn<LotAchat, Float> prixTotalColumn = new TableColumn<>("Prix total");
        prixTotalColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(
                produitDAO.getById(
                                contratDAO.getById(cellData.getValue().getContratId())
                                        .getIdProduit())
                        .getPrixVenteActuel() * cellData.getValue().getQuantite()));

        TableColumn<LotAchat, String> fournisseurColumn = new TableColumn<>("Fournisseur");
        fournisseurColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                fournisseurDAO.getById(contratDAO.getById(cellData.getValue().getContratId()).getnumSiret()).getNomSociete()
        ));

        TableColumn<LotAchat, Integer> idColumn = new TableColumn<>("ID du lot");
        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));

        TableColumn<LotAchat, Void> supprimerColumn = new TableColumn<>("Supprimer");
        supprimerColumn.setCellFactory(param -> new TableCell<>() {
            private final Button supprimerButton = new Button("Supprimer");

            {
                supprimerButton.setOnAction(event -> {
                    LotAchat lot = getTableView().getItems().get(getIndex());
                    supprimerCommande(lot);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(supprimerButton);
                }
            }
        });

        TableColumn<LotAchat, Void> modifierColumn = new TableColumn<>("Modifier");
        modifierColumn.setCellFactory(param -> new TableCell<>() {
            private final Button modifierButton = new Button("Modifier");

            {
                modifierButton.setOnAction(event -> {
                    LotAchat lot = getTableView().getItems().get(getIndex());
                    modifierCommande(lot);
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

        TableColumn<LotAchat, Void> validerColumn = new TableColumn<>("Valider");
        validerColumn.setCellFactory(param -> new TableCell<>() {
            private final Button validerButton = new Button("Valider");

            {
                validerButton.setOnAction(event -> {
                    LotAchat lot = getTableView().getItems().get(getIndex());
                    validerCommande(lot);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(validerButton);
                }
            }
        });

        tableView.getColumns().addAll(dateAchatColumn, produitNomColumn, quantityColumn, prixTotalColumn, fournisseurColumn, idColumn, supprimerColumn, modifierColumn, validerColumn);

        ObservableList<LotAchat> data = FXCollections.observableArrayList();

        for (LotAchat lot : lotAchatList) {
            if (lot.getDateAchat().isAfter(today)) {
                data.add(lot);
            }
        }

        tableView.setItems(data);

        // ajout commande
        Label addCommandTitle = new Label("Ajouter une nouvelle commande");
        addCommandTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        HBox addCommandForm = new HBox(10);
        HBox contenu = new HBox(10);

        List<Produit> produitList = produitDAO.listAll();
        ComboBox<Produit> produitComboBox = new ComboBox<>();
        ComboBox<Fournisseur> fournisseurComboBox = new ComboBox<>();
        produitComboBox.getItems().addAll(produitList);
        produitComboBox.setConverter(new StringConverter<>() {
            @Override
            public Produit fromString(String arg0) {
                return null;
            }

            @Override
            public String toString(Produit arg0) {
                return (arg0 == null) ? "" : arg0.getNom();
            }
        });

        produitComboBox.setOnAction(e -> {
            List<Contrat> contratList = contratDAO.listAll();
            Produit produit = produitComboBox.getValue();
            HashMap<Fournisseur, Contrat> mapContrats = new HashMap<>();

            for (Contrat contrat : contratList) {
                if (contrat.getIdProduit() == produit.getId()
                        && (contrat.getDateDebut().isBefore(today) || contrat.getDateDebut().isEqual(today))
                        && (contrat.getDateFin().isAfter(today) || contrat.getDateFin().isEqual(today))) {
                    Fournisseur localFournisseur = fournisseurDAO.getById(contrat.getnumSiret());
                    fournisseurComboBox.getItems().add(localFournisseur);
                    mapContrats.put(localFournisseur, contrat);
                }
            }

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
            contenu.getChildren().add(fournisseurComboBox);

            fournisseurComboBox.setOnAction(a -> {
                Contrat tempoContrat = null;
                for (Map.Entry<Fournisseur, Contrat> entry : mapContrats.entrySet()) {
                    if (entry.getKey().getNumSiret() == fournisseurComboBox.getValue().getNumSiret())
                        tempoContrat = entry.getValue();

                }
                TextField quantiteField = new TextField();
                quantiteField.setPromptText("Quantite (min " + tempoContrat.getQuantiteMin() + ")");
                DatePicker dateBuying = new DatePicker();
                dateBuying.setPromptText("Date achat");
                DatePicker datePeremption = new DatePicker();
                datePeremption.setPromptText("Date peremption");
                Button btnAjouter = new Button("Ajouter");

                contenu.getChildren().addAll(quantiteField, dateBuying, datePeremption, btnAjouter);

                Contrat finalTempoContrat = tempoContrat;
                btnAjouter.setOnAction(b -> {
                    if (quantiteField.getText().isEmpty() || dateBuying.getValue() == null || datePeremption.getValue() == null)
                        throw new EmptyFieldException("Creation Commande");
                    ajouterCommande(
                            finalTempoContrat,
                            Integer.parseInt(quantiteField.getText()),
                            finalTempoContrat.getQuantiteMin(),
                            dateBuying.getValue(),
                            datePeremption.getValue()
                    );

                });
            });
        });

        addCommandForm.getChildren().addAll(produitComboBox, contenu);

        root.getChildren().addAll(title, addCommandTitle, addCommandForm, subTitle, tableView);

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private void supprimerCommande(LotAchat lot) {
        LotAchatDAO lotAchatDAO = new LotAchatDAO();
        lotAchatDAO.deleteEntity(lot);
        Main.getInstance().recreateTab("Commandes");
    }

    private void modifierCommande(LotAchat lot) {
        new UpdateLotAchatPopup(lot);
    }

    private void ajouterCommande(Contrat contrat, float quantite, double min, LocalDate dateAchat, LocalDate datePeremption) throws LotAchatQuantityException {
        if (quantite < min) throw new LotAchatQuantityException(contrat, min);
        LotAchatDAO lotAchatDAO = new LotAchatDAO();
        lotAchatDAO.insertInTable(new LotAchat(1, contrat.getId(), quantite, dateAchat, datePeremption));
        Main.getInstance().recreateTab("Commandes");
    }

    private void validerCommande(LotAchat lot) {
        LotAchatDAO lotAchatDAO = new LotAchatDAO();
        lot.setDateAchat(LocalDate.now());
        lotAchatDAO.modifyEntity(lot);
        Main.getInstance().recreateTab("Commandes");
    }
}
