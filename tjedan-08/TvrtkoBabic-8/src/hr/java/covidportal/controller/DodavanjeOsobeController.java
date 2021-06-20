package hr.java.covidportal.controller;

import hr.java.covidportal.main.Datoteke;
import hr.java.covidportal.main.Main;
import hr.java.covidportal.model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class DodavanjeOsobeController implements Initializable {

    private static List<Zupanija> procitaneZupanije;
    private static List<Simptom> procitaniSimptomi;
    private static List<Bolest> procitaneBolesti;
    private static List<Virus> procitaniVirusi;

    private static List<Osoba> procitaneOsobe;
    private static List<Long> izabranKontakt = new ArrayList<>();

    @FXML
    private TextField imeText;
    @FXML
    private TextField prezimeText;
    @FXML
    private TextField starostText;
    @FXML
    private ChoiceBox<String> zupanijeChoiceBox = new ChoiceBox<>();
    @FXML
    private ChoiceBox<String> bolestiChoiceBox = new ChoiceBox<>();
    @FXML
    private MenuButton kontaktiMenuButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        procitaneZupanije = Datoteke.unosZupanija();
        procitaniSimptomi = Datoteke.unosSimptoma();
        procitaneBolesti = Datoteke.unosBolesti(procitaniSimptomi);
        procitaniVirusi = Datoteke.unosVirusa(procitaniSimptomi);
        procitaneOsobe = Datoteke.unosOsoba(procitaneZupanije, procitaneBolesti, procitaniVirusi);
        zupanijeChoiceBox.setValue("Odaberi");
        bolestiChoiceBox.setValue("Odaberi");

        for (Zupanija z : procitaneZupanije) {
            zupanijeChoiceBox.getItems().add(z.getNaziv());
        }
        for (Bolest b : procitaneBolesti) {
            bolestiChoiceBox.getItems().add(b.getNaziv());
        }
        for (Virus v : procitaniVirusi) {
            bolestiChoiceBox.getItems().add(v.getNaziv());
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
        Long novaOsobaId;
        String ime = imeText.getText();
        String prezime = prezimeText.getText();
        Integer starost = Integer.parseInt(starostText.getText());
        String nazivZupanije = zupanijeChoiceBox.getValue();
        String nazivBolesti = bolestiChoiceBox.getValue();
        String tipBolesti;

        List<Bolest> bolestiAndVirusi = new ArrayList<>();
        bolestiAndVirusi.addAll(procitaneBolesti);
        bolestiAndVirusi.addAll(procitaniVirusi);

        Optional<Osoba> maxId = procitaneOsobe.stream().max(Comparator.comparing(Osoba::getId));
        novaOsobaId = maxId.map(o -> o.getId() + 1).orElse(1L);

        Zupanija odabranaZupanija = procitaneZupanije.stream()
                .filter(z -> z.getNaziv().contains(nazivZupanije))
                .findFirst()
                .get();
        Bolest odabranaBolest = bolestiAndVirusi.stream()
                .filter(b -> b.getNaziv().contains(nazivBolesti))
                .findFirst()
                .get();

        if (odabranaBolest instanceof Virus) tipBolesti = "V";
        else tipBolesti = "B";

        unosOsobe(novaOsobaId, ime, prezime, starost, tipBolesti, odabranaZupanija, odabranaBolest);

        restartVrijednostiUnosa();
    }

    private void unosOsobe(Long novaOsobaId, String ime, String prezime, Integer starost, String tipBolesti, Zupanija odabranaZupanija, Bolest odabranaBolest) {
        try {
            System.out.println("Zapisivanje u " + Datoteke.OSOBE_TEXT);
            Path datoteka = Path.of(Datoteke.OSOBE_TEXT);

            Files.writeString(datoteka, Long.toString(novaOsobaId) + '\n', StandardOpenOption.APPEND);
            Files.writeString(datoteka,  ime + '\n' + prezime + '\n' + starost + '\n', StandardOpenOption.APPEND);
            Files.writeString(datoteka, Long.toString(odabranaZupanija.getId()) + '\n', StandardOpenOption.APPEND);
            Files.writeString(datoteka, tipBolesti + "-" + odabranaBolest.getId() + '\n', StandardOpenOption.APPEND);

            if (!izabranKontakt.isEmpty()) {
                if (izabranKontakt.size() > 1) {
                    for (int i = 0; i < izabranKontakt.size() - 1; i++) {
                        Files.writeString(datoteka, Long.toString(izabranKontakt.get(i)) + ',', StandardOpenOption.APPEND);
                    }
                    Files.writeString(datoteka, Long.toString(izabranKontakt.get(izabranKontakt.size()-1))
                            + '\n', StandardOpenOption.APPEND);
                }
                else Files.writeString(datoteka, Long.toString(izabranKontakt.get(0)) + '\n', StandardOpenOption.APPEND);
            }
            else Files.writeString(datoteka, "\n", StandardOpenOption.APPEND);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Unos osobe");
            alert.setHeaderText("Uspiješan unos");
            alert.setContentText("Uspiješno ste unesli osobu u aplikaciju!");
            alert.showAndWait();
        }
        catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Unos osobe");
            alert.setHeaderText("Pogreška");
            alert.setContentText("Pogreška pri unosu osobe u aplikaciju!");
            alert.showAndWait();

            ex.printStackTrace();
            Main.logger.error("Pogreška pri zapisavanju u " + Datoteke.OSOBE_TEXT, ex);
        }
    }

    private void restartVrijednostiUnosa() {
        imeText.clear();
        prezimeText.clear();
        starostText.clear();
        zupanijeChoiceBox.setValue("Odaberi");
        bolestiChoiceBox.setValue("Odaberi");
        procitaneOsobe = Datoteke.unosOsoba(procitaneZupanije, procitaneBolesti, procitaniVirusi);
    }
}
