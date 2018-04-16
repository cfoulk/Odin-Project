package App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("App/gui/Login.fxml"));
        primaryStage.setTitle("Odin Management");
        primaryStage.setScene(new Scene(root));
        root.getStylesheets().add("App/gui/resource/css/odin_scheme.css");
        primaryStage.show();
    }
}
