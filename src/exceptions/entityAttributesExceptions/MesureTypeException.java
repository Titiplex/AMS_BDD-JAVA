package exceptions.entityAttributesExceptions;

import exceptions.ApplicationException;

public class MesureTypeException extends EntityAttributeException {

    public MesureTypeException() {
        super("Le type de mesure est invalide !\nSeuls kg, U et L sont valides");
    }
}
