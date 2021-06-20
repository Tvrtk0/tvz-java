package hr.java.covidportal.niti;

import hr.java.covidportal.database.BazaPodataka;
import hr.java.covidportal.model.Zupanija;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class GetSveZupanijeNit implements Runnable {
    @Override
    public synchronized void run() {
        while (BazaPodataka.aktivnaVezaSBazomPodataka) {
            try {
                System.out.println("Čekanje na pristup bazi podataka...");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            List<Zupanija> listaZupanija = BazaPodataka.getSveZupanije();
            BazaPodataka.setListaZupanija(listaZupanija);
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
        notifyAll();
    }
}
