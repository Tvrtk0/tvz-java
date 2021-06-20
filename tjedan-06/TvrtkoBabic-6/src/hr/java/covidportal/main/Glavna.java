package hr.java.covidportal.main;

import hr.java.covidportal.enumeracije.VrijednostSimptoma;
import hr.java.covidportal.genericsi.KlinikaZaInfektivneBolesti;
import hr.java.covidportal.model.*;
import hr.java.covidportal.sort.CovidSorter;
import hr.java.covidportal.sort.VirusSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 *  Služi za unos i ispis Osoba, njihovih bolesti i simptoma te podataka o osobama kao što su županija stanovanja
 *  broj stanovnika te županije, ime, prezime i starost osobe i s kojim je osobama ta osoba bila u kontaktu.
 */
public class Glavna {
    private static final Logger logger = LoggerFactory.getLogger(Glavna.class);

    /**
     * Služi za pokretanje programa koji će od korisnika tražiti unos Osoba i podatcima o toj osobi,
     * Bolesti, Virusa, Simptoma. Unosi se testirju i podatci zapisuju u log.
     * @param args argumenti komandne linije se ne koriste.
     */
    public static void main(String[] args) {
        logger.info("Pokenuta je aplikacija.");

        Scanner scanner = new Scanner(System.in);

        List<Zupanija> listaZupanija = new ArrayList<>(unosZupanija());
        List<Simptom> listaSimptomi = unosSimptoma();
        List<Bolest> listaBolestiIvirusa = unosBolesti(listaSimptomi);
        List<Osoba> listaOsoba = unosOsoba(listaZupanija, listaBolestiIvirusa);

        System.out.println("Popis osoba: ");
        listaOsoba.forEach(Osoba::ispisOsobe);

        //Mapiranje
        Map<Bolest, List<Osoba>> mapaBolesneOsobe = listaOsoba.stream()
                .collect(Collectors.groupingBy(Osoba::getZarazenBolescu));
        ispisBolesnihOsoba(mapaBolesneOsobe);

        System.out.println("Najveći postotak zaraženosti je u županiji: "
                + listaZupanija.get(listaZupanija.size()-1).getNaziv() + ": "
                + listaZupanija.get(listaZupanija.size()-1).getPostotakZarazenih() + "%.");

        klinika(listaBolestiIvirusa, listaOsoba);

        pretragaPoPrezimenu(scanner, listaOsoba);

        listaBolestiIvirusa.stream()
                .map(bolest -> "Broj simptoma za " + bolest.getNaziv() + ": " + bolest.getSimptomi().size())
                .forEach(System.out::println);

        serijalizacijaZupanije(listaZupanija);

        logger.info("Kraj programa.");
    }

