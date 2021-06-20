package hr.java.covidportal.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.info("Pokenuta je aplikacija.");
        mainStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("pocetniEkran.fxml"));
        primaryStage.setTitle("Početni ekran");
        primaryStage.setScene(new Scene(root, 600, 450));
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
}