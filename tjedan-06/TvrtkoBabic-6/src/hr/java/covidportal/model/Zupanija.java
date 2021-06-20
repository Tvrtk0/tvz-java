package hr.java.covidportal.model;

import java.util.Objects;

/**
 * Klasa koja definira podatke o županiji u kojoj se osoba nalazi.
 */
public class Zupanija extends ImenovaniEntitet {

    private Integer brStanovnika;
    private Integer brZarazenih;

    /**
     * Inicijalizira objekt klase Zupanija
     * @param naziv predstavlja naziv županije
     * @param brStanovnika predstavlja broj stanovnika te županije
     */
    public Zupanija(Long id, String naziv, Integer brStanovnika, Integer brZarazenih) {
        super(id, naziv);
        this.brStanovnika = brStanovnika;
        this.brZarazenih = brZarazenih;
    }

    public double getPostotakZarazenih() {
        return (brZarazenih * 100.0) / brStanovnika;
    }

    public Integer getBrStanovnika() {
        return brStanovnika;
    }

    public void setBrStanovnika(Integer brStanovnika) {
        this.brStanovnika = brStanovnika;
    }

    public Integer getBrZarazenih() {
        return brZarazenih;
    }

    public void setBrZarazenih(Integer brZarazenih) {
        this.brZarazenih = brZarazenih;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Zupanija)) return false;
        if (!super.equals(o)) return false;
        Zupanija zupanija = (Zupanija) o;
        return getBrStanovnika().equals(zupanija.getBrStanovnika());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getBrStanovnika());
    }
}


