package exceptions.entityAttributesExceptions;

import exceptions.ApplicationException;

public abstract class EntityAttributeException extends ApplicationException {
    public EntityAttributeException(String message) {
        super(message);
    }
}
