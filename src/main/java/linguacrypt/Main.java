package linguacrypt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import linguacrypt.controller.MainController;
import linguacrypt.model.Clef;
import linguacrypt.model.Jeu;
import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

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
        mainControlleur.setPlateau();

        // Créer la scène et l'afficher
        Scene scene = new Scene(root, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("LinguaCrypt");
        primaryStage.setFullScreen(true);
        
        primaryStage.show();
    }

}
