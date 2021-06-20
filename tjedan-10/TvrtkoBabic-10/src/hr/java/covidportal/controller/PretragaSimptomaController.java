package hr.java.covidportal.controller;

import hr.java.covidportal.database.BazaPodatakaNiti;
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

public class PretragaSimptomaController implements Initializable {

    private static ObservableList<Simptom> observableListaSimptoma;
    private static List<Simptom> procitaniSimptomi;

    @FXML
    private TextField nazivSimptoma;
    @FXML
    private TableView<Simptom> tablicaSimptoma;
    @FXML
    private TableColumn<Simptom, String> stupacNazivSimptoma;
    @FXML
    private TableColumn<Simptom, String> stupacVrijednost;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        procitaniSimptomi = BazaPodatakaNiti.getSveSimptome();

        stupacNazivSimptoma.setCellValueFactory(new PropertyValueFactory<Simptom, String>("naziv"));
        stupacVrijednost.setCellValueFactory(new PropertyValueFactory<Simptom, String>("vrijednost"));

        List<Simptom> listaSimptoma = new ArrayList<>(procitaniSimptomi);
        observableListaSimptoma = FXCollections.observableList(listaSimptoma);
        tablicaSimptoma.setItems(observableListaSimptoma);
    }

    public void pretraga() {
        String trazenString = nazivSimptoma.getText().toLowerCase();

        List<Simptom> trazeniSimptom = procitaniSimptomi.stream()
                .filter(s -> s.getNaziv().toLowerCase().contains(trazenString))
                .collect(Collectors.toList());

        observableListaSimptoma.clear();
        observableListaSimptoma.addAll(FXCollections.observableList(trazeniSimptom));
    }
}