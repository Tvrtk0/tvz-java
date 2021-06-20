package hr.java.covidportal.niti;

import hr.java.covidportal.database.BazaPodatakaNiti;
import hr.java.covidportal.model.Zupanija;

import java.util.Comparator;

public class NajviseZarazenihNit implements Runnable {

    @Override
    public void run() {
        while (true) {
            Zupanija zupanijaMax = getZupanijaNajveciPostotakZarazenih();
            System.out.println(zupanijaMax.getNaziv());
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static Zupanija getZupanijaNajveciPostotakZarazenih() {
        return BazaPodatakaNiti.getSveZupanije().stream()
                .max(Comparator.comparing(Zupanija::getPostotakZarazenih))
                .get();
    }
}
