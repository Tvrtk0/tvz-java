package hr.java.covidportal.niti;

import hr.java.covidportal.database.BazaPodataka;
import hr.java.covidportal.model.Simptom;

import java.io.IOException;
import java.sql.SQLException;

public class SpremiSimptomNit implements Runnable {
    private static Simptom simptom;

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
            BazaPodataka.spremiNoviSimptom(simptom);
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
        notifyAll();
    }

    public static Simptom getSimptom() {
        return simptom;
    }

    public static void setSimptom(Simptom simptom) {
        SpremiSimptomNit.simptom = simptom;
    }
}
