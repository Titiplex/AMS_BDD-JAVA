package exceptions.entityAttributesExceptions;

import exceptions.ApplicationException;

public class ProductNameLengthException extends EntityAttributeException {

    public ProductNameLengthException() {
        super("Les nom du produit dépasse les 15 caractères !");
    }
}
