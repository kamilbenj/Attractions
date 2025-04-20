package controleur;

/**
 * Exception levée par le contrôleur
 */
class ControleurException extends Exception {

    public ControleurException(String message) {
        super(message);
    }

    public ControleurException(String message, Throwable cause) {
        super(message, cause);
    }
}