package linguacrypt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import linguacrypt.model.Plateau;
import linguacrypt.utils.WordsFileHandler;
import linguacrypt.model.Jeu;

import linguacrypt.controller.*;
import linguacrypt.model.Plateau;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        // Créer une instance de Jeu
        Jeu jeu = new Jeu();

        // Charger le fichier FXML principal
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
        Parent root = mainLoader.load();

        // On set le mainControlleur
        MainController mainControlleur = mainLoader.getController();
        jeu.addObserver(mainControlleur);
        mainControlleur.setJeu(jeu);

        // On set les autres vues
        mainControlleur.setMenuInitial();
        mainControlleur.setCartes();


        // Créer la scène et l'afficher
        Scene scene = new Scene(root, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX Album Project");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
