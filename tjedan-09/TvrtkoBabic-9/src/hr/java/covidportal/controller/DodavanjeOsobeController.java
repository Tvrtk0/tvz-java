package hr.java.covidportal.controller;

import hr.java.covidportal.database.BazaPodataka;
import hr.java.covidportal.model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class DodavanjeOsobeController implements Initializable {
    private static List<Zupanija> procitaneZupanije;
    private static List<Bolest> procitaneBolesti;
    private static List<Osoba> procitaneOsobe;
    private static List<Long> izabranKontakt = new ArrayList<>();

    @FXML
    private TextField imeText;
    @FXML
    private TextField prezimeText;
    @FXML
    private DatePicker datumRodjenjaDatePicker;
    @FXML
    private ChoiceBox<String> zupanijeChoiceBox = new ChoiceBox<>();
    @FXML
    private ChoiceBox<String> bolestiChoiceBox = new ChoiceBox<>();
    @FXML
    private MenuButton kontaktiMenuButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            procitaneZupanije = BazaPodataka.getSveZupanije();
            procitaneBolesti = BazaPodataka.getSveBolesti();
            procitaneOsobe = BazaPodataka.getSveOsobe();
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
        zupanijeChoiceBox.setValue("Odaberi");
        bolestiChoiceBox.setValue("Odaberi");

        for (Zupanija z : procitaneZupanije) {
            zupanijeChoiceBox.getItems().add(z.getNaziv());
        }
        for (Bolest b : procitaneBolesti) {
            bolestiChoiceBox.getItems().add(b.getNaziv());
        }

        for (Osoba o : procitaneOsobe) {
            String checkboxString = o.getIme() + " " + o.getPrezime() + " - " + o.getZupanija();
            CheckBox osobaCheckBox = new CheckBox(checkboxString);
            CustomMenuItem osobaItem = new CustomMenuItem(osobaCheckBox);

            osobaCheckBox.setOnAction(e -> {
                if(osobaCheckBox.isSelected()) {
                    izabranKontakt.add(o.getId());
                }
                else {
                    izabranKontakt.remove(o.getId());
                }
            });
            osobaItem.setHideOnClick(false);
            kontaktiMenuButton.getItems().add(osobaItem);
        }
    }

    public void spremi() {
        try {
            String ime = imeText.getText();
            String prezime = prezimeText.getText();
            LocalDate datumRodjenja = datumRodjenjaDatePicker.getValue();
            String nazivZupanije = zupanijeChoiceBox.getValue();
            String nazivBolesti = bolestiChoiceBox.getValue();

            Zupanija odabranaZupanija = procitaneZupanije.stream()
                    .filter(z -> z.getNaziv().contains(nazivZupanije))
                    .findFirst()
                    .get();
            Bolest odabranaBolest = procitaneBolesti.stream()
                    .filter(b -> b.getNaziv().contains(nazivBolesti))
                    .findFirst()
                    .get();

            if (ime.isEmpty() || prezime.isEmpty()) {
                alertPogreska();
            } else {
                spremiOsobu(ime, prezime, datumRodjenja, odabranaZupanija, odabranaBolest);
                alertUspijeh();
            }
        }
        catch (Exception ex) {
            alertPogreska();
            ex.printStackTrace();
        }
        restartUnesenihPolja();
    }

    private void spremiOsobu(String ime, String prezime, LocalDate datumRodjenja, Zupanija odabranaZupanija,
                             Bolest odabranaBolest) throws SQLException, IOException {
        if (!izabranKontakt.isEmpty()) {
            List<Osoba> izabraniKontakti = new ArrayList<>();
            for (long id : izabranKontakt) {
                izabraniKontakti.add(BazaPodataka.getOsobaById(id));
            }

            BazaPodataka.spremiNovuOsobu(new Osoba.Builder(null)
                    .withIme(ime)
                    .withPrezime(prezime)
                    .withDatumRodjenja(datumRodjenja)
                    .withZupanija(odabranaZupanija)
                    .withBolescu(odabranaBolest)
                    .withKontakti(izabraniKontakti)
                    .build());
        } else {
            BazaPodataka.spremiNovuOsobu(new Osoba.Builder(null)
                    .withIme(ime)
                    .withPrezime(prezime)
                    .withDatumRodjenja(datumRodjenja)
                    .withZupanija(odabranaZupanija)
                    .withBolescu(odabranaBolest)
                    .build());
        }
    }

    private void alertUspijeh() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Unos osobe");
        alert.setHeaderText("Uspiješan unos");
        alert.setContentText("Uspiješno ste unesli osobu u aplikaciju!");
        alert.showAndWait();
    }

    private void alertPogreska() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Unos osobe");
        alert.setHeaderText("Pogreška");
        alert.setContentText("Pogreška pri unosu osobe u aplikaciju!");
        alert.showAndWait();
    }

    private void restartUnesenihPolja() {
        imeText.clear();
        prezimeText.clear();
        datumRodjenjaDatePicker.getEditor().clear();
        zupanijeChoiceBox.setValue("Odaberi");
        bolestiChoiceBox.setValue("Odaberi");
    }
}
