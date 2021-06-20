package hr.java.covidportal.iznimke;

/**
 * Klasa za iznimku kada broj kontaktiranih osoba nije ispravan
 */

public class BrojKontaktiranihOsoba extends Exception{
    public BrojKontaktiranihOsoba(String message) {
        super(message);
    }

    public BrojKontaktiranihOsoba(Throwable casue) {
        super(casue);
    }

    public BrojKontaktiranihOsoba(String message, Throwable cause) {
        super(message, cause);
    }
}
