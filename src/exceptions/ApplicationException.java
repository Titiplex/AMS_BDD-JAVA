package exceptions;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public abstract class ApplicationException extends RuntimeException {

    public ApplicationException(String message, Throwable cause) {
        super("Erreur : " + message, cause);
        Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur : \n" + message, ButtonType.OK);
        alert.showAndWait();
    }
}
