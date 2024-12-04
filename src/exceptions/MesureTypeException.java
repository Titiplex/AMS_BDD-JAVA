package exceptions;

public class MesureTypeException extends Exception {

    /**Exception for when a product doesn't hold the required type of measurement.
     *
     */
    private static final long serialVersionUID = 1L;

    public MesureTypeException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public MesureTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

    public MesureTypeException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public MesureTypeException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public MesureTypeException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
}
