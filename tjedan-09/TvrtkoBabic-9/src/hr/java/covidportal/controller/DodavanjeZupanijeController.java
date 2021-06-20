package hr.java.covidportal.controller;

import hr.java.covidportal.database.BazaPodataka;
import hr.java.covidportal.model.Zupanija;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.net.URL;
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

    public void spremi() {
        try {
            String nazivZupanije = nazivZupanijeText.getText();
            Integer brojStanovnika = Integer.parseInt(brojStanovnikaText.getText());
            Integer brojZarazenihStanovnika = Integer.parseInt(brojZarazenihStanovnikaText.getText());

            if (nazivZupanije.isEmpty()) {
                alertPogreska();
            } else {
                BazaPodataka.spremiNovuZupaniju(
                        new Zupanija(null, nazivZupanije, brojStanovnika, brojZarazenihStanovnika));
                alertUspijeh();
            }
        } catch (Exception e) {
            alertPogreska();
            e.printStackTrace();
        }

        nazivZupanijeText.clear();
        brojStanovnikaText.clear();
        brojZarazenihStanovnikaText.clear();
    }

    private void alertPogreska() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Unos županije");
        alert.setHeaderText("Pogreška");
        alert.setContentText("Pogreška pri unosu županije u aplikaciju!");
        alert.showAndWait();
    }

    private void alertUspijeh() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Unos županije");
        alert.setHeaderText("Uspiješan unos");
        alert.setContentText("Uspiješno ste unesli županiju u aplikaciju!");
        alert.showAndWait();
    }

}