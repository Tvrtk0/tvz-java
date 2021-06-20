package hr.java.covidportal.model;

/**
 * Klasa koja definira podatke o županiji u kojoj se osoba nalazi.
 */

public class Zupanija extends ImenovaniEntitet {

    private Integer brStanovnika;

    /**
     * Inicijalizira objekt klase Zupanija
     * @param naziv predstavlja naziv županije
     * @param brStanovnika predstavlja broj stanovnika te županije
     */

    public Zupanija(String naziv, Integer brStanovnika) {
        super(naziv);
        this.brStanovnika = brStanovnika;
    }

    public Integer getBrStanovnika() {
        return brStanovnika;
    }

    public void setBrStanovnika(Integer brStanovnika) {
        this.brStanovnika = brStanovnika;
    }
}


