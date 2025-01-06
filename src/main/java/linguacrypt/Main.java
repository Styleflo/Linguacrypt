package linguacrypt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import linguacrypt.model.Jeu;

import linguacrypt.controller.*;
import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        // Créer une instance de Jeu

        // Charger le fichier FXML principal
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
        Parent root = mainLoader.load();

        MainController mainControlleur = mainLoader.getController();


        // Créer la scène et l'afficher
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX Album Project");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
