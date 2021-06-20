package hr.java.covidportal.genericsi;

import hr.java.covidportal.model.Osoba;
import hr.java.covidportal.model.Virus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KlinikaZaInfektivneBolesti<T extends Virus, S extends Osoba> {
    private List<T> listaVirusa = new ArrayList<>();
    private List<S> listaOsobaZarazenihVirusom = new ArrayList<>();

    public KlinikaZaInfektivneBolesti(List<T> listaVirusa, List<S> listaOsobaZarazenihVirusom) {
        this.listaVirusa = listaVirusa;
        this.listaOsobaZarazenihVirusom = listaOsobaZarazenihVirusom.stream()
                .filter(osoba -> osoba.getZarazenBolescu() instanceof Virus)
                .collect(Collectors.toList());
    }

    public void addVirus(T virus) {
        listaVirusa.add(virus);
    }

    public void addOsoba(S osoba) {
        listaOsobaZarazenihVirusom.add(osoba);
    }

    public List<T> getListaVirusa() {
        return listaVirusa;
    }

    public void setListaVirusa(List<T> listaVirusa) {
        this.listaVirusa = listaVirusa;
    }

    public List<S> getListaOsoba() {
        return listaOsobaZarazenihVirusom;
    }

    public void setListaOsoba(List<S> listaOsoba) {
        this.listaOsobaZarazenihVirusom = listaOsoba;
    }
}
