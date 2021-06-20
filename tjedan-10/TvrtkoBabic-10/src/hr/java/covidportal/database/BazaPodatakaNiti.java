package hr.java.covidportal.database;

import hr.java.covidportal.model.Bolest;
import hr.java.covidportal.model.Osoba;
import hr.java.covidportal.model.Simptom;
import hr.java.covidportal.model.Zupanija;
import hr.java.covidportal.niti.*;

import java.util.List;
import java.util.SplittableRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BazaPodatakaNiti {
    public static List<Zupanija> getSveZupanije() {
        GetSveZupanijeNit nit = new GetSveZupanijeNit();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(nit);
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return BazaPodataka.getListaZupanija();
    }

    public static List<Simptom> getSveSimptome() {
        GetSveSimptomeNit nit = new GetSveSimptomeNit();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(nit);
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return BazaPodataka.getListaSimptoma();
    }

    public static List<Bolest> getSveBolesti() {
        GetSveBolestiNit nit = new GetSveBolestiNit();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(nit);
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return BazaPodataka.getListaBolesti();
    }

    public static List<Osoba> getSveOsobe() {
        GetSveOsobeNit nit = new GetSveOsobeNit();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(nit);
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return BazaPodataka.getListaOsoba();
    }

    public static void spremiNoviSimptom(Simptom simptom) {
        SpremiSimptomNit nit = new SpremiSimptomNit();
        ExecutorService executorService = Executors.newCachedThreadPool();
        SpremiSimptomNit.setSimptom(simptom);
        executorService.execute(nit);
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void spremiNovuZupaniju(Zupanija zupanija) {
        SpremiZupanijuNit nit = new SpremiZupanijuNit();
        ExecutorService executorService = Executors.newCachedThreadPool();
        SpremiZupanijuNit.setZupanija(zupanija);
        executorService.execute(nit);
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void spremiNovuBolest(Bolest bolest) {
        SpremiBolestNit nit = new SpremiBolestNit();
        ExecutorService executorService = Executors.newCachedThreadPool();
        SpremiBolestNit.setBolest(bolest);
        executorService.execute(nit);
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void spremiNovuOsobu(Osoba osoba) {
        SpremiOsobuNit nit = new SpremiOsobuNit();
        ExecutorService executorService = Executors.newCachedThreadPool();
        SpremiOsobuNit.setOsoba(osoba);
        executorService.execute(nit);
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Zupanija getZupanijaById(long id) {
        GetZupanijaById nit = new GetZupanijaById();
        ExecutorService executorService = Executors.newCachedThreadPool();
        GetZupanijaById.setId(id);
        executorService.execute(nit);
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return BazaPodataka.getZupanija();
    }

    public static Simptom getSimptomById(long id) {
        GetSimptomById nit = new GetSimptomById();
        ExecutorService executorService = Executors.newCachedThreadPool();
        GetSimptomById.setId(id);
        executorService.execute(nit);
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return BazaPodataka.getSimptom();
    }

    public static Bolest getBolestById(long id) {
        GetBolestById nit = new GetBolestById();
        ExecutorService executorService = Executors.newCachedThreadPool();
        GetBolestById.setId(id);
        executorService.execute(nit);
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return BazaPodataka.getBolest();
    }

    public static Osoba getOsobaById(long id) {
        GetOsobaById nit = new GetOsobaById();
        ExecutorService executorService = Executors.newCachedThreadPool();
        GetOsobaById.setId(id);
        executorService.execute(nit);
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return BazaPodataka.getOsoba();
    }
}
