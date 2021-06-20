package hr.java.covidportal.sort;

import hr.java.covidportal.model.Virus;

import java.util.Comparator;

public class VirusSorter implements Comparator<Virus> {

    @Override
    public int compare(Virus v1, Virus v2) {
        return v2.getNaziv().compareTo(v1.getNaziv());
    }
}
