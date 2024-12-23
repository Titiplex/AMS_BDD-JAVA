package exceptions.entityAttributesExceptions;

public class UnorderedDatesException extends EntityAttributeException {
    public UnorderedDatesException() {

        super("Les dates d'achat et de péremption sont dans le mauvais ordre !");
    }
}
