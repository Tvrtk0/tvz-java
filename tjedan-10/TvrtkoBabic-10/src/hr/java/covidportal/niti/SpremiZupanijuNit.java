package hr.java.covidportal.niti;

import hr.java.covidportal.database.BazaPodataka;
import hr.java.covidportal.model.Zupanija;

import java.io.IOException;
import java.sql.SQLException;

public class SpremiZupanijuNit implements Runnable {
    private static Zupanija zupanija;

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
            BazaPodataka.spremiNovuZupaniju(zupanija);
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
        notifyAll();
    }

    public static Zupanija getZupanija() {
        return zupanija;
    }

    public static void setZupanija(Zupanija zupanija) {
        SpremiZupanijuNit.zupanija = zupanija;
    }
}
