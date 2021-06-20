package hr.java.covidportal.model;

import java.security.spec.RSAOtherPrimeInfo;

public class Osoba {

    private String ime;
    private String prezime;
    private Integer Starost;
    private Zupanija zupanija;
    private Bolest zarazenBolescu;
    private Osoba[] kontaktiraneOsobe;

    public static class Builder {
        private String ime;
        private String prezime;
        private Integer Starost;
        private Zupanija zupanija;
        private Bolest zarazenBolescu;
        private Osoba[] kontaktiraneOsobe;

        public Builder(String ime) {
            this.ime = ime;
        }

        public Builder withPrezime(String prezime) {
            this.prezime = prezime;
            return this;
        }

        public Builder withStarost(Integer Starost) {
            this.Starost = Starost;
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

        public Builder withKontakti(Osoba[] kontaktiraneOsobe) {
            this.kontaktiraneOsobe = kontaktiraneOsobe;
            return this;
        }

        public Osoba build() {
            Osoba osoba = new Osoba();
            osoba.ime = this.ime;
            osoba.prezime = this.prezime;
            osoba.Starost = this.Starost;
            osoba.zupanija = this.zupanija;
            osoba.zarazenBolescu = this.zarazenBolescu;
            osoba.kontaktiraneOsobe = this.kontaktiraneOsobe;
            if (osoba.kontaktiraneOsobe != null) {
                if (osoba.zarazenBolescu instanceof Virus virus) {
                    for (int i = 0; i < osoba.kontaktiraneOsobe.length; i++) {
                        virus.prelazakZarazeNaOsobu(osoba.kontaktiraneOsobe[i]);
                    }
                }
            }
            return osoba;
        }
    }

    private Osoba() {
    }

    public void ispisOsobe() {
        System.out.println("Ime i prezime: " + ime + " " + prezime);
        System.out.println("Starost: " + Starost);
        System.out.println("Županija prebivališta: " + zupanija.getNaziv() + " (" + zupanija.getBrStanovnika() + " stan.)");
        System.out.println("Zaražen Bolešću: " + zarazenBolescu.getNaziv());

        Simptom[] simptomi = zarazenBolescu.getSimptomi();
        System.out.println("Simptomi: ");
        for(int i = 0; i < simptomi.length; i++){
            System.out.println(" - " + simptomi[i].getNaziv() + " " + simptomi[i].getVrijednost());
        }
        System.out.println("Kontaktirane osobe: ");
        if (kontaktiraneOsobe != null) {
            for (int i = 0; i < kontaktiraneOsobe.length; i++) {
                    System.out.println(" - " + kontaktiraneOsobe[i].getIme() + " " + kontaktiraneOsobe[i].getPrezime());
            }
        } else
            System.out.println("Nema kontaktiranih osoba.");
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

    public Integer getStarost() {
        return Starost;
    }

    public void setStarost(Integer starost) {
        Starost = starost;
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

    public Osoba[] getKontaktiraneOsobe() {
        return kontaktiraneOsobe;
    }

    public void setKontaktiraneOsobe(Osoba[] kontaktiraneOsobe) {
        this.kontaktiraneOsobe = kontaktiraneOsobe;
    }
}
