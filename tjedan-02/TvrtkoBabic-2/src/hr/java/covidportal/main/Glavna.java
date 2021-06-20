package hr.java.covidportal.main;

import hr.java.covidportal.model.*;

import java.util.Scanner;

public class Glavna {

    private static final Integer BR_ZUPANIJA = 3;
    private static final Integer BR_SIMPTOMA = 3;
    private static final Integer BR_BOLESTI = 4;
    private static final Integer BR_OSOBA = 3;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Zupanija[] zupanije = new Zupanija[BR_ZUPANIJA];
        Simptom[] simptomi = new Simptom[BR_SIMPTOMA];
        Bolest[] bolesti = new Bolest[BR_BOLESTI];
        Osoba[] osobe = new Osoba[BR_OSOBA];

        for(int i = 0; i < BR_ZUPANIJA; i++) {
            zupanije[i] = unosZupanije(scanner, i);
        }

        for(int i = 0; i < BR_SIMPTOMA; i++) {
            simptomi[i] = unosSimptoma(scanner, i);
        }

        for(int i = 0; i < BR_BOLESTI; i++) {
            bolesti[i] = unosBolesti(scanner, simptomi, i);
        }

        for(int i = 0; i < BR_OSOBA; i++) {
            osobe[i] = unosOsobe(scanner, zupanije, bolesti, osobe, i);
        }

