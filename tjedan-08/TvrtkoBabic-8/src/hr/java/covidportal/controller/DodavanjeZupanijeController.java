package hr.java.covidportal.controller;

import hr.java.covidportal.main.Datoteke;
import hr.java.covidportal.main.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

public class DodavanjeZupanijeController implements Initializable {

    @FXML
    private TextField nazivZupanijeText;
    @FXML
    private TextField brojStanovnikaText;
    @FXML
    private TextField brojZarazenihStanovnikaText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void spremi() throws IOException {
        String nazivZupanije = nazivZupanijeText.getText();
        Integer brojStanovnika = Integer.parseInt(brojStanovnikaText.getText());
        Integer brojZarazenihStanovnika = Integer.parseInt(brojZarazenihStanovnikaText.getText());

        // DUGACIJE NAPRAVIT ID ---------------------------------
        // DUGACIJE NAPRAVIT ID ---------------------------------
        Path datoteka = Path.of(Datoteke.ZUPANIJE_TEXT);
        BufferedReader reader = new BufferedReader(new FileReader(Datoteke.ZUPANIJE_TEXT));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();
        int id = (lines / 4) + 1;

        unesiZupaniju(nazivZupanije, brojStanovnika, brojZarazenihStanovnika, datoteka, id);

        nazivZupanijeText.clear();
        brojStanovnikaText.clear();
        brojZarazenihStanovnikaText.clear();
    }

    private void unesiZupaniju(String nazivZupanije, Integer brojStanovnika, Integer brojZarazenihStanovnika, Path datoteka, int id) {
        try {
            System.out.println("Zapisivanje u " + Datoteke.ZUPANIJE_TEXT);
            Files.writeString(datoteka,  Integer.toString(id) + '\n', StandardOpenOption.APPEND);
            Files.writeString(datoteka, nazivZupanije.toUpperCase() + '\n', StandardOpenOption.APPEND);
            Files.writeString(datoteka, brojStanovnika.toString() + '\n', StandardOpenOption.APPEND);
            Files.writeString(datoteka, brojZarazenihStanovnika.toString() + '\n', StandardOpenOption.APPEND);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Unos županije");
            alert.setHeaderText("Uspiješan unos");
            alert.setContentText("Uspiješno ste unesli županiju u aplikaciju!");
            alert.showAndWait();
        }
        catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Unos županije");
            alert.setHeaderText("Pogreška");
            alert.setContentText("Pogreška pri unosu županije u aplikaciju!");
            alert.showAndWait();

            ex.printStackTrace();
            Main.logger.error("Pogreška pri zapisavanju u " + Datoteke.ZUPANIJE_TEXT, ex);
        }
    }
}