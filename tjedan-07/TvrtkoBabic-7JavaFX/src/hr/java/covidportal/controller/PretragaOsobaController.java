package hr.java.covidportal.controller;

import hr.java.covidportal.main.Main;
import hr.java.covidportal.model.Osoba;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PretragaOsobaController implements Initializable {
    private static ObservableList<Osoba> observableListaOsoba;

    private static final List<Osoba> procitaneOsobe = Main.getProcitaneOsobe();

    @FXML
    private TextField imeOsobe;
    @FXML
    private TextField prezimeOsobe;
    @FXML
    private TableView<Osoba> tablicaOsoba;
    @FXML
    private TableColumn<Osoba, String> stupacImeOsobe;
    @FXML
    private TableColumn<Osoba, String> stupacPrezimeOsobe;
    @FXML
    private TableColumn<Osoba, Integer> stupacStarost;
    @FXML
    private TableColumn<Osoba, String> stupacZupanija;
    @FXML
    private TableColumn<Osoba, String> stupacZarazenBolescu;
    @FXML
    private TableColumn<Osoba, String> stupacKontaktiraneOsobe;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stupacImeOsobe.setCellValueFactory(new PropertyValueFactory<Osoba, String>("ime"));
        stupacPrezimeOsobe.setCellValueFactory(new PropertyValueFactory<Osoba, String>("prezime"));
        stupacStarost.setCellValueFactory(new PropertyValueFactory<Osoba, Integer>("starost"));
        stupacZupanija.setCellValueFactory(new PropertyValueFactory<Osoba, String>("zupanija"));
        stupacZarazenBolescu.setCellValueFactory(new PropertyValueFactory<Osoba, String>("zarazenBolescu"));
        stupacKontaktiraneOsobe.setCellValueFactory(new PropertyValueFactory<Osoba, String>("kontaktiraneOsobe"));

        List<Osoba> listaOsoba = new ArrayList<>(procitaneOsobe);
        observableListaOsoba = FXCollections.observableList(listaOsoba);
        tablicaOsoba.setItems(observableListaOsoba);
    }

    public void pretraga() {
        String trazenoIme = imeOsobe.getText().toLowerCase();
        String trazenoPrezime = prezimeOsobe.getText().toLowerCase();

        List<Osoba> trazeneOsobe = procitaneOsobe.stream()
                .filter(o -> o.getIme().toLowerCase().contains(trazenoIme))
                .filter(o -> o.getPrezime().toLowerCase().contains(trazenoPrezime))
                .collect(Collectors.toList());

        observableListaOsoba.clear();
        observableListaOsoba.addAll(FXCollections.observableList(trazeneOsobe));
    }
}
