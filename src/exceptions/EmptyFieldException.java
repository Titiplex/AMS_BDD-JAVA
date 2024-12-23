package exceptions;

public class EmptyFieldException extends ApplicationException {

    public EmptyFieldException(String contexte) {
        super("Veuillez attribuer une valeur au champ pour : \n\t" + contexte, new Throwable("Empty Field"));
    }
}
