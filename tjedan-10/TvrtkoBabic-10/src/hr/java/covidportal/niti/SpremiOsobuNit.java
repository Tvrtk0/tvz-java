package hr.java.covidportal.niti;

import hr.java.covidportal.database.BazaPodataka;
import hr.java.covidportal.model.Osoba;

import java.io.IOException;
import java.sql.SQLException;

public class SpremiOsobuNit implements Runnable {
    private static Osoba osoba;

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
            BazaPodataka.spremiNovuOsobu(osoba);
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
        notifyAll();
    }

    public static Osoba getOsoba() {
        return osoba;
    }

    public static void setOsoba(Osoba osoba) {
        SpremiOsobuNit.osoba = osoba;
    }
}
