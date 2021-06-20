package hr.java.covidportal.model;

/**
 * Klasa koju nasljeđuju svi imenovani entiteti kojima je potrebna varijabla naziv.
 */

public abstract class ImenovaniEntitet {
    private String naziv;

    /**
     * Konstruktor klase ImenovaniEntitet
     * @param naziv varijabla koja prikazuje naziv entiteta
     */
    public ImenovaniEntitet(String naziv) {
        this.naziv = naziv;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

}
