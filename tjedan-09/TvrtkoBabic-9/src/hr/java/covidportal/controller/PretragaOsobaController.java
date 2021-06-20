package hr.java.covidportal.controller;

import hr.java.covidportal.database.BazaPodataka;
import hr.java.covidportal.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PretragaOsobaController implements Initializable {

    private static ObservableList<Osoba> observableListaOsoba;
    private static List<Osoba> procitaneOsobe;

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
        try {
            procitaneOsobe = BazaPodataka.getSveOsobe();
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }

        stupacImeOsobe.setCellValueFactory(new PropertyValueFactory<Osoba, String>("ime"));
        stupacPrezimeOsobe.setCellValueFactory(new PropertyValueFactory<Osoba, String>("prezime"));
        stupacStarost.setCellValueFactory(new PropertyValueFactory<Osoba, Integer>("starost"));
        stupacZupanija.setCellValueFactory(new PropertyValueFactory<Osoba, String>("zupanija"));
        stupacZarazenBolescu.setCellValueFactory(new PropertyValueFactory<Osoba, String>("zarazenBolescu"));
        stupacKontaktiraneOsobe.setCellValueFactory(new PropertyValueFactory<Osoba, String>("KontaktiraneOsobeToString"));

        List<Osoba> listaOsoba = new ArrayList<>(procitaneOsobe);
        observableListaOsoba = FXCollections.observableList(listaOsoba);
        tablicaOsoba.setItems(observableListaOsoba);

        ContextMenu clickMenu = new ContextMenu();
        MenuItem mi1 = new MenuItem("Menu 1");
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(e -> {
            Osoba o = tablicaOsoba.getSelectionModel().getSelectedItem();
            try {
                BazaPodataka.deleteOsobu(o.getId());
                observableListaOsoba.clear();
                observableListaOsoba.setAll(BazaPodataka.getSveOsobe());
            } catch (SQLException | IOException throwables) {
                throwables.printStackTrace();
            }
        });
        clickMenu.getItems().add(mi1);
        clickMenu.getItems().add(delete);
        tablicaOsoba.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
            if(t.getButton() == MouseButton.SECONDARY) {
                clickMenu.show(tablicaOsoba, t.getScreenX(), t.getScreenY());
            }
        });

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
