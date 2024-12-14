package exceptions.entityAttributesExceptions;

import exceptions.ApplicationException;

public class MesureTypeException extends ApplicationException {

    /**
     * Exception for when a product doesn't hold the required type of measurement.
     */
    private static final long serialVersionUID = 1L;

    public MesureTypeException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }
}
