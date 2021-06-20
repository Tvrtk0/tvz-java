package hr.java.covidportal.niti;

import hr.java.covidportal.database.BazaPodataka;
import hr.java.covidportal.model.Simptom;

import java.io.IOException;
import java.sql.SQLException;

public class GetSimptomById implements Runnable {
    private static long id;

    @Override
    public synchronized void run() {
        while (BazaPodataka.aktivnaVezaSBazomPodataka) {
            try {
                System.out.println("[GetSimptomById] Čekanje na pristup bazi podataka...");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            Simptom simptom = BazaPodataka.getSimptomById(id);
            BazaPodataka.setSimptom(simptom);
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
        notifyAll();
    }

    public static long getId() {
        return id;
    }

    public static void setId(long id) {
        GetSimptomById.id = id;
    }
}
