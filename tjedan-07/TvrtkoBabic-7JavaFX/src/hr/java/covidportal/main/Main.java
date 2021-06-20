package hr.java.covidportal.main;

import hr.java.covidportal.model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main extends Application {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static Stage mainStage;

    private static final List<Zupanija> procitaneZupanije = Main2.unosZupanija();
    private static final List<Simptom> procitaniSimptomi = Main2.unosSimptoma();
    private static final List<Bolest> procitaneBolesti = Main2.unosBolesti(procitaniSimptomi);
    private static final List<Virus> procitaniVirusi = Main2.unosVirusa(procitaniSimptomi);
    private static final List<Osoba> procitaneOsobe = Main2.unosOsoba(procitaneZupanije, procitaneBolesti, procitaniVirusi);

    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.info("Pokenuta je aplikacija.");
        mainStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("pocetniEkran.fxml"));
        primaryStage.setTitle("Početni ekran");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void setMainStage(Stage newStage) {
        mainStage = newStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static List<Zupanija> getProcitaneZupanije() {
        return procitaneZupanije;
    }

    public static List<Simptom> getProcitaniSimptomi() {
        return procitaniSimptomi;
    }

    public static List<Bolest> getProcitaneBolesti() {
        return procitaneBolesti;
    }

    public static List<Virus> getProcitaniVirusi() {
        return procitaniVirusi;
    }

    public static List<Osoba> getProcitaneOsobe() {
        return procitaneOsobe;
    }
}
