package hr.java.covidportal.controller;

import hr.java.covidportal.main.Datoteke;
import hr.java.covidportal.main.Main;
import hr.java.covidportal.model.Bolest;
import hr.java.covidportal.model.ImenovaniEntitet;
import hr.java.covidportal.model.Simptom;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class DodavanjeBolestiController implements Initializable {

    private static List<String> izabraniSimptomi = new ArrayList<>();
    private static List<Simptom> procitaniSimptomi;

    @FXML
    private TextField nazivBolestiText;
    @FXML
    private MenuButton simptomiMenuButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        procitaniSimptomi = Datoteke.unosSimptoma();
        izabraniSimptomi.clear();

        for (Simptom s : procitaniSimptomi) {
            String checkboxString = s.getNaziv() + " " + s.getVrijednost();
            CheckBox simptomCheckBox = new CheckBox(checkboxString);
            CustomMenuItem simptomItem = new CustomMenuItem(simptomCheckBox);

            simptomCheckBox.setOnAction(e -> {
                if(simptomCheckBox.isSelected()) {
                    izabraniSimptomi.add(s.getNaziv());
                    System.out.println(checkboxString + " je izabran");
                }
                else {
                    izabraniSimptomi.remove(s.getNaziv());
                    System.out.println(checkboxString + " nije izabran");
                }
            });
            simptomItem.setHideOnClick(false);
            simptomiMenuButton.getItems().add(simptomItem);
        }
    }

    public void spremi() {
        Long novaBolestId;
        String nazivBolesti = nazivBolestiText.getText();
        List<Long> simptomiIdList = new ArrayList<>();
        List<Bolest> procitaneBolesti = Datoteke.unosBolesti(procitaniSimptomi);

        Optional<Bolest> maxId = procitaneBolesti.stream().max(Comparator.comparing(ImenovaniEntitet::getId));
        novaBolestId = maxId.map(bolest -> bolest.getId() + 1).orElse(1L);

        for (String nazivIzabranog : izabraniSimptomi) {
            Simptom izabranSimptom = procitaniSimptomi.stream()
                    .filter(simptom -> simptom.getNaziv().contains(nazivIzabranog))
                    .findFirst()
                    .get();
            simptomiIdList.add(izabranSimptom.getId());
        }

        unesiBolest(nazivBolesti, simptomiIdList, novaBolestId);

        nazivBolestiText.clear();
    }

    private void unesiBolest(String nazivBolesti, List<Long> simptomiIdList, Long id) {
        try {
            System.out.println("Zapisivanje u " + Datoteke.BOLESTI_TEXT);
            Path datoteka = Path.of(Datoteke.BOLESTI_TEXT);

            Files.writeString(datoteka, Long.toString(id) + '\n', StandardOpenOption.APPEND);
            Files.writeString(datoteka,  nazivBolesti.toUpperCase() + '\n', StandardOpenOption.APPEND);

            if(simptomiIdList.size() > 1) {
                for (int i = 0; i < simptomiIdList.size() - 1; i++) {
                    Files.writeString(datoteka, Long.toString(simptomiIdList.get(i)) + ',', StandardOpenOption.APPEND);
                }
                Files.writeString(datoteka, Long.toString(simptomiIdList.get(simptomiIdList.size()-1))
                                + '\n', StandardOpenOption.APPEND);
            }
            else Files.writeString(datoteka, Long.toString(simptomiIdList.get(0)) + '\n', StandardOpenOption.APPEND);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Unos bolesti");
            alert.setHeaderText("Uspiješan unos");
            alert.setContentText("Uspiješno ste unesli bolest u aplikaciju!");
            alert.showAndWait();
        }
        catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Unos bolesti");
            alert.setHeaderText("Pogreška");
            alert.setContentText("Pogreška pri unosu bolest u aplikaciju!");
            alert.showAndWait();

            ex.printStackTrace();
            Main.logger.error("Pogreška pri zapisavanju u " + Datoteke.BOLESTI_TEXT, ex);
        }
    }
}
