package exceptions.entityAttributesExceptions;

public class PastDateException extends EntityAttributeException {

    public PastDateException() {
        super("La date ne peut être antérieure à aujourd'hui !");
    }
}
