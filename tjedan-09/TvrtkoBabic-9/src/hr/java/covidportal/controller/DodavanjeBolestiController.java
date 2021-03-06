package hr.java.covidportal.controller;

import hr.java.covidportal.database.BazaPodataka;
import hr.java.covidportal.model.Bolest;
import hr.java.covidportal.model.Simptom;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
        try {
            procitaniSimptomi = BazaPodataka.getSveSimptome();
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
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
            String nazivBolesti = nazivBolestiText.getText();
            List<Simptom> listaIzabranihSimptoma = new ArrayList<>();

            for (String nazivIzabranog : izabraniSimptomi) {
                Simptom izabranSimptom = procitaniSimptomi.stream()
                        .filter(simptom -> simptom.getNaziv().contains(nazivIzabranog))
                        .findFirst()
                        .get();
                listaIzabranihSimptoma.add(izabranSimptom);
            }

            if (nazivBolesti.isEmpty() || listaIzabranihSimptoma.isEmpty()) {
                alertPogreska();
            } else {
                BazaPodataka.spremiNovuBolest(new Bolest(null, nazivBolesti, listaIzabranihSimptoma));
                alertUspijeh();
            }
        }
        catch (Exception ex) {
            alertPogreska();
            ex.printStackTrace();
        }
        nazivBolestiText.clear();
    }

    private void alertPogreska() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Unos bolesti");
        alert.setHeaderText("Pogre??ka");
        alert.setContentText("Pogre??ka pri unosu bolest u aplikaciju!");
        alert.showAndWait();
    }

    private void alertUspijeh() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Unos bolesti");
        alert.setHeaderText("Uspije??an unos");
        alert.setContentText("Uspije??no ste unesli bolest u aplikaciju!");
        alert.showAndWait();
    }
}
