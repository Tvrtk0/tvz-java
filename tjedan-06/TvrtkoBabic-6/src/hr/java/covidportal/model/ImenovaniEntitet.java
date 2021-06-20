package hr.java.covidportal.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Klasa koju nasljeđuju svi imenovani entiteti kojima je potrebna varijabla naziv.
 */
public abstract class ImenovaniEntitet implements Serializable {
    private Long id;
    private String naziv;

    /**
     * Konstruktor klase ImenovaniEntitet
     * @param naziv varijabla koja prikazuje naziv entiteta
     */
    public ImenovaniEntitet(Long id, String naziv) {
        this.naziv = naziv;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImenovaniEntitet)) return false;
        ImenovaniEntitet that = (ImenovaniEntitet) o;
        return Objects.equals(getNaziv(), that.getNaziv());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNaziv());
    }
}