        System.out.println("Popis osoba: ");
        for(int i = 0; i < BR_OSOBA; i++){
            osobe[i].ispisOsobe();
        }
    }

    public static Osoba unosOsobe(Scanner scanner, Zupanija[] zupanije, Bolest[] bolesti, Osoba[] osobe, int i) {
        Integer odabirZupanije;
        Integer odabirBolesti;
        Integer brKontaktiranihOsoba;

        System.out.print("Unesi ime osobe " + (i + 1) + ": ");
        String ime = scanner.nextLine();
        System.out.print("Unesi prezime osobe: ");
        String prezime = scanner.nextLine();
        System.out.print("Unesi starost osobe: ");
        Integer starost = scanner.nextInt();
        scanner.nextLine();

        // Županije
        System.out.println("Unesi županiju prebivališta osobe: ");
        for(int j = 0; j < BR_ZUPANIJA; j++) {
            System.out.println( (j + 1) + ". " + zupanije[j].getNaziv()
                    + " (" + zupanije[j].getBrStanovnika() + ") ");
        }
        do {
            System.out.print("Odabir: ");
            odabirZupanije = scanner.nextInt();
            if(odabirZupanije < 1 || odabirZupanije > BR_ZUPANIJA)
                System.out.println("Neispravan unos. Pokušajte ponovo.");
        } while(odabirZupanije < 1 || odabirZupanije > BR_ZUPANIJA);

        // Bolesti
        System.out.println("Unesi bolest osobe: ");
        for(int j = 0; j < BR_BOLESTI; j++) {
            System.out.println( (j + 1) + ". " + bolesti[j].getNaziv());
        }
        do {
            System.out.print("Odabir: ");
            odabirBolesti = scanner.nextInt();
            scanner.nextLine();
            if(odabirBolesti < 1 || odabirBolesti > BR_BOLESTI)
                System.out.println("Neispravan unos. Pokušajte ponovo.");
        } while(odabirBolesti < 1 || odabirBolesti > BR_BOLESTI);

        // Kontakti
        if(i > 0) {
            System.out.print("Unesi broj osoba koje su bile u kontaktu s tom osobom (" + prezime + "): ");
            brKontaktiranihOsoba = scanner.nextInt();

            Osoba[] kontaktiraneOsobe = new Osoba[brKontaktiranihOsoba];

            //Odabir kontaktiranih osoba
            kontaktiraneOsobe = odabirKontaktiranihOsoba(scanner, osobe, i, prezime,
                                                kontaktiraneOsobe, brKontaktiranihOsoba);

            return new Osoba.Builder(ime)
                    .withPrezime(prezime)
                    .withStarost(starost)
                    .withZupanija(zupanije[odabirZupanije-1])
                    .withBolescu(bolesti[odabirBolesti-1])
                    .withKontakti(kontaktiraneOsobe)
                    .build();
        } else {
            return new Osoba.Builder(ime)
                    .withPrezime(prezime)
                    .withStarost(starost)
                    .withZupanija(zupanije[odabirZupanije-1])
                    .withBolescu(bolesti[odabirBolesti-1])
                    .build();
        }
    }

    public static Osoba[] odabirKontaktiranihOsoba(Scanner scanner, Osoba[] osobe, int i, String prezime,
                                                   Osoba[] kontaktiraneOsobe, Integer brKontaktiranihOsoba)
    {
        System.out.println("Unesite osobe koje su bile u kontaktu s tom osobom (" + prezime + "): ");

        for (int j = 0; j < brKontaktiranihOsoba; j++) {
            System.out.println("Odaberite " + (j + 1) + ". osobu: ");
            for (int l = 0; l < i; l++) {
                System.out.println((l + 1) + ". " + osobe[l].getIme() + " " + osobe[l].getPrezime());
            }
            System.out.print("Odabir: ");
            kontaktiraneOsobe[j] = osobe[scanner.nextInt() - 1];
            scanner.nextLine();
        }
        return kontaktiraneOsobe;
    }

    public static Bolest unosBolesti(Scanner scanner, Simptom[] simptomi, int i) {
        System.out.println("Unosite li bolest ili virus?\n1) BOLEST\n2) VIRUS");
        System.out.print("Odabir: ");
        int odabirBolestVirus = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Unesi naziv " + (i + 1) + ". bolesti ili virusa: ");
        String naziv = scanner.nextLine();

        // Unos Broja Simptoma
        Integer brSimptomaBolesti;
        do {
            System.out.print("Unesi broj simptoma: ");
            brSimptomaBolesti = scanner.nextInt();
            if(brSimptomaBolesti < 1 || brSimptomaBolesti > BR_SIMPTOMA)
                System.out.println("Neispravan odabir. Pokušajte ponovo.");
        } while (brSimptomaBolesti < 1 || brSimptomaBolesti > BR_SIMPTOMA);
        scanner.nextLine();

        // Unos Simptoma
        Simptom[] simptomiBolesti = new Simptom[brSimptomaBolesti];
        for(int j = 0; j < brSimptomaBolesti; j++) {
            Integer indexSimptoma = indexSimptomaBolesti(scanner, simptomi, naziv);

            simptomiBolesti[j] = simptomi[indexSimptoma];
            scanner.nextLine();
        }

        if(odabirBolestVirus == 1)
            return new Bolest(naziv, simptomiBolesti);
        return new Virus(naziv, simptomiBolesti);
    }

    public static Integer indexSimptomaBolesti(Scanner scanner, Simptom[] simptomi, String naziv) {
        Integer odabirSimptoma = 0;
        do {
            System.out.println("Odaberite redni broj simptoma (" + naziv + "): ");
            for(int k = 0; k < BR_SIMPTOMA; k++) {
                System.out.println((k + 1) + ". " + simptomi[k].getNaziv() + " " + simptomi[k].getVrijednost());
            }
            System.out.print("Odabir: ");
            odabirSimptoma = scanner.nextInt();

            if(odabirSimptoma < 1 || odabirSimptoma > BR_SIMPTOMA)
                System.out.println("Neispravan odabir. Odaberite jedan od ponuđenih odgovora.");
        } while(odabirSimptoma < 1 || odabirSimptoma > BR_SIMPTOMA);

        return odabirSimptoma - 1;
    }

    public static Simptom unosSimptoma(Scanner scanner, int i) {
        System.out.print("Unesi naziv simptoma " + (i + 1) + ": ");
        String naziv = scanner.nextLine();
        System.out.print("Unesi učestalost simptoma (RIJETKO, SREDNJE ili ČESTO): ");
        String vrijednost = scanner.nextLine();
        return new Simptom(naziv, vrijednost);
    }

    public static Zupanija unosZupanije(Scanner scanner, int i) {
        System.out.print("Unesi naziv županije " + (i + 1) + ": ");
        String naziv = scanner.nextLine();
        System.out.print("Unesi broj stanovnika (" + naziv + "): ");
        Integer brStanovnika = scanner.nextInt();
        scanner.nextLine();
        return new Zupanija(naziv, brStanovnika);
    }
}
