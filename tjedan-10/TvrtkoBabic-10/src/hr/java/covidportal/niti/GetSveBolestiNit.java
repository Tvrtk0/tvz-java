package hr.java.covidportal.niti;

import hr.java.covidportal.database.BazaPodataka;
import hr.java.covidportal.model.Bolest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class GetSveBolestiNit implements Runnable {
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
            List<Bolest> listaBolesti = BazaPodataka.getSveBolesti();
            BazaPodataka.setListaBolesti(listaBolesti);
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
        notifyAll();
    }
}
