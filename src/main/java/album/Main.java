package album;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        URL fxmlURL = getClass().getResource("/test.fxml");
        if (fxmlURL == null) {
            System.err.println("Could not find test.fxml");
            System.exit(1);
        }
        Parent root = FXMLLoader.load(fxmlURL);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX Bootstrap Project using FXML");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
