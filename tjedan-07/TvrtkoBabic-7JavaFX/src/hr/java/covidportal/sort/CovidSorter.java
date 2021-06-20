package hr.java.covidportal.sort;

import hr.java.covidportal.model.Zupanija;

import java.util.Comparator;

/**
 * Služi za sortiranje županija
 */
public class CovidSorter implements Comparator<Zupanija> {

    /**
     * Sortita županije prema postotku zaraženih osoba
     * @param o1 prva županija
     * @param o2 sljedeća županija
     * @return ovisno o rezultatu 1, -1 i 0 što predstavlja >, <, ==
     */
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