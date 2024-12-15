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

        VBox commandesContainer = new VBox(10);
        HBox labels = new HBox(10);
        labels.getChildren().addAll(
                new Label("Date d'achat"),
                new Label("Nom du produit"),
                new Label("Quantité"),
                new Label("Prix total"),
                new Label("Fournisseur")
        );

        LotAchatDAO lotAchatDAO = new LotAchatDAO();
        ProduitDAO produitDAO = new ProduitDAO();
        ContratDAO contratDAO = new ContratDAO();
        FournisseurDAO fournisseurDAO = new FournisseurDAO();
        List<LotAchat> lotAchatList = lotAchatDAO.listAll();
        LocalDate today = LocalDate.now();

        List<String[]> commandes = new ArrayList<>(); // Utilisez une liste dynamique

        for (LotAchat lot : lotAchatList) {
            if (lot.getDateAchat().isAfter(today)) {
                Contrat contrat = contratDAO.getById(lot.getContratId());
                Produit produit = produitDAO.getById(contrat.getIdProduit());
                Fournisseur fournisseur = fournisseurDAO.getById(contrat.getnumSiret());

                commandes.add(new String[]{
                        lot.getDateAchat().toString(),
                        produit.getNom(),
                        "" + lot.getQuantite(),
                        "" + (produit.getPrixVenteActuel() * lot.getQuantite()),
                        fournisseur.getNomSociete(),
                        "" + lot.getId()
                });
            }
        }


        // on ajoute les commandes au container
        for (String[] commande : commandes) {
            HBox commandeRow = new HBox(10);
            commandeRow.setStyle("-fx-padding: 5; -fx-border-color: lightgray; -fx-border-width: 1;");

            VBox details = new VBox(5);
            details.getChildren().addAll(
                    new Label(commande[0] != null ? commande[0] : ""),
                    new Label(commande[1] != null ? commande[1] : ""),
                    new Label(commande[2] != null ? commande[2] : ""),
                    new Label(commande[3] != null ? commande[3] : ""),
                    new Label(commande[4] != null ? commande[4] : "")
            );

            // boutons d'actions
            Button btnSupprimer = new Button("Supprimer");
            Button btnModifier = new Button("Modifier");
            Button btnValider = new Button("Valider");

            btnSupprimer.setOnAction(e -> supprimerCommande(lotAchatDAO.getById(Integer.parseInt(commande[5]))));
            btnModifier.setOnAction(e -> modifierCommande(lotAchatDAO.getById(Integer.parseInt(commande[5]))));
            btnValider.setOnAction(e -> validerCommande(lotAchatDAO.getById(Integer.parseInt(commande[5]))));

            VBox actions = new VBox(5);
            actions.getChildren().addAll(btnModifier, btnSupprimer, btnValider);

            // ajout final des lignes de commande
            commandeRow.getChildren().addAll(details, actions);
            commandesContainer.getChildren().add(commandeRow);
        }

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

        root.getChildren().addAll(title, addCommandTitle, addCommandForm, subTitle, commandesContainer);

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
