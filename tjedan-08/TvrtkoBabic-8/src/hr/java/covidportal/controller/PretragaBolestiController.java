package hr.java.covidportal.controller;

import hr.java.covidportal.main.Datoteke;
import hr.java.covidportal.model.Bolest;
import hr.java.covidportal.model.Simptom;
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

public class PretragaBolestiController implements Initializable {
    private static ObservableList<Bolest> observableListaBolesti;
    private static List<Bolest> procitaneBolesti;

    @FXML
    private TextField nazivBolesti;
    @FXML
    private TableView<Bolest> tablicaBolesti;
    @FXML
    private TableColumn<Bolest, String> stupacNazivBolesti;
    @FXML
    private TableColumn<Bolest, String> stupacSimptomi;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Simptom> procitaniSimptomi = Datoteke.unosSimptoma();
        procitaneBolesti = Datoteke.unosBolesti(procitaniSimptomi);

        stupacNazivBolesti.setCellValueFactory(new PropertyValueFactory<Bolest, String>("naziv"));
        stupacSimptomi.setCellValueFactory(new PropertyValueFactory<Bolest, String>("SimptomiToString"));

        List<Bolest> listaBolesti = new ArrayList<>(procitaneBolesti);
        observableListaBolesti = FXCollections.observableList(listaBolesti);
        tablicaBolesti.setItems(observableListaBolesti);
    }

    public void pretraga() {
        String trazenString = nazivBolesti.getText().toLowerCase();

        List<Bolest> trazeneBolesti = procitaneBolesti.stream()
                .filter(b -> b.getNaziv().toLowerCase().contains(trazenString))
                .collect(Collectors.toList());

        observableListaBolesti.clear();
        observableListaBolesti.addAll(FXCollections.observableList(trazeneBolesti));
    }
}