    public static void serijalizacijaZupanije(List<Zupanija> zupanije) {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("dat/objektiZupanije.dat")))
        {
            List<Zupanija> listaZupanija = zupanije.stream()
                    .filter(z -> z.getPostotakZarazenih() > 2)
                    .collect(Collectors.toList());
            out.writeObject(listaZupanija);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            logger.error("Pogreška pri serijalizaciji!", ex);
        }

        List<Zupanija> procitaneZupanije = null;
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream("dat/objektiZupanije.dat")))
        {
            procitaneZupanije = (List<Zupanija>) in.readObject();
            procitaneZupanije.forEach(z -> System.out.println(z.getNaziv() + " " + z.getPostotakZarazenih()));
        }
        catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            logger.error("Pogreška pri deserijalizaciji!", ex);
        }
    }

    /**
     * Pretražuje osobe sa unesenim stringom te ako sadrže taj string ispisuje te osobe
     * @param scanner služi za unos podataka
     * @param listaOsoba lista osoba koje se pretražuju
     */
    public static void pretragaPoPrezimenu(Scanner scanner, List<Osoba> listaOsoba) {
        System.out.println("Unesite string za pretragu po prezimenu: ");
        String nadji = scanner.nextLine();

        System.out.println("Osobe čije prezime sadrži " + nadji + ": ");
        listaOsoba = listaOsoba.stream()
                .filter(osoba -> osoba.getPrezime().toLowerCase().contains(nadji.toLowerCase()))
                .collect(Collectors.toList());

        Optional<Osoba> osoba1 = listaOsoba.stream().findFirst();
        if (osoba1.isPresent()) {
            listaOsoba.forEach(o -> System.out.println(o.getIme() + " " + o.getPrezime()));
        } else {
            System.out.println("Nema osobe koja sadrži " + nadji + "! " + osoba1);
        }
    }

    /**
     * Ispisuje sortirane viruse klinike za infektivne bolesti
     * @param listaBolesti sadrži bolesti
     * @param listaOsoba sadrži osobe
     */
    public static void klinika(List<Bolest> listaBolesti, List<Osoba> listaOsoba) {
        List<Virus> listaVirusa = listaBolesti.stream()
                .filter(bolest -> bolest instanceof Virus)
                .map(virus -> (Virus)virus)
                .collect(Collectors.toList());

        KlinikaZaInfektivneBolesti<Virus, Osoba> klinika = new KlinikaZaInfektivneBolesti<>(listaVirusa, listaOsoba);
        klinikaSortiranjeVirusa(klinika);

        System.out.println("Sortirani virusi: ");
        klinika.getListaVirusa().forEach(System.out::println);
    }

    /**
     * Sortira viruse klinike Z - A i ispisuje vrijeme trajanja sortiranja pomocu i bez lambdi
     * @param klinika sadrži viruse i osobe zaražene tim virusima
     */
    public static void klinikaSortiranjeVirusa
            (KlinikaZaInfektivneBolesti<Virus, Osoba> klinika) {
        Instant start1 = Instant.now();
        klinika.getListaVirusa().sort(new VirusSorter());
        Instant end1 = Instant.now();

        Instant start2 = Instant.now();
        List<Virus> listaVirusa = klinika.getListaVirusa().stream()
                .sorted(Comparator.comparing(ImenovaniEntitet::getNaziv).reversed())
                .collect(Collectors.toList());
        Instant end2 = Instant.now();

        klinika.setListaVirusa(listaVirusa);
        System.out.println("Vrijeme sortiranja bez lambda: " + Duration.between(start1, end1).toMillis() + " ms.");
        System.out.println("Vrijeme sortiranja lambdom: " + Duration.between(start2, end2).toMillis() + " ms.");
    }

    /**
     * Ispisuje sve osobe koje boluju od određene bolesti ili virusa.
     * @param mapaBolesneOsobe kljuć su bolesti a value lista osoba koje su zaražene tom bolesti ili virusom
     */
    public static void ispisBolesnihOsoba(Map<Bolest, List<Osoba>> mapaBolesneOsobe) {
        for(Map.Entry<Bolest, List<Osoba>> m : mapaBolesneOsobe.entrySet()) {
            if (m.getValue() != null && !m.getValue().isEmpty()) {
                if(m.getKey() instanceof Virus)
                    System.out.print("Osobe zaražene virusom ");
                else System.out.print("Osobe zaražene bolešću ");
                System.out.println(m.getKey().getNaziv() + ":");
                for (Osoba o : m.getValue())
                    System.out.println(" - " + o.getIme() + " " + o.getPrezime());
            }
        }
    }

    public static Osoba getOsobeById(List<Osoba> osobeList, Long id) {
        return osobeList.stream()
                .filter(osoba -> id.equals(osoba.getId()))
                .findAny()
                .orElse(null);
    }

    /**
     * Služi za unos osobe i svih podataka koje pripadaju toj osobi.
     * @param zupanije polje županija u kojem su osobe
     * @param bolesti polje bolesti kojima osoba može biti zaražena
     * @return povratna vrijednost je objek Osoba koji se uneso
     */
    public static List<Osoba> unosOsoba(List<Zupanija> zupanije, List<Bolest> bolesti) {
        System.out.println("Učitavanje osoba...");
        List<Osoba> osobeList = new ArrayList<>();
        File osobe = new File("dat/osobe.txt");
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
                Integer bolestId = Integer.parseInt(reader.readLine());

                List<Osoba> kontaktiraneOsobe = new ArrayList<>();
                String[] indexi = reader.readLine().split(",");
                if (indexi[0].isEmpty()) {
                    osobeList.add(new Osoba.Builder(id)
                            .withIme(ime)
                            .withPrezime(prezime)
                            .withStarost(starost)
                            .withZupanija(getZupanijaById(zupanije, zupanijaId))
                            .withBolescu(bolesti.get(bolestId - 1))
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
                            .withBolescu(bolesti.get(bolestId - 1))
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

    public static Virus getVirusById(List<Bolest> bolestiIvirusi, Long id) {
        return bolestiIvirusi.stream()
                .filter(v -> v instanceof  Virus)
                .map(v -> (Virus) v)
                .filter(v -> id.equals(v.getId()))
                .findAny()
                .orElse(null);
    }

    public static Bolest getBolestById(List<Bolest> bolestiIvirusi, Long id) {
        return bolestiIvirusi.stream()
                .filter(v -> id.equals(v.getId()) && !v.equals(getVirusById(bolestiIvirusi, id)))
                .findAny()
                .orElse(null);
    }

    /**
     * Služi za unos bolesti ili virusa i odabir simptoma koja ta bolest ili virus ima iz niza prije unešenih simptoma.
     * @param simptomi niz simptoma koje bolest može poprimiti
     * @return povratna vrijednost je objek Bolest koji je unesen
     */
    public static List<Bolest> unosBolesti(List<Simptom> simptomi) {
        List<Bolest> bolestList = new ArrayList<>();
        File bolesti = new File("dat/bolesti.txt");
        File virusi = new File("dat/virusi.txt");

        //Bolesti
        System.out.println("Učitavanje podataka o bolestima...");
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
                bolestList.add(new Bolest(id, naziv, simptomList));
            }
        }
        catch (IOException ex) {
            ex.getStackTrace();
            logger.error("Pogreška kod unosa bolesti iz datoteke!", ex);
        }

        //Virusi
        System.out.println("Učitavanje podataka o virusima...");
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
                bolestList.add(new Virus(id, naziv, simptomList));
            }
        }
        catch (IOException ex) {
            ex.getStackTrace();
            logger.error("Pogreška kod unosa virusa iz datoteke!", ex);
        }

        return bolestList;
    }

    public static Simptom getSimptomById(List<Simptom> simptomList, Long id) {
        return simptomList.stream()
                .filter(simptom -> id.equals(simptom.getId()))
                .findAny()
                .orElse(null);
    }

    /**
     * Služi za unos naziva i vrijednosti simptoma i kreiranje objekta te vraćanje tog objkta u niz simptoma.
     * @return vraća se novo keirani objekt simptom koji je unešen
     */
    public static List<Simptom> unosSimptoma() {
        System.out.println("Učitavanje podataka o simptomima...");
        List<Simptom> simptomiList = new ArrayList<>();
        File simptomi = new File("dat/simptomi.txt");
        try (FileReader fileReader = new FileReader(simptomi);
             BufferedReader reader = new BufferedReader(fileReader))
        {
            String linija;
            while((linija = reader.readLine()) != null) {
                Long id = Long.parseLong(linija);
                String nazivSimptoma = reader.readLine();
                String vrijednostSimptoma = reader.readLine();
                VrijednostSimptoma.valueOf(vrijednostSimptoma);
                simptomiList.add(new Simptom(id, nazivSimptoma, vrijednostSimptoma));
            }
        }
        catch (IOException ex) {
            ex.getStackTrace();
            logger.error("Pogreška kod unosa simptoma iz datoteke!", ex);
        }

        return simptomiList;
    }

    public static Zupanija getZupanijaById(List<Zupanija> zupanije, Long id) {
        return zupanije.stream()
                .filter(zupanija -> id.equals(zupanija.getId()))
                .findAny()
                .orElse(null);
    }

    /**
     * Služi za unos naziva i broja stanovnika županije i kreiranje objekta te vraćanje tog objkta u niz županija.
     * @return vraća se novo keirani objekt županije koji je unešen
     */
    public static SortedSet<Zupanija> unosZupanija() {
        System.out.println("Učitavanje podataka o županijama...");
        SortedSet<Zupanija> zupanijaSet = new TreeSet<>(new CovidSorter());
        File zupanije = new File("dat/zupanije.txt");
        try (FileReader fileReader = new FileReader(zupanije);
             BufferedReader reader = new BufferedReader(fileReader))
        {
              String linija;
              while ((linija = reader.readLine()) != null) {
                  Long id = Long.parseLong(linija);
                  String nazivZupanije = reader.readLine();
                  Integer brStanovnika = Integer.parseInt(reader.readLine());
                  Integer brZarazenih = Integer.parseInt(reader.readLine());
                  zupanijaSet.add(new Zupanija(id, nazivZupanije, brStanovnika, brZarazenih));
              }
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.error("Pogreška kod unosa županija iz datoteke!", ex);
        }

        return zupanijaSet;
    }
}