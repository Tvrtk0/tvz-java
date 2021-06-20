package hr.java.covidportal.model;

public class Zupanija extends ImenovaniEntitet {

    private Integer brStanovnika;

    public Zupanija(String naziv, Integer brStanovnika) {
        super(naziv);
        this.brStanovnika = brStanovnika;
    }

    public Integer getBrStanovnika() {
        return brStanovnika;
    }

    public void setBrStanovnika(Integer brStanovnika) {
        this.brStanovnika = brStanovnika;
    }
}


