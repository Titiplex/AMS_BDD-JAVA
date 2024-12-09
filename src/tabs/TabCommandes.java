package tabs;

import database.dataAccessObject.ContratDAO;
import database.dataAccessObject.FournisseurDAO;
import database.dataAccessObject.LotAchatDAO;
import database.dataAccessObject.ProduitDAO;
import entities.Contrat;
import entities.Fournisseur;
import entities.LotAchat;
import entities.Produit;
import exceptions.LotAchatQuantityException;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import tabs.popup.UpdateLotAchatPopup;
import tabs.tabUtilities.TabTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabCommandes implements TabTemplate {

    @Override
    public Node createTab() {
        VBox root = new VBox(20); // Conteneur principal vertical
        root.setStyle("-fx-padding: 10;");

        Label title = new Label("Liste des commandes du jour");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");
        Label subTitle = new Label("Les commandes pour le lendemain sont passées automatiquement à minuit");

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
        String[][] commandes = new String[lotAchatList.size()][6];
        LocalDate today = LocalDate.now();

        for (int i = 0; i < lotAchatList.size(); i++) {
            LotAchat lot = lotAchatList.get(i);
            if (lot.getDateAchat().isAfter(today)) {

                Contrat contrat = contratDAO.getById(lot.getContratId());
                Produit produit = produitDAO.getById(contrat.getIdProduit());
                Fournisseur fournisseur = fournisseurDAO.getById(contrat.getnumSiret());

                commandes[i][0] = lot.getDateAchat().toString();
                commandes[i][1] = produit.getNom();
                commandes[i][2] = "" + lot.getQuantite();
                commandes[i][3] = "" + (produit.getPrixVenteActuel() * lot.getQuantite());
                commandes[i][4] = fournisseur.getNomSociete();
                commandes[i][5] = "" + lot.getId();
            }
        }

        // on ajoute les commandes au container
        for (String[] commande : commandes) {
            HBox commandeRow = new HBox(10);
            commandeRow.setStyle("-fx-padding: 5; -fx-border-color: lightgray; -fx-border-width: 1;");

            VBox details = new VBox(5);
            details.getChildren().addAll(
                    new Label(commande[0]),
                    new Label(commande[1]),
                    new Label(commande[2]),
                    new Label(commande[3]),
                    new Label(commande[4])
            );

            // boutons d'actions
            Button btnSupprimer = new Button("Supprimer");
            Button btnModifier = new Button("Modifier");

            btnSupprimer.setOnAction(e -> supprimerCommande(lotAchatDAO.getById(Integer.parseInt(commande[5]))));
            btnModifier.setOnAction(e -> modifierCommande(lotAchatDAO.getById(Integer.parseInt(commande[5]))));

            VBox actions = new VBox(5);
            actions.getChildren().addAll(btnModifier, btnSupprimer);

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
                    try {
                        ajouterCommande(
                                finalTempoContrat,
                                Integer.parseInt(quantiteField.getText()),
                                finalTempoContrat.getQuantiteMin(),
                                dateBuying.getValue(),
                                datePeremption.getValue()
                        );
                    } catch (LotAchatQuantityException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            });
        });

        addCommandForm.getChildren().addAll(produitComboBox, contenu);

        root.getChildren().addAll(title, addCommandTitle, addCommandForm, subTitle, commandesContainer);

        return root;
    }

    private void supprimerCommande(LotAchat lot) {
        LotAchatDAO lotAchatDAO = new LotAchatDAO();
        lotAchatDAO.deleteEntity(lot);
        // TODO regen
    }

    private void modifierCommande(LotAchat lot) {
        new UpdateLotAchatPopup(lot);
    }

    private void ajouterCommande(Contrat contrat, double quantite, double min, LocalDate dateAchat, LocalDate datePeremption) throws LotAchatQuantityException {
        if (quantite < min) throw new LotAchatQuantityException(contrat, min);
        LotAchatDAO lotAchatDAO = new LotAchatDAO();
        lotAchatDAO.insertInTable(new LotAchat(1, contrat.getId(), quantite, dateAchat, datePeremption));
        // TODO regénérer la fenêtre pour update le contenu
    }
}
