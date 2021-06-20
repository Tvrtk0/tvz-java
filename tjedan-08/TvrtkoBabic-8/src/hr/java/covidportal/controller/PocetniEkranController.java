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
import java.util.Objects;

public class PocetniEkranController implements Serializable {

    private static int visinaEkrana = 450;
    private static int sirinaEkrana = 600;
    @FXML
    private MenuBar glavniIzbornik;

    @FXML
    public void prikaziEkranZaPretraguZupanija() throws IOException {
        Main.logger.info("Prikazuje se ekran za pretragu županija.");
        Parent pretragaZupanijaFrame = FXMLLoader.load(Objects.requireNonNull(
                        getClass().getClassLoader().getResource("pretragaZupanija.fxml")));
        Scene pretragaZupanijaScene = new Scene(pretragaZupanijaFrame, sirinaEkrana, visinaEkrana);
        Stage stage = (Stage) glavniIzbornik.getScene().getWindow();

        Main.setMainStage(stage);
        Main.getMainStage().setScene(pretragaZupanijaScene);
    }

    public void prikaziEkranZaDodavanjeZupanija() throws IOException {
        Main.logger.info("Prikazuje se ekran za dodavanje županija.");
        Parent dodavanjeZupanijaFrame = FXMLLoader.load(Objects.requireNonNull(
                getClass().getClassLoader().getResource("dodavanjeZupanije.fxml")));
        Scene dodavanjeZupanijaScene = new Scene(dodavanjeZupanijaFrame, sirinaEkrana, visinaEkrana);
        Stage stage = (Stage) glavniIzbornik.getScene().getWindow();

        Main.setMainStage(stage);
        Main.getMainStage().setScene(dodavanjeZupanijaScene);
    }

    public void prikaziEkranZaPretraguSimptoma() throws IOException {
        Main.logger.info("Prikazuje se ekran za pretragu simptoma.");
        Parent pretragaSimptomaFrame = FXMLLoader.load(Objects.requireNonNull(
                getClass().getClassLoader().getResource("pretragaSimptoma.fxml")));
        Scene pretragaSimptomaScene = new Scene(pretragaSimptomaFrame, sirinaEkrana, visinaEkrana);
        Stage stage = (Stage) glavniIzbornik.getScene().getWindow();

        Main.setMainStage(stage);
        Main.getMainStage().setScene(pretragaSimptomaScene);
    }

    public void prikaziEkranZaDodavanjeSimptoma() throws IOException {
        Main.logger.info("Prikazuje se ekran za dodavanje županija.");
        Parent dodavanjeZupanijaFrame = FXMLLoader.load(Objects.requireNonNull(
                getClass().getClassLoader().getResource("dodavanjeSimptoma.fxml")));
        Scene dodavanjeZupanijaScene = new Scene(dodavanjeZupanijaFrame, sirinaEkrana, visinaEkrana);
        Stage stage = (Stage) glavniIzbornik.getScene().getWindow();

        Main.setMainStage(stage);
        Main.getMainStage().setScene(dodavanjeZupanijaScene);
    }

    public void prikaziEkranZaPretraguBolesti() throws IOException {
        Main.logger.info("Prikazuje se ekran za pretragu bolesti.");
        Parent pretragaBolestiFrame = FXMLLoader.load(Objects.requireNonNull(
                getClass().getClassLoader().getResource("pretragaBolesti.fxml")));
        Scene pretragaBolestiScene = new Scene(pretragaBolestiFrame, sirinaEkrana, visinaEkrana);
        Stage stage = (Stage) glavniIzbornik.getScene().getWindow();

        Main.setMainStage(stage);
        Main.getMainStage().setScene(pretragaBolestiScene);
    }

    public void prikaziEkranZaDodavanjeBolesti() throws IOException {
        Main.logger.info("Prikazuje se ekran za dodavanje bolesti.");
        Parent dodavanjeBolestiFrame = FXMLLoader.load(Objects.requireNonNull(
                getClass().getClassLoader().getResource("dodavanjeBolesti.fxml")));
        Scene dodavanjeBolestiScene = new Scene(dodavanjeBolestiFrame, sirinaEkrana, visinaEkrana);
        Stage stage = (Stage) glavniIzbornik.getScene().getWindow();

        Main.setMainStage(stage);
        Main.getMainStage().setScene(dodavanjeBolestiScene);
    }

    public void prikaziEkranZaPretraguVirusa() throws IOException {
        Main.logger.info("Prikazuje se ekran za pretragu virusa.");
        Parent pretragaVirusaFrame = FXMLLoader.load(Objects.requireNonNull(
                getClass().getClassLoader().getResource("pretragaVirusa.fxml")));
        Scene pretragaVirusaScene = new Scene(pretragaVirusaFrame, sirinaEkrana, visinaEkrana);
        Stage stage = (Stage) glavniIzbornik.getScene().getWindow();

        Main.setMainStage(stage);
        Main.getMainStage().setScene(pretragaVirusaScene);
    }

    public void prikaziEkranZaDodavanjeVirusa() throws IOException {
        Main.logger.info("Prikazuje se ekran za dodavanje virusa.");
        Parent dodavanjeVirusaFrame = FXMLLoader.load(Objects.requireNonNull(
                getClass().getClassLoader().getResource("dodavanjeVirusa.fxml")));
        Scene dodavanjeVirusaScene = new Scene(dodavanjeVirusaFrame, sirinaEkrana, visinaEkrana);
        Stage stage = (Stage) glavniIzbornik.getScene().getWindow();

        Main.setMainStage(stage);
        Main.getMainStage().setScene(dodavanjeVirusaScene);
    }

    public void prikaziEkranZaPretraguOsoba() throws IOException {
        Main.logger.info("Prikazuje se ekran za pretragu osoba.");
        Parent pretragaOsobaFrame = FXMLLoader.load(Objects.requireNonNull(
                getClass().getClassLoader().getResource("pretragaOsoba.fxml")));
        Scene pretragaOsobaScene = new Scene(pretragaOsobaFrame, sirinaEkrana, visinaEkrana);
        Stage stage = (Stage) glavniIzbornik.getScene().getWindow();

        Main.setMainStage(stage);
        Main.getMainStage().setScene(pretragaOsobaScene);
    }

    public void prikaziEkranZaDodavanjeOsoba() throws IOException {
        Main.logger.info("Prikazuje se ekran za dodavanje osoba.");
        Parent dodavanjeOsobaFrame = FXMLLoader.load(Objects.requireNonNull(
                getClass().getClassLoader().getResource("dodavanjeOsobe.fxml")));
        Scene dodavanjeOsobaScene = new Scene(dodavanjeOsobaFrame, sirinaEkrana, visinaEkrana);
        Stage stage = (Stage) glavniIzbornik.getScene().getWindow();

        Main.setMainStage(stage);
        Main.getMainStage().setScene(dodavanjeOsobaScene);
    }
}