package hr.java.covidportal.niti;

import hr.java.covidportal.database.BazaPodataka;
import hr.java.covidportal.model.Bolest;

import java.io.IOException;
import java.sql.SQLException;

public class GetBolestById implements Runnable {
    private static long id;

    @Override
    public synchronized void run() {
        while (BazaPodataka.aktivnaVezaSBazomPodataka) {
            try {
                System.out.println("[GetBolestById] Čekanje na pristup bazi podataka...");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            Bolest bolest = BazaPodataka.getBolestById(id);
            BazaPodataka.setBolest(bolest);
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
        notifyAll();
    }

    public static long getId() {
        return id;
    }

    public static void setId(long id) {
        GetBolestById.id = id;
    }
}
