package hr.java.covidportal.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Definira bolesti koje osoba može dobiti. Nasljeđuje ImenovaniEntitet jer ima naziv.
 */
public class Bolest extends ImenovaniEntitet {

    private Set<Simptom> simptomi = new HashSet<>();

    /**
     * Inicijalizira osobu klase Bolest
     * @param naziv predstavlja naziv bolesti
     * @param simptomi niz simptoma koje bolest sadrži
     */
    public Bolest(String naziv, Set<Simptom> simptomi) {
        super(naziv);
        this.simptomi = simptomi;
    }

    public Set<Simptom> getSimptomi() {
        return simptomi;
    }

    public void setSimptomi(Set<Simptom> simptomi) {
        this.simptomi = simptomi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bolest)) return false;
        if (!super.equals(o)) return false;
        Bolest bolest = (Bolest) o;
        return Objects.equals(getSimptomi(), bolest.getSimptomi());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getSimptomi());
    }
}
