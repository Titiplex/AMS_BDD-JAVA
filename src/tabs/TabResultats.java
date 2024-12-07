package tabs;

import database.dataAccessObject.ContratDAO;
import database.dataAccessObject.LotAchatDAO;
import database.dataAccessObject.ProduitDAO;
import database.dataAccessObject.VenteDAO;
import entities.Contrat;
import entities.LotAchat;
import entities.Produit;
import entities.Vente;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tabs.tabUtilities.TabTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class TabResultats implements TabTemplate {

    @Override
    public Node createTab() {

        HBox root = new HBox(20); // conteneur principal
        root.setStyle("-fx-padding: 10;");

        // on récupère les ventes
        VenteDAO VenteDao = new VenteDAO();
        List<Vente> listVentes = VenteDao.listAll();

        // Création du menu déroulant pour choisir le critère de tri
        ComboBox<String> sortComboBox = new ComboBox<>();
        sortComboBox.getItems().addAll("Trier par Quantité", "Trier par Bénéfice");

        // Action pour trier
        sortComboBox.setOnAction(e -> {
            String selectedOption = sortComboBox.getValue();
            if ("Trier par Quantité".equals(selectedOption)) {
                listVentes.sort(Comparator.comparingInt(Vente::getQuantity).reversed());
            } else if ("Trier par Bénéfice".equals(selectedOption)) {
				listVentes.sort((vente1, vente2) -> {
					double benefice1 = getBenefice(vente1, new ContratDAO().getById(new LotAchatDAO().getById(vente1.getIdLotAchat()).getContratId()));
					double benefice2 = getBenefice(vente2, new ContratDAO().getById(new LotAchatDAO().getById(vente2.getIdLotAchat()).getContratId()));
					return Double.compare(benefice2, benefice1); // Trie décroissant
				});
			}
			root.getChildren().removeIf(node -> node != sortComboBox);
            root.getChildren().add(maj(listVentes));
        });

        root.getChildren().addAll(sortComboBox);

        return root;
    }

    private Node maj(List<Vente> listVentes) {
		HBox root = new HBox(20);
		root.setStyle("-fx-padding: 10;");

        // on créé une liste de produits en string
        String[][] topDaySalesString = new String[10][3];
        String[][] topMonthSalesString = new String[10][3];

        double totalDayBenefices = 0, totalMonthBenefices = 0, totalDaySales = 0, totalMonthSales = 0;

        // on trie et on passe les ventes sous forme de string, en jour mois et total
        int itJour = 0, itMois = 0;
        for (int i = 0; i < listVentes.size(); i++) {
            Vente tempo = listVentes.get(i);
            LotAchat tempoLot = new LotAchatDAO().getById(tempo.getIdLotAchat());
            Contrat tempoContrat = new ContratDAO().getById(tempoLot.getContratId());
            Produit tempoProduit = new ProduitDAO().getById(tempoContrat.getIdProduit());

			LocalDate dateAchat = tempo.getDateAchat();
            LocalDate today = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            double benefice = getBenefice(tempo, tempoContrat);

            if (dateAchat.equals(today) && itJour < 10) {
                topDaySalesString[i][0] = tempoProduit.getNom();
                topDaySalesString[i][1] = tempo.getQuantity() + " unités";
                topDaySalesString[i][2] = benefice + "€";
                itJour++;
                totalDayBenefices += benefice;
                totalDaySales += (tempo.getQuantity() * tempo.getPrixDuMoment());
            }
            if (dateAchat.getMonth() == today.getMonth() && dateAchat.getYear() == today.getYear() && itMois < 10) {
                topMonthSalesString[i][0] = tempoProduit.getNom();
                topMonthSalesString[i][1] = tempo.getQuantity() + " unités";
                topMonthSalesString[i][2] = benefice + "€";
                itMois++;
                totalMonthBenefices += benefice;
                totalMonthSales += (tempo.getQuantity() * tempo.getPrixDuMoment());
            }
        }
        VBox dayTopSalesList = createTopSalesList(topDaySalesString);
        VBox monthTopSalesList = createTopSalesList(topMonthSalesString);

        // jour
        VBox dayColumn = new VBox(10);
        Label dayTitle = new Label("Données du jour");
        dayTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        Label dayTotalSales = new Label("Total des ventes : " + totalDaySales);
        Label dayTotalBenef = new Label("Bénéfices : " + totalDayBenefices);
        Label dayTopSalesLabel = new Label("Top 10 ventes (Jour)");
        dayTopSalesLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        dayColumn.getChildren().addAll(dayTitle, dayTotalSales, dayTotalBenef, dayTopSalesLabel, dayTopSalesList);

        // mois
        VBox monthColumn = new VBox(10);
        Label monthTitle = new Label("Données du mois");
        monthTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        Label monthTotalSales = new Label("Total des ventes : " + totalMonthSales);
        Label monthTotalBenef = new Label("Bénéfices : " + totalMonthBenefices);
        Label monthTopSalesLabel = new Label("Top 10 ventes (Mois)");
        monthTopSalesLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        monthColumn.getChildren().addAll(monthTitle, monthTotalSales, monthTotalBenef, monthTopSalesLabel, monthTopSalesList);

        // Ajouter les deux colonnes dans le conteneur principal
        root.getChildren().addAll(dayColumn, monthColumn);
		return root;
    }

    private VBox createTopSalesList(String[][] salesData) {
        VBox listContainer = new VBox(5);
        for (String[] sale : salesData) {
            HBox row = new HBox(20);
            row.getChildren().addAll(
                    new Label(sale[0]), // nom
                    new Label(sale[1]), // qtté
                    new Label(sale[2])  // benef
            );
            listContainer.getChildren().add(row);
        }
        return listContainer;
    }

    private double getBenefice(Vente vente, Contrat contrat) {
        return (vente.getQuantity() * (vente.getPrixDuMoment() - contrat.getPrixFixe()));
    }
}
