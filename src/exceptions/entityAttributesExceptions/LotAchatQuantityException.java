package exceptions.entityAttributesExceptions;

import entities.Contrat;
import exceptions.ApplicationException;

public class LotAchatQuantityException extends EntityAttributeException {
    public LotAchatQuantityException(Contrat contrat, double quantity) {
        super("Quantité insuffisante pour achat du lot\nPar rapport au contrat " + contrat.getId() + ", min = " + quantity);
    }
}
