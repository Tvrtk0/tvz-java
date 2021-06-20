package hr.java.covidportal.model;

public class Zupanija {

    private String naziv;
    private Integer brStanovnika;

    public Zupanija(String naziv, Integer brStanovnika) {
        this.naziv = naziv;
        this.brStanovnika = brStanovnika;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Integer getBrStanovnika() {
        return brStanovnika;
    }

    public void setBrStanovnika(Integer brStanovnika) {
        this.brStanovnika = brStanovnika;
    }
}


