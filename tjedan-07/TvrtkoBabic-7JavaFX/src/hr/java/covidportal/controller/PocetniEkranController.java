package hr.java.covidportal.controller;

import hr.java.covidportal.main.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;

public class PocetniEkranController implements Serializable {

    @FXML
    private MenuBar glavniIzbornik;

    @FXML
    public void prikaziEkranZaPretraguZupanija() throws IOException {
        Main.logger.info("Prikazuje se ekran za pretragu županija.");
        Parent pretragaZupanijaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaZupanija.fxml"));
        Scene pretragaZupanijaScene = new Scene(pretragaZupanijaFrame, 600, 400);
        Stage stage = (Stage) glavniIzbornik.getScene().getWindow();

        Main.setMainStage(stage);
        Main.getMainStage().setScene(pretragaZupanijaScene);
    }

    public void prikaziEkranZaPretraguSimptoma() throws IOException {
        Main.logger.info("Prikazuje se ekran za pretragu simptoma.");
        Parent pretragaSimptomaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaSimptoma.fxml"));
        Scene pretragaSimptomaScene = new Scene(pretragaSimptomaFrame, 600, 400);
        Stage stage = (Stage) glavniIzbornik.getScene().getWindow();

        Main.setMainStage(stage);
        Main.getMainStage().setScene(pretragaSimptomaScene);
    }

    public void prikaziEkranZaPretraguBolesti() throws IOException {
        Main.logger.info("Prikazuje se ekran za pretragu bolesti.");
        Parent pretragaBolestiFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaBolesti.fxml"));
        Scene pretragaBolestiScene = new Scene(pretragaBolestiFrame, 600, 400);
        Stage stage = (Stage) glavniIzbornik.getScene().getWindow();

        Main.setMainStage(stage);
        Main.getMainStage().setScene(pretragaBolestiScene);
    }

    public void prikaziEkranZaPretraguVirusa() throws IOException {
        Main.logger.info("Prikazuje se ekran za pretragu virusa.");
        Parent pretragaVirusaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaVirusa.fxml"));
        Scene pretragaVirusaScene = new Scene(pretragaVirusaFrame, 600, 400);
        Stage stage = (Stage) glavniIzbornik.getScene().getWindow();

        Main.setMainStage(stage);
        Main.getMainStage().setScene(pretragaVirusaScene);
    }

    public void prikaziEkranZaPretraguOsoba() throws IOException {
        Main.logger.info("Prikazuje se ekran za pretragu osoba.");
        Parent pretragaOsobaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaOsoba.fxml"));
        Scene pretragaOsobaScene = new Scene(pretragaOsobaFrame, 600, 400);
        Stage stage = (Stage) glavniIzbornik.getScene().getWindow();

        Main.setMainStage(stage);
        Main.getMainStage().setScene(pretragaOsobaScene);
    }
}