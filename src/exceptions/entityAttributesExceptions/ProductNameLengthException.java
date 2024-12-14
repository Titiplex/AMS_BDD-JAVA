package exceptions.entityAttributesExceptions;

import exceptions.ApplicationException;

public class ProductNameLengthException extends ApplicationException {
    /**
     * Exception for when the name of an entity doesn't have the required amount of characters.
     */
    private static final long serialVersionUID = 1L;


    public ProductNameLengthException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }
}
