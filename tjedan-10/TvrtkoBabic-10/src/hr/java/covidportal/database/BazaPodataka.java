package hr.java.covidportal.database;

import hr.java.covidportal.model.*;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class BazaPodataka {

    private static List<Zupanija> listaZupanija = new ArrayList<>();
    private static List<Simptom> listaSimptoma = new ArrayList<>();
    private static List<Bolest> listaBolesti = new ArrayList<>();
    private static List<Osoba> listaOsoba = new ArrayList<>();

    private static Zupanija zupanija;
    private static Simptom simptom;
    private static Bolest bolest;
    private static Osoba osoba;

    public static boolean aktivnaVezaSBazomPodataka = false;

    private static final String DATABASE_CONFIG_FILE = "src/resources/database.properties";

    public static synchronized Connection connectToDatabase() throws SQLException, IOException {
        BazaPodataka.aktivnaVezaSBazomPodataka = true;
        Properties svojstva = new Properties();
        svojstva.load(new FileReader(DATABASE_CONFIG_FILE));

        String urlBazePodataka = svojstva.getProperty("bazaPodatakaUrl");
        String korisnickoIme = svojstva.getProperty("korisnickoIme");
        String lozinka = svojstva.getProperty("lozinka");

        return DriverManager.getConnection(urlBazePodataka, korisnickoIme, lozinka);
    }

    public static synchronized void closeConnectionToDatabase(Connection veza) throws SQLException {
        veza.close();
        BazaPodataka.aktivnaVezaSBazomPodataka = false;
    }

    public static List<Simptom> getSveSimptome() throws SQLException, IOException {
        Connection veza = BazaPodataka.connectToDatabase();
        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM SIMPTOM");
        List<Simptom> listaSimptoma = new ArrayList<>();

        while (rs.next()) {
            long id = rs.getLong("id");
            String naziv = rs.getString("naziv");
            String vrijednost = rs.getString("vrijednost");

            listaSimptoma.add(new Simptom(id, naziv, vrijednost));
        }

        BazaPodataka.closeConnectionToDatabase(veza);
        return listaSimptoma;
    }

    public static Simptom getSimptomById(long id) throws SQLException, IOException {
        Connection veza = connectToDatabase();
        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM SIMPTOM WHERE ID = " + id);

        rs.next();
        String naziv = rs.getString("naziv");
        String vrijednost = rs.getString("vrijednost");

        closeConnectionToDatabase(veza);
        return new Simptom(id, naziv, vrijednost);
    }

    public static void spremiNoviSimptom(Simptom noviSimptom) throws SQLException, IOException {
        Connection veza = connectToDatabase();
        PreparedStatement upit = veza.prepareStatement("INSERT INTO SIMPTOM (naziv, vrijednost) VALUES(?, ?)");

        upit.setString(1, noviSimptom.getNaziv());
        upit.setString(2, noviSimptom.getVrijednost());
        upit.executeUpdate();

        closeConnectionToDatabase(veza);
    }

    public static List<Bolest> getSveBolesti() throws SQLException, IOException {
        Connection veza = BazaPodataka.connectToDatabase();
        Statement stmt = veza.createStatement();
        ResultSet rsBolest = stmt.executeQuery("SELECT * FROM BOLEST");
        List<Bolest> listaBolesti = new ArrayList<>();
        List<List<Long>> listaListaSimptomId = new ArrayList<>();

        while (rsBolest.next()) {
            long id = rsBolest.getLong("id");
            String naziv = rsBolest.getString("naziv");
            boolean virus = rsBolest.getBoolean("virus");

            if (virus) listaBolesti.add(new Virus(id, naziv, null));
                else listaBolesti.add(new Bolest(id, naziv, null));
        }

        for (Bolest b : listaBolesti) {
            List<Long> listaSimptomId = new ArrayList<>();
            String querySimptomi = "SELECT * FROM BOLEST_SIMPTOM WHERE BOLEST_ID = " + b.getId();
            ResultSet rsSimptomi = stmt.executeQuery(querySimptomi);

            while (rsSimptomi.next()) {
                long idSimptoma = rsSimptomi.getLong("simptom_id");
                listaSimptomId.add(idSimptoma);
            }
            listaListaSimptomId.add(listaSimptomId);
        }
        BazaPodataka.closeConnectionToDatabase(veza);

        for (int i = 0; i < listaBolesti.size(); i++) {
            List<Simptom> listaSimptoma = new ArrayList<>();
            for (Long id : listaListaSimptomId.get(i)) {
                listaSimptoma.add(BazaPodatakaNiti.getSimptomById(id));
            }
            listaBolesti.get(i).setSimptomi(listaSimptoma);
        }

        return listaBolesti;
    }

    public static Bolest getBolestById(long id) throws SQLException, IOException {
        Connection veza = connectToDatabase();
        Statement stmt = veza.createStatement();
        List<Simptom> listaSimptoma = new ArrayList<>();
        List<Long> listaSimptomId = new ArrayList<>();

        ResultSet rsBolest = stmt.executeQuery("SELECT * FROM BOLEST WHERE ID = " + id);
        rsBolest.next();
        String naziv = rsBolest.getString("naziv");
        boolean virus = rsBolest.getBoolean("virus");

        ResultSet rsSimptomi = stmt.executeQuery("SELECT * FROM BOLEST_SIMPTOM WHERE BOLEST_ID = " + id);
        while (rsSimptomi.next()) {
            listaSimptomId.add(rsSimptomi.getLong("simptom_id"));
        }
        closeConnectionToDatabase(veza);

        for (long idSimptoma : listaSimptomId) {
            Simptom s = BazaPodatakaNiti.getSimptomById(idSimptoma);
            listaSimptoma.add(s);
        }

        if (virus) { return new Virus(id, naziv, listaSimptoma); }
        else { return new Bolest(id, naziv, listaSimptoma); }
    }

    public static void spremiNovuBolest(Bolest novaBolest) throws SQLException, IOException {
        boolean virus = novaBolest instanceof Virus;
        Connection veza = connectToDatabase();
        String queryBolest = "INSERT INTO BOLEST (naziv, virus) VALUES(?, ?)";
        PreparedStatement upitBolest = veza.prepareStatement(queryBolest, Statement.RETURN_GENERATED_KEYS);


        upitBolest.setString(1, novaBolest.getNaziv());
        upitBolest.setBoolean(2, virus);
        upitBolest.executeUpdate();

        ResultSet keys = upitBolest.getGeneratedKeys();
        keys.next();
        long bolestId = keys.getLong(1);

        String querySimptom = "INSERT INTO BOLEST_SIMPTOM (BOLEST_ID, SIMPTOM_ID) VALUES (?, ?)";
        PreparedStatement upitSimptom = veza.prepareStatement(querySimptom);

        for (Simptom s : novaBolest.getSimptomi()) {
            upitSimptom.setLong(1, bolestId);
            upitSimptom.setLong(2, s.getId());
            upitSimptom.executeUpdate();
        }
        closeConnectionToDatabase(veza);
    }

    public static List<Zupanija> getSveZupanije() throws SQLException, IOException {
        Connection veza = BazaPodataka.connectToDatabase();
        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ZUPANIJA");
        List<Zupanija> listaZupanija = new ArrayList<>();

        while (rs.next()) {
            long id = rs.getLong("id");
            String naziv = rs.getString("naziv");
            int brStanovnika = rs.getInt("broj_stanovnika");
            int brZarazenih = rs.getInt("broj_zarazenih_stanovnika");

            listaZupanija.add(new Zupanija(id, naziv, brStanovnika, brZarazenih));
        }

        BazaPodataka.closeConnectionToDatabase(veza);
        return listaZupanija;
    }

    public static Zupanija getZupanijaById(long id) throws SQLException, IOException {
        Connection veza = connectToDatabase();
        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM  ZUPANIJA WHERE ID = " + id);

        rs.next();
        String naziv = rs.getString("naziv");
        int brStanovnika = rs.getInt("broj_stanovnika");
        int brZarazenih = rs.getInt("broj_zarazenih_stanovnika");

        closeConnectionToDatabase(veza);
        return new Zupanija(id, naziv, brStanovnika, brZarazenih);
    }

    public static void spremiNovuZupaniju(Zupanija novaZupanija) throws SQLException, IOException {
        Connection veza = connectToDatabase();
        PreparedStatement upit = veza.prepareStatement(
                "INSERT INTO ZUPANIJA (naziv, broj_stanovnika, broj_zarazenih_stanovnika) VALUES (?, ?, ?)");

        upit.setString(1, novaZupanija.getNaziv());
        upit.setInt(2, novaZupanija.getBrStanovnika());
        upit.setInt(3, novaZupanija.getBrZarazenih());
        upit.executeUpdate();

        closeConnectionToDatabase(veza);
    }

    public static List<Osoba> getSveOsobe() throws SQLException, IOException {
        Connection veza = connectToDatabase();
        Statement stmt = veza.createStatement();
        ResultSet rsOsoba = stmt.executeQuery("SELECT * FROM OSOBA");
        List<Osoba> listaOsoba = new ArrayList<>();
        List<Long> listaOsobaId = new ArrayList<>();

        while (rsOsoba.next()) {
            long id = rsOsoba.getLong("id");
            listaOsobaId.add(id);
        }
        closeConnectionToDatabase(veza);

        for (long id : listaOsobaId) {
            Osoba osoba = BazaPodatakaNiti.getOsobaById(id);
            listaOsoba.add(osoba);
        }

        return listaOsoba;
    }

    public static Osoba getOsobaById(long id) throws SQLException, IOException {
        List<Osoba> kontaktiraneOsobe = new ArrayList<>();
        Connection veza = connectToDatabase();
        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM KONTAKTIRANE_OSOBE WHERE OSOBA_ID = " + id);

        while (rs.next()) {
            long idKontakta = rs.getLong("kontaktirana_osoba_id");
            kontaktiraneOsobe.add(getKontaktById(idKontakta));
        }

        closeConnectionToDatabase(veza);

        Osoba osoba = getKontaktById(id);
        osoba.setKontaktiraneOsobe(kontaktiraneOsobe);

        return osoba;
    }

    public static Osoba getKontaktById(long id) throws SQLException, IOException {
        Connection veza = connectToDatabase();
        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM OSOBA WHERE ID = " + id);

        rs.next();
        String ime = rs.getString("ime");
        String prezime = rs.getString("prezime");
        LocalDate datumRodjenja = rs.getDate("datum_rodjenja").toLocalDate();
        long zupanijaID = rs.getLong("zupanija_id");
        long bolestID = rs.getLong("bolest_id");

        closeConnectionToDatabase(veza);

        Zupanija zupanija = BazaPodatakaNiti.getZupanijaById(zupanijaID);
        Bolest bolest = BazaPodatakaNiti.getBolestById(bolestID);

        return new Osoba.Builder(id)
                .withIme(ime)
                .withPrezime(prezime)
                .withDatumRodjenja(datumRodjenja)
                .withZupanija(zupanija)
                .withBolescu(bolest)
                .build();
    }

    public static void spremiNovuOsobu(Osoba novaOsoba) throws SQLException, IOException {
        Connection veza = connectToDatabase();
        String queryOsoba = "INSERT INTO OSOBA(ime, prezime, datum_rodjenja, zupanija_id, bolest_id) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement upitOsoba = veza.prepareStatement(queryOsoba, Statement.RETURN_GENERATED_KEYS);

        upitOsoba.setString(1, novaOsoba.getIme());
        upitOsoba.setString(2, novaOsoba.getPrezime());
        upitOsoba.setDate(3,  Date.valueOf(novaOsoba.getDatumRodenja()));
        upitOsoba.setLong(4, novaOsoba.getZupanija().getId());
        upitOsoba.setLong(5, novaOsoba.getZarazenBolescu().getId());
        upitOsoba.executeUpdate();

        if (!novaOsoba.getKontaktiraneOsobe().isEmpty()) {
            ResultSet keys = upitOsoba.getGeneratedKeys();
            keys.next();
            long novaOsobaId = keys.getLong(1);
            String queryKontakt = "INSERT INTO KONTAKTIRANE_OSOBE(osoba_id, kontaktirana_osoba_id) VALUES(?, ?)";
            PreparedStatement upitKontakt = veza.prepareStatement(queryKontakt);

            for (Osoba k : novaOsoba.getKontaktiraneOsobe()) {
                upitKontakt.setLong(1, novaOsobaId);
                upitKontakt.setLong(2, k.getId());
                upitKontakt.executeUpdate();
            }
        }
        closeConnectionToDatabase(veza);
    }

    public static List<Bolest> getSamoBolesti() {
        List<Bolest> listaBolesti = BazaPodatakaNiti.getSveBolesti();
        return  listaBolesti.stream()
                .filter(b -> !(b instanceof Virus))
                .collect(Collectors.toList());
    }

    public static List<Virus> getSamoVirusi()  {
        List<Bolest> listaBolesti = BazaPodatakaNiti.getSveBolesti();
        return listaBolesti.stream()
                .filter(v -> v instanceof Virus)
                .map(v -> (Virus)v)
                .collect(Collectors.toList());
    }


    // getteri i setteri
    public static List<Zupanija> getListaZupanija() {
        return listaZupanija;
    }

    public static void setListaZupanija(List<Zupanija> listaZupanija) {
        BazaPodataka.listaZupanija = listaZupanija;
    }

    public static List<Simptom> getListaSimptoma() {
        return listaSimptoma;
    }

    public static void setListaSimptoma(List<Simptom> listaSimptoma) {
        BazaPodataka.listaSimptoma = listaSimptoma;
    }

    public static List<Bolest> getListaBolesti() {
        return listaBolesti;
    }

    public static void setListaBolesti(List<Bolest> listaBolesti) {
        BazaPodataka.listaBolesti = listaBolesti;
    }

    public static List<Osoba> getListaOsoba() {
        return listaOsoba;
    }

    public static void setListaOsoba(List<Osoba> listaOsoba) {
        BazaPodataka.listaOsoba = listaOsoba;
    }

    public static Zupanija getZupanija() {
        return zupanija;
    }

    public static void setZupanija(Zupanija zupanija) {
        BazaPodataka.zupanija = zupanija;
    }

    public static Simptom getSimptom() {
        return simptom;
    }

    public static void setSimptom(Simptom simptom) {
        BazaPodataka.simptom = simptom;
    }

    public static Bolest getBolest() {
        return bolest;
    }

    public static void setBolest(Bolest bolest) {
        BazaPodataka.bolest = bolest;
    }

    public static Osoba getOsoba() {
        return osoba;
    }

    public static void setOsoba(Osoba osoba) {
        BazaPodataka.osoba = osoba;
    }
}
