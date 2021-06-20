package hr.java.covidportal.enumeracije;

public enum VrijednostSimptoma {
    RIJETKO("RIJETKO"),
    SREDNJE("SREDNJE"),
    CESTO("ČESTO"),
    PRODUKTIVNI("PRODUKTIVNI"),
    INTENZIVNO("INTENZIVNO"),
    VISOKA("VISOKA"),
    JAKA("JAKA");
    private final String vrijednost;

    VrijednostSimptoma(String vrijednost) {
        this.vrijednost = vrijednost;
    }

    public String getVrijednost() {
        return vrijednost;
    }
}