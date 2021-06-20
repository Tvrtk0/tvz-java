package hr.java.covidportal.controller;

import hr.java.covidportal.database.BazaPodataka;
import hr.java.covidportal.enumeracije.VrijednostSimptoma;
import hr.java.covidportal.main.Main;
import hr.java.covidportal.model.Simptom;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

public class DodavanjeSimptomaController implements Initializable {

    @FXML
    private TextField nazivSimptomaText;
    @FXML
    private ChoiceBox<String> choiceBox = new ChoiceBox<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBox.getItems().add(VrijednostSimptoma.PRODUKTIVNI.getVrijednost());
        choiceBox.getItems().add(VrijednostSimptoma.INTENZIVNO.getVrijednost());
        choiceBox.getItems().add(VrijednostSimptoma.JAKA.getVrijednost());
        choiceBox.getItems().add(VrijednostSimptoma.VISOKA.getVrijednost());
        choiceBox.getItems().add(VrijednostSimptoma.RIJETKO.getVrijednost());
        choiceBox.getItems().add(VrijednostSimptoma.SREDNJE.getVrijednost());
        choiceBox.getItems().add(VrijednostSimptoma.CESTO.getVrijednost());
        choiceBox.setValue("Odabir");
    }

    public void spremi() {
        try{
            String nazivSimptoma = nazivSimptomaText.getText();
            String vrijednostSimptoma = choiceBox.getValue();

            if (nazivSimptoma.isEmpty() || vrijednostSimptoma.equals("Odabir")) {
                alertPogreska();
            } else {
                BazaPodataka.spremiNoviSimptom(new Simptom(null, nazivSimptoma, vrijednostSimptoma));
                alertUspijeh();
            }
        } catch (Exception e) {
            e.printStackTrace();
            alertPogreska();
        }

        nazivSimptomaText.clear();
        choiceBox.setValue("Odabir");
    }

    private void alertUspijeh() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Unos simptoma");
        alert.setHeaderText("Uspiješan unos");
        alert.setContentText("Uspiješno ste unesli simptom u aplikaciju!");
        alert.showAndWait();
    }

    private void alertPogreska() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Unos simptoma");
        alert.setHeaderText("Pogreška");
        alert.setContentText("Pogreška pri unosu simptoma u aplikaciju!");
        alert.showAndWait();
    }
}

