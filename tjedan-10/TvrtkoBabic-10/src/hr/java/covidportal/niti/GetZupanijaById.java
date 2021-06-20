package hr.java.covidportal.niti;

import hr.java.covidportal.database.BazaPodataka;
import hr.java.covidportal.model.Zupanija;

import java.io.IOException;
import java.sql.SQLException;

public class GetZupanijaById implements Runnable {
    private static long id;

    @Override
    public synchronized void run() {
        while (BazaPodataka.aktivnaVezaSBazomPodataka) {
            try {
                System.out.println("[GetZupanijaById] Čekanje na pristup bazi podataka...");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            Zupanija zupanija = BazaPodataka.getZupanijaById(id);
            BazaPodataka.setZupanija(zupanija);
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
        notifyAll();
    }

    public static long getId() {
        return id;
    }

    public static void setId(long id) {
        GetZupanijaById.id = id;
    }
}
