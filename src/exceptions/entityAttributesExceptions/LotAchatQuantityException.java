package exceptions.entityAttributesExceptions;

import entities.Contrat;
import exceptions.ApplicationException;

public class LotAchatQuantityException extends ApplicationException {
    public LotAchatQuantityException(Contrat contrat, double quantity) {
        super("Quantité insuffisante par rapport au contrat " + contrat.getId() + ", min = " + quantity);
        // TODO faire une alerte ou autre
    }
}
