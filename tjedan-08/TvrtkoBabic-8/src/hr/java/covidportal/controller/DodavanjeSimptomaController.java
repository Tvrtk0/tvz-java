package hr.java.covidportal.controller;

import hr.java.covidportal.main.Datoteke;
import hr.java.covidportal.main.Main;
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
        choiceBox.getItems().add("RIJETKO");
        choiceBox.getItems().add("SREDNJE");
        choiceBox.getItems().add("ČESTO");

        choiceBox.setValue("Odabir");
    }

    public void spremi() throws IOException {
        String nazivSimptoma = nazivSimptomaText.getText();
        String vrijednostSimptoma = choiceBox.getValue();

        // DUGACIJE NAPRAVIT ID ---------------------------------
        // DUGACIJE NAPRAVIT ID ---------------------------------
        Path datoteka = Path.of(Datoteke.SIMPTOMI_TEXT);
        BufferedReader reader = new BufferedReader(new FileReader(Datoteke.SIMPTOMI_TEXT));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();
        int id = (lines / 3) + 1;

        unesiSimptom(nazivSimptoma, vrijednostSimptoma, datoteka, id);

        nazivSimptomaText.clear();
        choiceBox.setValue("Odabir");
    }

    private void unesiSimptom(String nazivSimptoma, String vrijednostSimptoma, Path datoteka, int id) {
        try {
            System.out.println("Zapisivanje u " + Datoteke.SIMPTOMI_TEXT);
            Files.writeString(datoteka, Integer.toString(id) + '\n', StandardOpenOption.APPEND);
            Files.writeString(datoteka,  nazivSimptoma.toUpperCase() + '\n', StandardOpenOption.APPEND);
            Files.writeString(datoteka,  vrijednostSimptoma + '\n', StandardOpenOption.APPEND);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Unos simptoma");
            alert.setHeaderText("Uspiješan unos");
            alert.setContentText("Uspiješno ste unesli simptom u aplikaciju!");
            alert.showAndWait();
        }
        catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Unos simptoma");
            alert.setHeaderText("Pogreška");
            alert.setContentText("Pogreška pri unosu simptoma u aplikaciju!");
            alert.showAndWait();

            ex.printStackTrace();
            Main.logger.error("Pogreška pri zapisavanju u " + Datoteke.SIMPTOMI_TEXT, ex);
        }
    }
}

