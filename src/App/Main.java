package App;

import App.gui.component.loginController;
import App.gui.persistentUser;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class Main extends Application {
    public void start(Stage primaryStage) throws Exception {
        //Load icons
        new Thread(() -> {
            try {
                //Prefix is the prefix to the name to be called (e.g. icomoon.svg.OdinLogo)
                SVGGlyphLoader.loadGlyphsFont(Main.class.getResourceAsStream("/font/icomoon.svg"),
                        "icomoon.svg");
            } catch (IOException ioExc) {
                ioExc.printStackTrace();
            }
        }).start();

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("App/gui/Login.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Odin Management");
        JFXDecorator decorator = new JFXDecorator(primaryStage, root);
        persistentUser.DECORATOR = decorator;
        persistentUser.PARENT_LOGIN = root;
        primaryStage.setScene(new Scene(decorator));
        decorator.setContent(root);

        ((loginController) loader.getController()).setDecorator(decorator);

        //Addisng style classes
        root.getStylesheets().add("App/gui/resource/css/odin_scheme.css");
        decorator.getStylesheets().add("App/gui/resource/css/odin_scheme.css");
        //'X' will close java application
        decorator.setOnCloseButtonAction(() -> {
            System.exit(0);
        });

        decorator.getScene().getWindow().sizeToScene();

        primaryStage.show();
    }
}
