package hr.java.covidportal.main;

import hr.java.covidportal.enumeracije.VrijednostSimptoma;
import hr.java.covidportal.iznimke.BolestIstihSimptoma;
import hr.java.covidportal.iznimke.BrojKontaktiranihOsoba;
import hr.java.covidportal.iznimke.DuplikatKontaktiraneOsobe;
import hr.java.covidportal.model.*;
import hr.java.covidportal.sort.CovidSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketImpl;
import java.util.*;
import java.util.stream.Collectors;

/**
 *  Služi za unos i ispis Osoba, njihovih bolesti i simptoma te podataka o osobama kao što su županija stanovanja
 *  broj stanovnika te županije, ime, prezime i starost osobe i s kojim je osobama ta osoba bila u kontaktu.
 */

public class Glavna {
    private static final Logger logger = LoggerFactory.getLogger(Glavna.class);

    private static Integer BR_ZUPANIJA;
    private static Integer BR_SIMPTOMA;
    private static Integer BR_BOLESTI;
    private static Integer BR_OSOBA;

    /**
     * Služi za pokretanje programa koji će od korisnika tražiti unos Osoba i podatcima o toj osobi,
     * Bolesti, Virusa, Simptoma. Unosi se testirju i podatci zapisuju u log.
     * @param args argumenti komandne linije se ne koriste.
     */

