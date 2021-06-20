package hr.java.covidportal.controller;

import hr.java.covidportal.database.BazaPodatakaNiti;
import hr.java.covidportal.main.Main;
import hr.java.covidportal.model.Zupanija;
import hr.java.covidportal.niti.NajviseZarazenihNit;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class PretragaZupanijaController implements Initializable {

    private static ObservableList<Zupanija> observableListaZupanija;
    private static List<Zupanija> procitaneZupanije;

    @FXML
    private TextField nazivZupanije;
    @FXML
    private TableView<Zupanija> tablicaZupanija;
    @FXML
    private TableColumn<Zupanija, String> stupacNazivZupanije;
    @FXML
    private TableColumn<Zupanija, Integer> stupacBrojStanovnika;
    @FXML
    private TableColumn<Zupanija, String> stupacBrojZarazenih;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        procitaneZupanije = BazaPodatakaNiti.getSveZupanije();

        stupacNazivZupanije.setCellValueFactory(new PropertyValueFactory<Zupanija, String>("naziv"));
        stupacBrojStanovnika.setCellValueFactory(new PropertyValueFactory<Zupanija, Integer>("brStanovnika"));
        stupacBrojZarazenih.setCellValueFactory(new PropertyValueFactory<Zupanija, String>("brZarazenih"));

        List<Zupanija> listaZupanija = new ArrayList<>(procitaneZupanije);
        observableListaZupanija = FXCollections.observableList(listaZupanija);
        tablicaZupanija.setItems(observableListaZupanija);

        NajviseZarazenihNit nit = new NajviseZarazenihNit();
        ExecutorService executorServiceNajviseZarazenih = Executors.newCachedThreadPool();
        executorServiceNajviseZarazenih.execute(nit);
        executorServiceNajviseZarazenih.shutdown();

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            Zupanija z = NajviseZarazenihNit.getZupanijaNajveciPostotakZarazenih();
            Main.getMainStage().setTitle(z.getNaziv() + ": " + z.getPostotakZarazenih());
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public void pretraga() {
        String trazenString = nazivZupanije.getText().toLowerCase();

        List<Zupanija> trazeneZupanije = procitaneZupanije.stream()
                .filter(z -> z.getNaziv().toLowerCase().contains(trazenString))
                .collect(Collectors.toList());

        observableListaZupanija.clear();
        observableListaZupanija.addAll(FXCollections.observableList(trazeneZupanije));
    }

    public static ObservableList<Zupanija> getObservableListaZupanija() {
        return observableListaZupanija;
    }

    public static void setObservableListaZupanija(ObservableList<Zupanija> observableListaZupanija) {
        PretragaZupanijaController.observableListaZupanija = observableListaZupanija;
    }
}
