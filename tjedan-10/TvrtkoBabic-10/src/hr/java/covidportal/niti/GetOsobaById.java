package hr.java.covidportal.niti;

import hr.java.covidportal.database.BazaPodataka;
import hr.java.covidportal.model.Osoba;

import java.io.IOException;
import java.sql.SQLException;

public class GetOsobaById implements Runnable {
    private static long id;

    @Override
    public synchronized void run() {
        while (BazaPodataka.aktivnaVezaSBazomPodataka) {
            try {
                System.out.println("[GetOsobaById] Čekanje na pristup bazi podataka...");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            Osoba osoba = BazaPodataka.getOsobaById(id);
            BazaPodataka.setOsoba(osoba);
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
        notifyAll();
    }

    public static long getId() {
        return id;
    }

    public static void setId(long id) {
        GetOsobaById.id = id;
    }
}
