package exceptions;

import entities.Contrat;

public class LotAchatQuantityException extends Exception {
    public LotAchatQuantityException(Contrat contrat, double quantity) {
        super("Quantité insuffisante par rapport au contrat " + contrat.getId() + ", min = " + quantity);
        // TODO faire une alerte ou autre
    }
}
