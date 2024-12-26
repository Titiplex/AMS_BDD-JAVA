package tabs.tabUtilities;

import database.dataAccessObject.VenteDAO;
import entities.LotAchat;
import entities.Vente;

import java.util.List;

public class TabUtilitiesMethodes {
    private static List<Vente> listVentes;

    /**
     * Get the number of remaining products in a LotAchat
     *
     * @param lot
     * @return
     */
    public static double getRemainingLot(LotAchat lot) {

        VenteDAO venteDAO = new VenteDAO();
        listVentes = venteDAO.listAll();

        double totalNumber = lot.getQuantite();
        for (Vente vente : listVentes) {
            if (vente.getIdLotAchat() == lot.getId()) {
                totalNumber -= vente.getQuantity();
            }
        }
        return totalNumber;
    }
}
