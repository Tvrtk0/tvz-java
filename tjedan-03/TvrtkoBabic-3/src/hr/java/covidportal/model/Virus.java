package hr.java.covidportal.model;

/**
 * Nasljeđuje klasu Bolest i implementira interface Zarazno.
 */

public class Virus extends Bolest implements Zarazno {

    /**
     * Inicijalizira vrijednosti objekta virus naredbom super jer ih nasljeđuje iz klase Bolest
     * @param naziv predstavlja naziv virusa
     * @param simptomi niz simptoma koje virus sadrži
     */

    public Virus(String naziv, Simptom[] simptomi) {
        super(naziv, simptomi);
    }

    /**
     * Služi za prelazak virusa na osobu
     * @param o objekt osobe na koju prelazi virus
     */
    @Override
    public void prelazakZarazeNaOsobu(Osoba o) {
        o.setZarazenBolescu(this);
    }
}
