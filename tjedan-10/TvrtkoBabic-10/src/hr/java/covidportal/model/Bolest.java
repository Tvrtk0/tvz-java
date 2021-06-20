package hr.java.covidportal.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Definira bolesti koje osoba može dobiti. Nasljeđuje ImenovaniEntitet jer ima naziv.
 */
public class Bolest extends ImenovaniEntitet {

    private List<Simptom> simptomi;

    /**
     * Inicijalizira osobu klase Bolest
     * @param naziv predstavlja naziv bolesti
     * @param simptomi niz simptoma koje bolest sadrži
     */
    public Bolest(Long id, String naziv, List<Simptom> simptomi) {
        super(id, naziv);
        this.simptomi = simptomi;
    }

    public List<Simptom> getSimptomi() {
        return simptomi;
    }

    public void setSimptomi(List<Simptom> simptomi) {
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

    @Override
    public String toString() {
        return getNaziv() + " ";
    }

    public String getSimptomiToString() {
        String str = Arrays.toString(simptomi.toArray());
        return str.substring(1, str.length()-1);
    }
}