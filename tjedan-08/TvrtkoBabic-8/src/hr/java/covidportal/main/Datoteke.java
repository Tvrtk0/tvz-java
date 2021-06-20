package hr.java.covidportal.main;

import hr.java.covidportal.model.*;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Datoteke {
    public static final String ZUPANIJE_TEXT = "dat/zupanije.txt";
    public static final String SIMPTOMI_TEXT = "dat/simptomi.txt";
    public static final String BOLESTI_TEXT = "dat/bolesti.txt";
    public static final String VIRUSI_TEXT = "dat/virusi.txt";
    public static final String OSOBE_TEXT = "dat/osobe.txt";
    private static final Logger logger = Main.logger;

    /**
     * Služi za unos naziva i broja stanovnika županije i kreiranje objekta te vraćanje tog objkta u niz županija.
     * @return vraća se novo keirani objekt županije koji je unešen
     */
    public static List<Zupanija> unosZupanija() {
        System.out.println("Učitavanje podataka o županijama...");
        List<Zupanija> zupanijaList = new ArrayList<>();
        File zupanije = new File(ZUPANIJE_TEXT);
        try (FileReader fileReader = new FileReader(zupanije);
             BufferedReader reader = new BufferedReader(fileReader))
        {
            String linija;
            while ((linija = reader.readLine()) != null) {
                Long id = Long.parseLong(linija);
                String nazivZupanije = reader.readLine();
                Integer brStanovnika = Integer.parseInt(reader.readLine());
                Integer brZarazenih = Integer.parseInt(reader.readLine());
                zupanijaList.add(new Zupanija(id, nazivZupanije, brStanovnika, brZarazenih));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.error("Pogreška kod unosa županija iz datoteke!", ex);
        }

        return zupanijaList;
    }

    /**
     * Služi za unos naziva i vrijednosti simptoma i kreiranje objekta te vraćanje tog objkta u niz simptoma.
     * @return vraća se novo keirani objekt simptom koji je unešen
     */
    public static List<Simptom> unosSimptoma() {
        System.out.println("Učitavanje podataka o simptomima...");
        List<Simptom> simptomiList = new ArrayList<>();
        File simptomi = new File(SIMPTOMI_TEXT);
        try (FileReader fileReader = new FileReader(simptomi);
             BufferedReader reader = new BufferedReader(fileReader))
        {
            String linija;
            while((linija = reader.readLine()) != null) {
                Long id = Long.parseLong(linija);
                String nazivSimptoma = reader.readLine();
                String vrijednostSimptoma = reader.readLine();
                simptomiList.add(new Simptom(id, nazivSimptoma, vrijednostSimptoma));
            }
        }
        catch (IOException ex) {
            ex.getStackTrace();
            logger.error("Pogreška kod unosa simptoma iz datoteke!", ex);
        }

        return simptomiList;
    }

    public static List<Bolest> unosBolesti(List<Simptom> simptomi) {
        File bolesti = new File(BOLESTI_TEXT);
        System.out.println("Učitavanje podataka o bolestima...");
        List<Bolest> listaBolesti = new ArrayList<>();

        try (FileReader fileReader = new FileReader(bolesti);
             BufferedReader reader = new BufferedReader(fileReader))
        {
            String linija;
            while ((linija = reader.readLine()) != null) {
                Long id = Long.parseLong(linija);
                String naziv = reader.readLine();

                List<Simptom> simptomList = new ArrayList<>();
                String[] indexi = reader.readLine().split(",");
                Long[] indexiSimptomaBolesti = new Long[indexi.length];
                for (int i = 0; i < indexi.length; i++) {
                    indexiSimptomaBolesti[i] = Long.parseLong(indexi[i]);
                }
                for (Long i : indexiSimptomaBolesti) {
                    simptomList.add(getSimptomById(simptomi, i));
                }
                listaBolesti.add(new Bolest(id, naziv, simptomList));
            }
        }
        catch (IOException ex) {
            ex.getStackTrace();
            logger.error("Pogreška kod unosa bolesti iz datoteke!", ex);
        }
        return listaBolesti;
    }

    public static List<Virus> unosVirusa(List<Simptom> simptomi) {
        File virusi = new File(VIRUSI_TEXT);
        System.out.println("Učitavanje podataka o virusima...");
        List<Virus> listaVirusa = new ArrayList<>();

        try (FileReader fileReader = new FileReader(virusi);
             BufferedReader reader = new BufferedReader(fileReader))
        {
            String linija;
            while ((linija = reader.readLine()) != null) {
                Long id = Long.parseLong(linija);
                String naziv = reader.readLine();
                List<Simptom> simptomList = new ArrayList<>();
                String[] indexi = reader.readLine().split(",");
                Long[] indexiSimptomaVirusa = new Long[indexi.length];
                for (int i = 0; i < indexi.length; i++) {
                    indexiSimptomaVirusa[i] = Long.parseLong(indexi[i]);
                }
                for (Long i : indexiSimptomaVirusa) {
                    simptomList.add(getSimptomById(simptomi, i));
                }
                listaVirusa.add(new Virus(id, naziv, simptomList));
            }
        }
        catch (IOException ex) {
            ex.getStackTrace();
            logger.error("Pogreška kod unosa virusa iz datoteke!", ex);
        }
        return listaVirusa;
    }

    /**
     * Služi za unos osobe i svih podataka koje pripadaju toj osobi.
     * @param zupanije polje županija u kojem su osobe
     * @return povratna vrijednost je objek Osoba koji se uneso
     */
    public static List<Osoba> unosOsoba(List<Zupanija> zupanije, List<Bolest> bolesti, List<Virus> virusi) {
        System.out.println("Učitavanje osoba...");
        List<Osoba> osobeList = new ArrayList<>();
        File osobe = new File(OSOBE_TEXT);
        try (FileReader fileReader = new FileReader(osobe);
             BufferedReader reader = new BufferedReader(fileReader))
        {
            String linija;
            while((linija = reader.readLine()) != null) {
                Long id = Long.parseLong(linija);
                String ime = reader.readLine();
                String prezime = reader.readLine();
                Integer starost = Integer.parseInt(reader.readLine());
                Long zupanijaId = Long.parseLong(reader.readLine());


                String[] bolestType = reader.readLine().split("-");
                Long bolestId = Long.parseLong(bolestType[1]);
                Bolest trazenaBolest;
                if (bolestType[0].equals("B")) {
                    trazenaBolest = getBolestById(bolesti, bolestId);
                }
                else if (bolestType[0].equals("V")) {
                    trazenaBolest = getVirusById(virusi, bolestId);
                }
                else {
                    System.out.println("Neispravan unos bolesti ili virusa u datoteku Osobe.txt");
                    logger.error("Neispravan unos bolesti ili virusa u datoteku Osobe.txt");
                    trazenaBolest = null;
                }

                List<Osoba> kontaktiraneOsobe = new ArrayList<>();
                String[] indexi = reader.readLine().split(",");
                if (indexi[0].isEmpty()) {
                    osobeList.add(new Osoba.Builder(id)
                            .withIme(ime)
                            .withPrezime(prezime)
                            .withStarost(starost)
                            .withZupanija(getZupanijaById(zupanije, zupanijaId))
                            .withBolescu(trazenaBolest)
                            .build());
                } else {
                    Long[] indexikontaktiranihOsobe = new Long[indexi.length];

                    for (int i = 0; i < indexi.length; i++) {
                        indexikontaktiranihOsobe[i] = Long.parseLong(indexi[i]);
                    }
                    for (Long i : indexikontaktiranihOsobe) {
                        kontaktiraneOsobe.add(getOsobeById(osobeList, i));
                    }

                    osobeList.add(new Osoba.Builder(id)
                            .withIme(ime)
                            .withPrezime(prezime)
                            .withStarost(starost)
                            .withZupanija(getZupanijaById(zupanije, zupanijaId))
                            .withBolescu(trazenaBolest)
                            .withKontakti(kontaktiraneOsobe)
                            .build());
                }
            }
        }
        catch (IOException ex) {
            ex.getStackTrace();
            logger.error("Pogreška kod unosa osoba iz datoteke!", ex);
        }

        return osobeList;
    }

    public static Zupanija getZupanijaById(List<Zupanija> zupanije, Long id) {
        return zupanije.stream()
                .filter(zupanija -> id.equals(zupanija.getId()))
                .findAny()
                .orElse(null);
    }

    public static Simptom getSimptomById(List<Simptom> simptomList, Long id) {
        return simptomList.stream()
                .filter(simptom -> id.equals(simptom.getId()))
                .findAny()
                .orElse(null);
    }

    public static Virus getVirusById(List<Virus> virusi, Long id) {
        return virusi.stream()
                .filter(v -> id.equals(v.getId()))
                .findAny()
                .orElse(null);
    }

    public static Bolest getBolestById(List<Bolest> bolesti, Long id) {
        return bolesti.stream()
                .filter(b -> id.equals(b.getId()))
                .findAny()
                .orElse(null);
    }

    public static Osoba getOsobeById(List<Osoba> osobeList, Long id) {
        return osobeList.stream()
                .filter(osoba -> id.equals(osoba.getId()))
                .findAny()
                .orElse(null);
    }

}
