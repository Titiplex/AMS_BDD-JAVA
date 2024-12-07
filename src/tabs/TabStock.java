package tabs;

import database.dataAccessObject.ContratDAO;
import database.dataAccessObject.LotAchatDAO;
import database.dataAccessObject.ProduitDAO;
import database.dataAccessObject.VenteDAO;
import entities.LotAchat;
import entities.Vente;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class TabStock implements TabTemplate {
    List<Vente> listVentes;

    public TabStock() {
        VenteDAO venteDAO = new VenteDAO();
        listVentes = venteDAO.listAll();
    }

    @Override
    public Node createTab() {
        VBox root = new VBox(20);
        root.setStyle("-fx-padding: 10;");

        Label title = new Label("Liste des lots en stock");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        VBox lotsContainer = new VBox(10);
        LotAchatDAO lotAchatDAO = new LotAchatDAO();
        ProduitDAO produitDAO = new ProduitDAO();
        ContratDAO contratDAO = new ContratDAO();
        List<LotAchat> lotAchatList = lotAchatDAO.listAll();
        String[][] lots = new String[lotAchatList.size()][3];
        LocalDate today = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        for (int i = 0; i < lotAchatList.size(); i++) {

            LotAchat lot = lotAchatList.get(i);

            if ((lot.getDatePeremption().isAfter(today) || lot.getDatePeremption().equals(today))
                    && (lot.getDateAchat().isBefore(today) || lot.getDateAchat().equals(today))) {
                lots[i][0] = produitDAO.getById(contratDAO.getById(lot.getContratId()).getIdProduit()).getNom();
                lots[i][1] = lot.getDatePeremption().toString();
                lots[i][2] = "" + getRemainingLot(lot);
            }
        }

        // trier date écheance
        Arrays.sort(lots, Comparator.comparing(lot -> LocalDate.parse(lot[1])));

        // groupe par produit
        Map<String, List<String[]>> groupedLots = new HashMap<>();
        for (String[] row : lots) {
            if (groupedLots.containsKey(row[0])) {
                groupedLots.get(row[0]).add(row);
            } else {
                List<String[]> rows = new ArrayList<>();
                rows.add(row);
                groupedLots.put(row[0], rows);
            }
        }

        // créer les boxs par produit
        for (String produit : groupedLots.keySet()) {
            VBox produitSection = new VBox(10);
            produitSection.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-width: 1;");

            Label produitTitle = new Label(produit);
            produitTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

            HBox lotsList = new HBox(5);
			VBox dateList = new VBox(5);
			VBox quantityList = new VBox(5);
            Label dateHeader = new Label("Date d'échéance");
            Label quantityHeader = new Label("Quantité restante");
            dateList.getChildren().add(dateHeader);
			quantityList.getChildren().add(quantityHeader);

            for (String[] lot : groupedLots.get(produit)) {
                HBox lotDateRow = new HBox(20);
				HBox lotQuantityRow = new HBox(20);

                Label dateLabel = new Label(lot[1]);
                Label quantityLabel = new Label(lot[2]);

                lotDateRow.getChildren().add(dateLabel);
				lotQuantityRow.getChildren().add(quantityLabel);

				dateList.getChildren().add(lotDateRow);
				quantityList.getChildren().add(lotQuantityRow);
            }
			lotsList.getChildren().addAll(dateList, quantityList);

            produitSection.getChildren().addAll(produitTitle, lotsList);
            lotsContainer.getChildren().add(produitSection);
        }

        // Ajouter tout au conteneur principal
        root.getChildren().addAll(title, lotsContainer);

        return root;
    }

    private double getRemainingLot(LotAchat lot) {
        double totalNumber = lot.getQuantite();
        for (Vente vente : listVentes) {
            if (vente.getIdLotAchat() == lot.getId()) {
                totalNumber -= vente.getQuantity();
            }
        }
        return totalNumber;
    }
}
