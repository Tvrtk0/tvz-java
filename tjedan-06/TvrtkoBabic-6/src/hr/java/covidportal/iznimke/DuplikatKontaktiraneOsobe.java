package hr.java.covidportal.iznimke;

/**
 * Klasa za iznimku kada se za kontaktiranu osobu više puta unese ista osoba
 */

public class DuplikatKontaktiraneOsobe extends Exception {

    public DuplikatKontaktiraneOsobe(String message) {
        super(message);
    }

    public DuplikatKontaktiraneOsobe(Throwable casue) {
        super(casue);
    }

    public DuplikatKontaktiraneOsobe(String message, Throwable cause) {
        super(message, cause);
    }

}
