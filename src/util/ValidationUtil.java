package util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Classe utilitaire contenant des fonctions de validation
 */
public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    /**
     * Vérifier si une chaîne est vide ou nulle
     * @param str La chaîne à vérifier
     * @return true si la chaîne est vide ou nulle, false sinon
     */
    public static boolean estVide(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Valider qu'une chaîne n'est pas vide
     * @param str La chaîne à valider
     * @param nomChamp Le nom du champ pour le message d'erreur
     * @throws ValidationException si la chaîne est vide
     */
    public static void validerNonVide(String str, String nomChamp) throws ValidationException {
        if (estVide(str)) {
            throw new ValidationException("Le champ " + nomChamp + " ne peut pas être vide");
        }
    }

    /**
     * Valider une longueur minimale pour une chaîne
     * @param str La chaîne à valider
     * @param longueurMin La longueur minimale requise
     * @param nomChamp Le nom du champ pour le message d'erreur
     * @throws ValidationException si la chaîne ne respecte pas la longueur minimale
     */
    public static void validerLongueurMin(String str, int longueurMin, String nomChamp) throws ValidationException {
        validerNonVide(str, nomChamp);
        if (str.length() < longueurMin) {
            throw new ValidationException("Le champ " + nomChamp + " doit contenir au moins " + longueurMin + " caractères");
        }
    }

    /**
     * Valider une adresse email
     * @param email L'email à valider
     * @throws ValidationException si l'email n'est pas valide
     */
    public static void validerEmail(String email) throws ValidationException {
        validerNonVide(email, "email");
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("L'adresse email n'est pas valide");
        }
    }

    /**
     * Valider un montant positif
     * @param montant Le montant à valider
     * @param nomChamp Le nom du champ pour le message d'erreur
     * @throws ValidationException si le montant n'est pas positif
     */
    public static void validerMontantPositif(BigDecimal montant, String nomChamp) throws ValidationException {
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Le " + nomChamp + " doit être supérieur à zéro");
        }
    }

    /**
     * Valider une valeur entière positive
     * @param valeur La valeur à valider
     * @param nomChamp Le nom du champ pour le message d'erreur
     * @throws ValidationException si la valeur n'est pas positive
     */
    public static void validerEntierPositif(int valeur, String nomChamp) throws ValidationException {
        if (valeur <= 0) {
            throw new ValidationException("Le champ " + nomChamp + " doit être supérieur à zéro");
        }
    }

    /**
     * Valider qu'une date est dans le futur
     * @param date La date à valider
     * @param nomChamp Le nom du champ pour le message d'erreur
     * @throws ValidationException si la date n'est pas dans le futur
     */
    public static void validerDateFuture(Date date, String nomChamp) throws ValidationException {
        if (date == null || date.before(new Date())) {
            throw new ValidationException("La " + nomChamp + " doit être une date future");
        }
    }
}

/**
 * Exception levée lors d'une erreur de validation
 */

