package hr.java.covidportal.niti;

import hr.java.covidportal.database.BazaPodataka;
import hr.java.covidportal.model.Bolest;

import java.io.IOException;
import java.sql.SQLException;

public class SpremiBolestNit implements Runnable {
    private static Bolest bolest;

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
            BazaPodataka.spremiNovuBolest(bolest);
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
        notifyAll();
    }

    public static Bolest getBolest() {
        return bolest;
    }

    public static void setBolest(Bolest bolest) {
        SpremiBolestNit.bolest = bolest;
    }
}
