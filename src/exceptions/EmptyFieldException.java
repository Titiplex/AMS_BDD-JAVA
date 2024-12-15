package exceptions;

public class EmptyFieldException extends ApplicationException {

    public EmptyFieldException(String contexte) {
        super("Veuillez attribuer une valeur au champ pour : " + contexte, new Throwable("Empty Field"));
    }
}
