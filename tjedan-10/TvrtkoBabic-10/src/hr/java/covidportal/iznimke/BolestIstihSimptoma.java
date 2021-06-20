package hr.java.covidportal.iznimke;

/**
 * Klasa za iznimku kada je unešena bolest ili virus sa prije unešenim simptomima
 */
public class BolestIstihSimptoma extends RuntimeException {

    public BolestIstihSimptoma(String message) {
        super(message);
    }

    public BolestIstihSimptoma(Throwable casue) {
        super(casue);
    }

    public BolestIstihSimptoma(String message, Throwable cause) {
        super(message, cause);
    }

}