    public static void main(String[] args) {
        logger.info("Pokenuta je aplikacija.");

        Scanner scanner = new Scanner(System.in);

        SortedSet<Zupanija> setZupanije = new TreeSet<>(new CovidSorter());
        Set<Simptom> setSimptomi = new HashSet<>();
        Set<Bolest> setBolesti = new HashSet<>();
        List<Osoba> listaOsoba = new ArrayList<>();

        System.out.print("Unesite broj županija koje želite unijeti: ");
        BR_ZUPANIJA = scanner.nextInt();
        scanner.nextLine();
        for(int i = 0; i < BR_ZUPANIJA; i++) {
            setZupanije.add(unosZupanije(scanner, i));
        }

        System.out.print("Unesite broj simptoma koje želite unijeti: ");
        BR_SIMPTOMA = scanner.nextInt();
        scanner.nextLine();
        for(int i = 0; i < BR_SIMPTOMA; i++) {
            setSimptomi.add(unosSimptoma(scanner, i));
        }

        List<Simptom> listaSimptomi = new ArrayList<>(setSimptomi);

        listaSimptomi = listaSimptomi.stream().sorted(Comparator.comparing(Simptom::getNaziv)).collect(Collectors.toList());
        listaSimptomi.forEach(System.out::print);

        System.out.print("Unesite broj bolesti koje želite unijeti: ");
        int brBolesti = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Unesite broj virusa koje želite unijeti: ");
        int brVirusa = scanner.nextInt();
        scanner.nextLine();
        BR_BOLESTI = brBolesti + brVirusa;
        for(int i = 0; i < BR_BOLESTI; i++) {
            Bolest bolest = unosBolesti(scanner, setSimptomi, i);
            try {
                provjeraBolestIstihSimptoma(setBolesti, bolest);
                setBolesti.add(bolest);
            }
            catch (BolestIstihSimptoma ex1) {
                System.out.println(ex1.getMessage());
                logger.error("Unesena bolest sa istim sipmtomima kao i jedna od prošlih bolesti.", ex1);
                i--;
            }
        }

        System.out.print("Unesite broj osoba koje želite unijeti: ");
        BR_OSOBA = scanner.nextInt();
        scanner.nextLine();
        for(int i = 0; i < BR_OSOBA; i++) {
            listaOsoba.add(unosOsobe(scanner, setZupanije, setBolesti, listaOsoba, i));
        }

        System.out.println("Popis osoba: ");
        for(Osoba osoba : listaOsoba){
            osoba.ispisOsobe();
        }

        Map<Bolest, List<Osoba>> mapaBolesneOsobe = dodajBolesneOsobe(setBolesti, listaOsoba);
        ispisBolesnihOsoba(mapaBolesneOsobe);

        System.out.println("Najveći postotak zaraženosti je u županiji: "
                + setZupanije.last().getNaziv() + ": " + setZupanije.last().getPostotakZarazenih() + "%.");

        logger.info("Kraj programa.");
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

    /**
     * Kreira mapu baziranu na bolestima i osobama tako da za svaku bolest postoji lista osoba koja boluje od nje
     * @param setBolesti skup bolesti i virusa
     * @param listaOsoba lista osoba
     * @return mapa bolesti i osoba
     */
    public static Map<Bolest, List<Osoba>> dodajBolesneOsobe(Set<Bolest> setBolesti, List<Osoba> listaOsoba) {
        Map<Bolest, List<Osoba>> mapaBolesneOsobe = new HashMap<>();
        for(int i = 0; i < BR_BOLESTI; i++) {
            Bolest bolest = getBolestAtIndex(setBolesti, i);
            List<Osoba> listaBolesnihOsoba = new ArrayList<>();
            for(int j = 0; j < BR_OSOBA; j++) {
                Osoba osoba = listaOsoba.get(j);
                if(osoba.getZarazenBolescu().equals(bolest))
                    listaBolesnihOsoba.add(osoba);
            }
            mapaBolesneOsobe.put(bolest, listaBolesnihOsoba);
        }
        return mapaBolesneOsobe;
    }

    /**
     * Služi za unos osobe i svih podataka koje pripadaju toj osobi.
     * @param scanner služi za unos podataka
     * @param zupanije polje županija u kojem su osobe
     * @param bolesti polje bolesti kojima osoba može biti zaražena
     * @param osobe polje unesenih osoba
     * @param i indeks osobe koju je trenutno potrebno unesti
     * @return povratna vrijednost je objek Osoba koji se uneso
     */
    public static Osoba unosOsobe(Scanner scanner, Set<Zupanija> zupanije, Set<Bolest> bolesti, List<Osoba> osobe, int i) {
        int odabirZupanije = 0;
        int odabirBolesti = 0;
        int brKontaktiranihOsoba = 0;
        boolean nastaviPetlju;

        System.out.print("Unesi ime osobe " + (i + 1) + ": ");
        String ime = scanner.nextLine();
        System.out.print("Unesi prezime osobe: ");
        String prezime = scanner.nextLine();
        Integer starost = 0;
        do {
            try {
                System.out.print("Unesi starost osobe: ");
                starost = scanner.nextInt();
                scanner.nextLine();
                logger.info("Unešena je starost osobe: " + starost);
                nastaviPetlju = false;
            }
            catch (InputMismatchException ex1) {
                System.out.println("Molimo unesite brojčanu vrijednost.");
                scanner.nextLine();
                logger.error("Pogreška kod unosa starosti osobe jer je unešen String umjesto broja!", ex1);
                nastaviPetlju = true;
            }
        } while (nastaviPetlju);

        // Županije
        System.out.println("Unesi županiju prebivališta osobe: ");
        for(int j = 0; j < BR_ZUPANIJA; j++) {
            Zupanija zupanija = getZupanijaAtIndex(zupanije, j);
            System.out.println( (j + 1) + ". " + zupanija.getNaziv()
                    + " (" + zupanija.getBrStanovnika() + ") ");
        }
        do {
            try {
                System.out.print("Odabir: ");
                odabirZupanije = scanner.nextInt();
                scanner.nextLine();
                logger.info("Odabrana je županija: " + odabirZupanije);
                nastaviPetlju = false;
            }
            catch (InputMismatchException ex1) {
                System.out.println("Molimo unesite brojčanu vrijednost.");
                scanner.nextLine();
                logger.error("Pogreška kod odabira županije jer je unešen String umjesto broja!", ex1);
                nastaviPetlju = true;
            }
            if(odabirZupanije < 1 || odabirZupanije > BR_ZUPANIJA) {
                System.out.println("Neispravan unos. Pokušajte ponovo.");
                logger.info("Odabran je broj koji nije ponuđen.");
            }
        } while (odabirZupanije < 1 || odabirZupanije > BR_ZUPANIJA || nastaviPetlju);

        // Bolesti
        System.out.println("Unesi bolest osobe: ");
        for(int j = 0; j < BR_BOLESTI; j++) {
            System.out.println( (j + 1) + ". " + getBolestAtIndex(bolesti, j).getNaziv());
        }
        do {
            try {
                System.out.print("Odabir: ");
                odabirBolesti = scanner.nextInt();
                scanner.nextLine();
                logger.info("Odabrana je bolest: " + odabirBolesti);
                nastaviPetlju = false;
            }
            catch (InputMismatchException ex1) {
                System.out.println("Molimo unesite brojčanu vrijednost.");
                scanner.nextLine();
                logger.error("Pogreška kod odabira bolesti jer je unešen String umjesto broja!", ex1);
                nastaviPetlju = true;
            }
            if(odabirBolesti < 1 || odabirBolesti > BR_BOLESTI) {
                System.out.println("Neispravan unos. Pokušajte ponovo.");
                logger.info("Odabran je broj koji nije ponuđen.");
            }
        } while(odabirBolesti < 1 || odabirBolesti > BR_BOLESTI || nastaviPetlju);

        // Kontakti
        if(i > 0) {
            do {
                try {
                    System.out.print("Unesi broj osoba koje su bile u kontaktu s tom osobom (" + prezime + "): ");
                    brKontaktiranihOsoba = scanner.nextInt();
                    scanner.nextLine();
                    logger.info("Broj kontakata je: " + brKontaktiranihOsoba);
                    nastaviPetlju = false;
                }
                catch (InputMismatchException ex1) {
                    System.out.println("Molimo unesite brojčanu vrijednost.");
                    scanner.nextLine();
                    logger.error("Pogreška kod broja kontakata jer je unešen String umjesto broja!", ex1);
                    nastaviPetlju = true;
                }
                if (brKontaktiranihOsoba > i || brKontaktiranihOsoba < 0) {
                    System.out.println("Neispravan unos, pokušajte ponovo.");
                    logger.info("Odabran je broj koji nije u dozvoljenom dometu.");
                }
            } while (nastaviPetlju || brKontaktiranihOsoba > i || brKontaktiranihOsoba < 0);

            List<Osoba> kontaktiraneOsobe = odabirKontaktiranihOsoba(scanner, osobe, i, prezime, brKontaktiranihOsoba);

            return new Osoba.Builder(ime)
                    .withPrezime(prezime)
                    .withStarost(starost)
                    .withZupanija(getZupanijaAtIndex(zupanije, odabirZupanije - 1))
                    .withBolescu(getBolestAtIndex(bolesti, odabirBolesti - 1))
                    .withKontakti(kontaktiraneOsobe)
                    .build();
        } else {
            return new Osoba.Builder(ime)
                    .withPrezime(prezime)
                    .withStarost(starost)
                    .withZupanija(getZupanijaAtIndex(zupanije, odabirZupanije - 1))
                    .withBolescu(getBolestAtIndex(bolesti, odabirBolesti - 1))
                    .build();
        }
    }

    /**
     * Služi za odabir prijeunešene osobe za osobu s kojom je trenutna osoba bila u kontaktu
     * @param scanner služi za unos podataka
     * @param osobe niz svih unesenih osoba
     * @param i index trenutne osobe
     * @param prezime prezime trenutne osobe
     * @param brKontaktiranihOsoba broj osoba s kojim je trenutna osoba bila u kontaktu
     * @return povratna vrijednost je niz osoba koje su odabrane kao kontakti osobe
     */
    public static List<Osoba> odabirKontaktiranihOsoba(Scanner scanner, List<Osoba> osobe, int i, String prezime
                                                       , Integer brKontaktiranihOsoba)
    {
        System.out.println("Unesite osobe koje su bile u kontaktu s tom osobom (" + prezime + "): ");
        boolean nastaviPetlju;
        int indexKontakta = 1;
        List<Osoba> kontaktiraneOsobe = new ArrayList<>();

        for (int j = 0; j < brKontaktiranihOsoba; j++) {
            do {
                try {
                    System.out.println("Odaberite " + (j + 1) + ". osobu: ");
                    for (int l = 0; l < i; l++) {
                        System.out.println((l + 1) + ". " + osobe.get(l).getIme() + " " + osobe.get(l).getPrezime());
                    }
                    System.out.print("Odabir: ");
                    indexKontakta = scanner.nextInt();
                    provjeraBrKontaktiranihOsoba(indexKontakta, i);
                    logger.info("Odabran kontakt ima index: " + indexKontakta);
                    indexKontakta--;
                    scanner.nextLine();
                    nastaviPetlju = false;
                } catch (InputMismatchException ex1) {
                    System.out.println("Molimo unesite brojčanu vrijednost.");
                    scanner.nextLine();
                    logger.error("Pogreška kod odabira kontakta jer je unešen String umjesto broja!", ex1);
                    nastaviPetlju = true;
                } catch (BrojKontaktiranihOsoba ex2) {
                    System.out.println(ex2.getMessage());
                    scanner.nextLine();
                    logger.error(ex2.getMessage());
                    nastaviPetlju = true;
                }
            } while (nastaviPetlju);

            Osoba kontakt = osobe.get(indexKontakta);
            try {
                logger.info("Odabran je kontakt: " + kontakt.getPrezime());
                provjeraKontaktiranihOsoba(kontaktiraneOsobe, kontakt);
                kontaktiraneOsobe.add(kontakt);
            }
            catch (DuplikatKontaktiraneOsobe ex1) {
                System.out.println(ex1.getMessage());
                logger.error("Pogreška kod odabira kontakta. " +
                        "Odabrana osoba se već nalazi među kontaktiranim osobama!", ex1);
                j--;
            }
        }

        return kontaktiraneOsobe;
    }

    /**
     * Uspoređuje se index kontaktirane osobe sa brojem trenutno unešenih osoba. Provjerava se da nije uneše broj
     * isod dopuštenog. Baca se iznimka <code>BrojKontaktiranihOsoba</code> ako je unos izvan granica.
     * @param indexKontakta index na kojem se nalazi osoba kontakt
     * @param i index trenutne osobe
     * @throws BrojKontaktiranihOsoba iznimka koja se baca u slučaju kad je odabir kontaktirane osobe izvan
     * dopuštene granice
     */
    public static void provjeraBrKontaktiranihOsoba (int indexKontakta, int i) throws BrojKontaktiranihOsoba {
        if (indexKontakta > i || indexKontakta < 1){
            throw new BrojKontaktiranihOsoba("Odabir je izvan granice ponuđenih brojeva.");
        }
    }

    /**
     * Služi za provjeru kontakata kako se nebi ponovio unos
     * @param osobe lista prije unešenih osoba
     * @param noviKontakt novo izabrana osoba
     * @throws DuplikatKontaktiraneOsobe baca inzimku u slučaju kad je novo odabrana osoba već u listi
     */
    public static void provjeraKontaktiranihOsoba (List<Osoba> osobe, Osoba noviKontakt)
            throws DuplikatKontaktiraneOsobe {

        if (osobe.contains(noviKontakt)) {
            throw new DuplikatKontaktiraneOsobe("Odabrana osoba se već nalazi među kontaktiranim osobama.");
        }
    }

    /**
     * Služi za unos bolesti ili virusa i odabir simptoma koja ta bolest ili virus ima iz niza prije unešenih simptoma.
     * @param scanner služi za unos podataka
     * @param simptomi niz simptoma koje bolest može poprimiti
     * @param i index trenutne bolesti
     * @return povratna vrijednost je objek Bolest koji je unesen
     */
    public static Bolest unosBolesti(Scanner scanner, Set<Simptom> simptomi, int i) {
        int odabirBolestVirus = 0;
        boolean nastaviPetlju;
        do {
            try {
                System.out.println("Unosite li bolest ili virus?\n1) BOLEST\n2) VIRUS");
                System.out.print("Odabir: ");
                odabirBolestVirus = scanner.nextInt();
                scanner.nextLine();
                logger.info("Odabran/a je bolest(1)/virus(2): " + odabirBolestVirus);
                nastaviPetlju = false;
            }
            catch (InputMismatchException ex1) {
                System.out.println("Molimo unesite brojčanu vrijednost.");
                scanner.nextLine();
                logger.error("Pogreška kod odabira virusa/bolesti jer je unešen String umjesto broja!", ex1);
                nastaviPetlju = true;
            }
            if(odabirBolestVirus < 1 || odabirBolestVirus > 2) {
                System.out.println("Neispravan unos, pokušajte ponovo.");
                logger.info("Odabran je broj koji nije ponuđen.");
            }
        } while (odabirBolestVirus < 1 || odabirBolestVirus > 2 || nastaviPetlju);

        System.out.print("Unesi naziv " + (i + 1) + ". bolesti ili virusa: ");
        String naziv = scanner.nextLine();

        // Unos Broja Simptoma
        int brSimptomaBolesti = 0;
        do {
            try {
                System.out.print("Unesi broj simptoma: ");
                brSimptomaBolesti = scanner.nextInt();
                logger.info("Broj simptoma bolesti: " + brSimptomaBolesti);
                nastaviPetlju = false;
            }
            catch (InputMismatchException ex1) {
                System.out.println("Molimo unesite brojčanu vrijednost.");
                scanner.nextLine();
                logger.error("Pogreška kod unosa broja simptoma jer je unešen String umjesto broja!", ex1);
                nastaviPetlju = true;
            }
            if(brSimptomaBolesti < 1 || brSimptomaBolesti > BR_SIMPTOMA) {
                System.out.println("Neispravan odabir. Pokušajte ponovo.");
                logger.info("Odabran je broj koji nije ponuđen.");
            }
        } while (brSimptomaBolesti < 1 || brSimptomaBolesti > BR_SIMPTOMA || nastaviPetlju);
        scanner.nextLine();

        // Unos Simptoma
        Set<Simptom> simptomiBolesti = new HashSet<>();
        for(int j = 0; j < brSimptomaBolesti; j++) {
            Integer indexSimptoma = indexSimptomaBolesti(scanner, simptomi, naziv);
            simptomiBolesti.add(getSimptomAtIndex(simptomi, indexSimptoma));
            scanner.nextLine();
        }

        if(odabirBolestVirus == 1)
            return new Bolest(naziv, simptomiBolesti);
        return new Virus(naziv, simptomiBolesti);
    }

    /**
     *  Služi za odabir simptoma za bolesti ili viruse koji se unose u program.
     * @param scanner služi za unos podataka
     * @param simptomi niz simptoma od kojih se bolest može sastojati
     * @param naziv naziv unesene bolesti ili virusa
     * @return povratna vrijednost je Integer unešenog odabira simptoma
     */
    public static Integer indexSimptomaBolesti(Scanner scanner, Set<Simptom> simptomi, String naziv) {
        int odabirSimptoma = 0;
        boolean nastaviPetlju;
        do {
            System.out.println("Odaberite redni broj simptoma (" + naziv + "): ");
            for(int k = 0; k < BR_SIMPTOMA; k++) {
                Simptom simptom = getSimptomAtIndex(simptomi, k);
                System.out.println((k + 1) + ". " + simptom.getNaziv() + " " + simptom.getVrijednost());
            }

            try {
                System.out.print("Odabir: ");
                odabirSimptoma = scanner.nextInt();
                logger.info("Odabran je simptom indexa: " + odabirSimptoma);
                nastaviPetlju = false;
            }
            catch (InputMismatchException ex1) {
                System.out.println("Molimo unesite brojčanu vrijednost.");
                scanner.nextLine();
                logger.error("Pogreška kod unosa broja simptoma jer je unešen String umjesto broja!", ex1);
                nastaviPetlju = true;
            }
            if(odabirSimptoma < 1 || odabirSimptoma > BR_SIMPTOMA) {
                System.out.println("Neispravan odabir. Odaberite jedan od ponuđenih odgovora.");
                logger.info("Odabran je broj koji nije ponuđen.");
            }
        } while(odabirSimptoma < 1 || odabirSimptoma > BR_SIMPTOMA || nastaviPetlju);

        return odabirSimptoma - 1;
    }

    /**
     * Provjerava unešenu bolest ili virus sa nizom prije unešenih virusa i bolesti kako ne bi imali iste simptome. Ako imaju
     * iste simptome baca se iznimka <code>BolestIstihSimptoma</code>.
     * @param bolesti niz bolesti i virus unesenih u program
     * @param novaBolest index na kojem se nalazi trenutna bolest ili virus za koju je potrebna provjera.
     * @throws BolestIstihSimptoma iznimka koja se baca u slučaju kad je unesena bolest ili virus sa istim simptomima
     * koje je imala prije unešena bolest ili virus
     */
    public static void provjeraBolestIstihSimptoma(Set<Bolest> bolesti, Bolest novaBolest)
            throws BolestIstihSimptoma {

        Set<Simptom> simptomi = novaBolest.getSimptomi();
        for(Bolest bolest : bolesti) {
            Set<Simptom> tempSimptomi = bolest.getSimptomi();
            if (tempSimptomi.containsAll(simptomi) && tempSimptomi.size() == simptomi.size())
                throw new BolestIstihSimptoma("Već ste unijeli bolest ili virus s istim simptomima. " +
                        "Molimo ponovite unos.");
        }
    }

    /**
     * Služi za unos naziva i vrijednosti simptoma i kreiranje objekta te vraćanje tog objkta u niz simptoma.
     * @param scanner služi za unos podataka
     * @param i index trenutnog simptoma u nizu
     * @return vraća se novo keirani objekt simptom koji je unešen
     */
    public static Simptom unosSimptoma(Scanner scanner, int i) {
        System.out.print("Unesi naziv simptoma " + (i + 1) + ": ");
        String naziv = scanner.nextLine();
        boolean nastaviPetlju;
        String vrijednost = "";

        do {
            try {
                System.out.print("Unesi učestalost simptoma (RIJETKO, SREDNJE ili CESTO): ");
                vrijednost = scanner.nextLine();
                VrijednostSimptoma.valueOf(vrijednost);
                nastaviPetlju = false;
            } catch (IllegalArgumentException ex1) {
                logger.error("Pogreška kod unosa simptoma!", ex1);
                System.out.println("Molimo unesite ponuđenu vrijednost.");
                nastaviPetlju = true;
            }
        } while (nastaviPetlju);

        return new Simptom(naziv, vrijednost);
    }

    /**
     * Služi za unos naziva i broja stanovnika županije i kreiranje objekta te vraćanje tog objkta u niz županija.
     * @param scanner služi za unos podataka
     * @param i index trenutne županije u nizu
     * @return vraća se novo keirani objekt županije koji je unešen
     */
    public static Zupanija unosZupanije(Scanner scanner, int i) {
        System.out.print("Unesi naziv županije " + (i + 1) + ": ");
        String naziv = scanner.nextLine();

        int brStanovnika=0;
        int brZarazenih=0;
        boolean nastaviPetlju;
        do {
            try {
                System.out.print("Unesi broj stanovnika (" + naziv + "): ");
                brStanovnika = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Unesi broj zaraženih stanovnika: ");
                brZarazenih = scanner.nextInt();
                scanner.nextLine();
                logger.info("Unesen je broj stanovnika: " + brStanovnika);
                logger.info("Unesen je broj zaraženih stanovnika: " + brZarazenih);
                nastaviPetlju = false;
            }
            catch (InputMismatchException ex1) {
                logger.error("Pogreška kod unosa broja stanovnika jer je unešen String umjesto broja!", ex1);
                System.out.println("Molimo unesite brojčanu vrijednost.");
                scanner.nextLine();
                nastaviPetlju = true;
            }
        } while (nastaviPetlju);

        return new Zupanija(naziv, brStanovnika, brZarazenih);
    }

    public static Simptom getSimptomAtIndex (Set<Simptom> simptomi, int index) {
        int i = 0;
        for (Simptom simptom : simptomi) {
            if (i == index)
                return simptom;
            i++;
        }
        return null;
    }

    public static Bolest getBolestAtIndex (Set<Bolest> bolesti, int index) {
        int i = 0;
        for (Bolest bolest : bolesti) {
            if (i == index)
                return bolest;
            i++;
        }
        return null;
    }

    public static Zupanija getZupanijaAtIndex (Set<Zupanija> zupanije, int index) {
        int i = 0;
        for (Zupanija zupanija : zupanije) {
            if (i == index)
                return zupanija;
            i++;
        }
        return null;
    }
}