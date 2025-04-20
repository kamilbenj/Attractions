package dao;

/**
 * Exception spécifique aux erreurs d'accès aux données
 */
public class ExceptionDAO extends Exception {

    public ExceptionDAO(String message) {
        super(message);
    }

    public ExceptionDAO(String message, Throwable cause) {
        super(message, cause);
    }
}

