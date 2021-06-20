package hr.java.covidportal.controller;

import hr.java.covidportal.main.Main;
import hr.java.covidportal.model.Virus;
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

public class PretragaVirusaController implements Initializable {
    private static ObservableList<Virus> observableListaBolesti;

    private static final List<Virus> procitaniVirusi = Main.getProcitaniVirusi();

    @FXML
    private TextField nazivVirusa;
    @FXML
    private TableView<Virus> tablicaVirusa;
    @FXML
    private TableColumn<Virus, String> stupacNazivVirusa;
    @FXML
    private TableColumn<Virus, String> stupacSimptomi;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stupacNazivVirusa.setCellValueFactory(new PropertyValueFactory<Virus, String>("naziv"));
        stupacSimptomi.setCellValueFactory(new PropertyValueFactory<Virus, String>("simptomi"));

        List<Virus> listaVirusa = new ArrayList<>(procitaniVirusi);
        observableListaBolesti = FXCollections.observableList(listaVirusa);
        tablicaVirusa.setItems(observableListaBolesti);
    }

    public void pretraga() {
        String trazenString = nazivVirusa.getText().toLowerCase();

        List<Virus> trazeniVirusi = procitaniVirusi.stream()
                .filter(v -> v.getNaziv().toLowerCase().contains(trazenString))
                .collect(Collectors.toList());

        observableListaBolesti.clear();
        observableListaBolesti.addAll(FXCollections.observableList(trazeniVirusi));
    }
}
