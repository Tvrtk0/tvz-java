package hr.java.covidportal.sort;

import hr.java.covidportal.model.Zupanija;

import java.util.Comparator;

public class CovidSorter implements Comparator<Zupanija> {
    @Override
    public int compare(Zupanija o1, Zupanija o2) {
        double p1 = o1.getPostotakZarazenih();
        double p2 = o2.getPostotakZarazenih();
        if(p1 > p2) {
            return 1;
        }
        else if (p1 < p2) {
            return -1;
        }
        else {
            return 0;
        }
    }
}
