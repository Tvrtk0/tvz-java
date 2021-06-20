package hr.java.covidportal.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Služi za strukturiranje podataka o osobi i njezinoj bolesti ili virusu.
 */
public class Osoba implements Serializable {

    private Long id;
    private String ime;
    private String prezime;
    private LocalDate datumRodenja;
    private Zupanija zupanija;
    private Bolest zarazenBolescu;
    private List<Osoba> kontaktiraneOsobe;

    /**
     * Builder Pattern koji služi kao konstruktor. Sami biramo koliko varijabli inicijaliziramo u konstruktoru.
     */
    public static class Builder {
        private Long id;
        private String ime;
        private String prezime;
        private LocalDate datumRodenja;
        private Zupanija zupanija;
        private Bolest zarazenBolescu;
        private List<Osoba> kontaktiraneOsobe;

        public Builder(Long id) {
            this.id = id;
        }

        public Builder withIme(String ime) {
            this.ime = ime;
            return this;
        }

        public Builder withPrezime(String prezime) {
            this.prezime = prezime;
            return this;
        }

        public Builder withDatumRodjenja(LocalDate datumRodenja) {
            this.datumRodenja = datumRodenja;
            return this;
        }

        public Builder withZupanija(Zupanija zupanija) {
            this.zupanija = zupanija;
            return this;
        }

        public Builder withBolescu(Bolest zarazenBolescu) {
            this.zarazenBolescu = zarazenBolescu;
            return this;
        }

        public Builder withKontakti(List<Osoba> kontaktiraneOsobe) {
            this.kontaktiraneOsobe = kontaktiraneOsobe;
            return this;
        }

        /**
         * Služi kao konstruktor objekta osobe koji se može postupeno graditi.
         * @return izlazna vrijednost je objekt osoba.
         */
        public Osoba build() {
            Osoba osoba = new Osoba();
            osoba.ime = this.ime;
            osoba.id = this.id;
            osoba.prezime = this.prezime;
            osoba.datumRodenja = this.datumRodenja;
            osoba.zupanija = this.zupanija;
            osoba.zarazenBolescu = this.zarazenBolescu;
            osoba.kontaktiraneOsobe = this.kontaktiraneOsobe;
            if (osoba.kontaktiraneOsobe != null) {
                if (osoba.zarazenBolescu instanceof Virus virus) {
                    for (Osoba kontakt : osoba.kontaktiraneOsobe) {
                        virus.prelazakZarazeNaOsobu(kontakt);
                    }
                }
            } else osoba.kontaktiraneOsobe = new ArrayList<>();
            return osoba;
        }
    }

    /**
     * Ne koristi se jer koristimo builder pattern
     */
    private Osoba() {
    }

    public long getStarost() {
        LocalDate now = LocalDate.now();
        return ChronoUnit.YEARS.between(datumRodenja, now);
    }

    /**
     * Služi za ispis svih podataka o osobi.
     */
    public void ispisOsobe() {
        System.out.println("Ime i prezime: " + ime + " " + prezime);
        System.out.println("Starost: " + this.getStarost());
        System.out.println("Županija prebivališta: " + zupanija.getNaziv() + " (" + zupanija.getBrStanovnika() + " stan.)");
        System.out.println("Zaražen Bolešću: " + zarazenBolescu.getNaziv());

        List<Simptom> simptomi = zarazenBolescu.getSimptomi();
        System.out.println("Simptomi: ");
        for(Simptom simptom : simptomi){
            System.out.println(" - " + simptom.getNaziv() + " " + simptom.getVrijednost());
        }
        System.out.println("Kontaktirane osobe: ");
        if (kontaktiraneOsobe != null) {
            for (Osoba kontakt : kontaktiraneOsobe) {
                System.out.println(" - " + kontakt.getIme() + " " + kontakt.getPrezime());
            }
        } else
            System.out.println(" - Nema kontaktiranih osoba.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Osoba)) return false;
        Osoba osoba = (Osoba) o;
        return Objects.equals(getIme(), osoba.getIme()) &&
                Objects.equals(getPrezime(), osoba.getPrezime()) &&
                Objects.equals(getStarost(), osoba.getStarost()) &&
                Objects.equals(getZupanija(), osoba.getZupanija()) &&
                Objects.equals(getZarazenBolescu(), osoba.getZarazenBolescu()) &&
                Objects.equals(getKontaktiraneOsobe(), osoba.getKontaktiraneOsobe());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIme(), getPrezime(), getStarost(), getZupanija(), getZarazenBolescu(), getKontaktiraneOsobe());
    }

    @Override
    public String toString() {
        return getIme() + " " + getPrezime();
    }

    public String getKontaktiraneOsobeToString() {
        if (kontaktiraneOsobe.isEmpty()) return "-";
        String str = Arrays.toString(kontaktiraneOsobe.toArray());
        return str.substring(1, str.length()-1);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public LocalDate getDatumRodenja() {
        return datumRodenja;
    }

    public void setDatumRodenja(LocalDate datum_rodenja) {
        this.datumRodenja = datum_rodenja;
    }

    public Zupanija getZupanija() {
        return zupanija;
    }

    public void setZupanija(Zupanija zupanija) {
        this.zupanija = zupanija;
    }

    public Bolest getZarazenBolescu() {
        return zarazenBolescu;
    }

    public void setZarazenBolescu(Bolest zarazenBolescu) {
        this.zarazenBolescu = zarazenBolescu;
    }

    public List<Osoba> getKontaktiraneOsobe() {
        return kontaktiraneOsobe;
    }

    public void setKontaktiraneOsobe(List<Osoba> kontaktiraneOsobe) {
        this.kontaktiraneOsobe = kontaktiraneOsobe;
    }
}