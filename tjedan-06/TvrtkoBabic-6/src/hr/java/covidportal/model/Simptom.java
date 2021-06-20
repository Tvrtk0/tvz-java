package hr.java.covidportal.model;

import java.util.Objects;

/**
 * Klasa koja definira simptome koje osoba može imati da bi mogli definirat bolesti/viruse.
 */
public class Simptom extends ImenovaniEntitet {

    private String vrijednost;

    /**
     * Inicijalizacija objekta klase Simptom
     * @param naziv predstavlja naziv simptoma
     * @param vrijednost prikazuje koliko često se simptom pojavljuje
     */
    public Simptom(Long id, String naziv, String vrijednost) {
        super(id, naziv);
        this.vrijednost = vrijednost;
    }

    public String getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(String vrijednost) {
        this.vrijednost = vrijednost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Simptom)) return false;
        if (!super.equals(o)) return false;
        Simptom simptom = (Simptom) o;
        return Objects.equals(getVrijednost(), simptom.getVrijednost());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getVrijednost());
    }
}

