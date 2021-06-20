package hr.java.covidportal.model;

/**
 * Definira bolesti koje osoba može dobiti. Nasljeđuje ImenovaniEntitet jer ima naziv.
 */

public class Bolest extends ImenovaniEntitet {

    private Simptom[] simptomi;

    /**
     * Inicijalizira osobu klase Bolest
     * @param naziv predstavlja naziv bolesti
     * @param simptomi niz simptoma koje bolest sadrži
     */

    public Bolest(String naziv, Simptom[] simptomi) {
        super(naziv);
        this.simptomi = simptomi;
    }

    public Simptom[] getSimptomi() {
        return simptomi;
    }

    public void setSimptomi(Simptom[] simptomi) {
        this.simptomi = simptomi;
    }
}
