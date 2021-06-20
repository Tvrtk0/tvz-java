package hr.java.covidportal.model;

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

    public Simptom(String naziv, String vrijednost) {
        super(naziv);
        this.vrijednost = vrijednost;
    }

    public String getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(String vrijednost) {
        this.vrijednost = vrijednost;
    }
}

