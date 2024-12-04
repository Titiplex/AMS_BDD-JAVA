package exceptions;

public class ProductNameLengthException extends Exception {
    /**Exception for when the name of an entity doesn't have the required amount of characters.
     *
     */
    private static final long serialVersionUID = 1L;

    public ProductNameLengthException() {
        // TODO Auto-generated constructor stub
    }

    public ProductNameLengthException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public ProductNameLengthException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    public ProductNameLengthException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public ProductNameLengthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }
}
