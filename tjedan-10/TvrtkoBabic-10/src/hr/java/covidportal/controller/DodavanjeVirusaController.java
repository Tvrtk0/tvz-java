package hr.java.covidportal.controller;

import hr.java.covidportal.database.BazaPodatakaNiti;
import hr.java.covidportal.model.Simptom;
import hr.java.covidportal.model.Virus;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.*;

public class DodavanjeVirusaController implements Initializable {
    private static List<String> izabraniSimptomi = new ArrayList<>();
    private static List<Simptom> procitaniSimptomi;

    @FXML
    private TextField nazivVirusaText;
    @FXML
    private MenuButton simptomiMenuButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        procitaniSimptomi = BazaPodatakaNiti.getSveSimptome();

        izabraniSimptomi.clear();
        for (Simptom s : procitaniSimptomi) {
            String checkboxString = s.getNaziv() + " " + s.getVrijednost();
            CheckBox simptomCheckBox = new CheckBox(checkboxString);
            CustomMenuItem simptomItem = new CustomMenuItem(simptomCheckBox);

            simptomCheckBox.setOnAction(e -> {
                if(simptomCheckBox.isSelected()) {
                    izabraniSimptomi.add(s.getNaziv());
                }
                else {
                    izabraniSimptomi.remove(s.getNaziv());
                }
            });
            simptomItem.setHideOnClick(false);
            simptomiMenuButton.getItems().add(simptomItem);
        }
    }

    public void spremi() {
        try {
            String nazivVirusa = nazivVirusaText.getText();
            List<Simptom> listaIzabranihSimptoma = new ArrayList<>();

            for (String nazivIzabranog : izabraniSimptomi) {
                Simptom izabranSimptom = procitaniSimptomi.stream()
                        .filter(simptom -> simptom.getNaziv().contains(nazivIzabranog))
                        .findFirst()
                        .get();
                listaIzabranihSimptoma.add(izabranSimptom);
            }

            if (nazivVirusa.isEmpty() || listaIzabranihSimptoma.isEmpty()) {
                alertPogreska();
            } else {
                BazaPodatakaNiti.spremiNovuBolest(new Virus(null, nazivVirusa, listaIzabranihSimptoma));
                alertUspijeh();
            }
        }
        catch (Exception ex) {
            alertPogreska();
            ex.printStackTrace();
        }
        nazivVirusaText.clear();
    }

    private void alertUspijeh() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Unos virusa");
        alert.setHeaderText("Uspije??an unos");
        alert.setContentText("Uspije??no ste unesli virus u aplikaciju!");
        alert.showAndWait();
    }

    private void alertPogreska() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Unos virusa");
        alert.setHeaderText("Pogre??ka");
        alert.setContentText("Pogre??ka pri unosu virusa u aplikaciju!");
        alert.showAndWait();
    }
}
