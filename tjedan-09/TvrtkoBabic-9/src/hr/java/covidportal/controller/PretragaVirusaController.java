package hr.java.covidportal.controller;

import hr.java.covidportal.database.BazaPodataka;
import hr.java.covidportal.model.Virus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PretragaVirusaController implements Initializable {

    private static ObservableList<Virus> observableListaVirusa;
    private static List<Virus> procitaniVirusi;

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
        try {
            procitaniVirusi = BazaPodataka.getSamoVirusi();
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }


        stupacNazivVirusa.setCellValueFactory(new PropertyValueFactory<Virus, String>("naziv"));
        stupacSimptomi.setCellValueFactory(new PropertyValueFactory<Virus, String>("SimptomiToString"));

        List<Virus> listaVirusa = new ArrayList<>(procitaniVirusi);
        observableListaVirusa = FXCollections.observableList(listaVirusa);
        tablicaVirusa.setItems(observableListaVirusa);
    }

    public void pretraga() {
        String trazenString = nazivVirusa.getText().toLowerCase();

        List<Virus> trazeniVirusi = procitaniVirusi.stream()
                .filter(v -> v.getNaziv().toLowerCase().contains(trazenString))
                .collect(Collectors.toList());

        observableListaVirusa.clear();
        observableListaVirusa.addAll(FXCollections.observableList(trazeniVirusi));
    }
}
