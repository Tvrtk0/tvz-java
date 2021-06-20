package hr.java.covidportal.model;

import java.security.spec.RSAOtherPrimeInfo;

public class Osoba {

    private String ime;
    private String prezime;
    private Integer Starost;
    private Zupanija zupanija;
    private Bolest zarazenBolescu;
    private Osoba[] kontaktiraneOsobe;

    public Osoba(String ime, String prezime, Integer starost, Zupanija zupanija,
                 Bolest zarazenBolescu, Osoba[] kontaktiraneOsobe) {
        this.ime = ime;
        this.prezime = prezime;
        Starost = starost;
        this.zupanija = zupanija;
        this.zarazenBolescu = zarazenBolescu;
        this.kontaktiraneOsobe = kontaktiraneOsobe;
    }

    public void ispisOsobe() {
        System.out.println("Ime i prezime: " + ime + " " + prezime);
        System.out.println("Starost: " + Starost);
        System.out.println("Županija prebivališta: " + zupanija.getNaziv() + " (" + zupanija.getBrStanovnika() + " stan.)");
        System.out.println("Zaražen Bolešću: " + zarazenBolescu.getNaziv());

        Simptom[] simptomi = zarazenBolescu.getSimptomi();
        System.out.println("Simptomi: ");
        for(int i = 0; i < simptomi.length; i++){
            System.out.println(simptomi[i].getNaziv() + " " + simptomi[i].getVrijednost());
        }
        System.out.println("Kontaktirane osobe: ");
        if (kontaktiraneOsobe != null) {
            for (int i = 0; i < kontaktiraneOsobe.length; i++) {
                    System.out.println(kontaktiraneOsobe[i].getIme() + " " + kontaktiraneOsobe[i].getPrezime());
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
