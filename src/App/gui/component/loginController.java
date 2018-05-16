package App.gui.component;

import App.gui.component.util.ConnectionStatus;
import App.gui.persistentUser;
import Model.OdinModel;
import com.jfoenix.controls.*;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.SQLException;

public class loginController {

    public Parent Root;

    private JFXDecorator decorator;

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private JFXButton loginButton;

    @FXML
    private VBox contentArray;

    OdinModel OM = null;

    @FXML
    private StackPane stackPane;

    private JFXDialog popup;

    ConnectionStatus StatusIcon;

    @FXML
    void login(ActionEvent event) {
        login();
    }

    //Used to pass in decorator to controller (Used by main)
    public void setDecorator(JFXDecorator decorator) {
        this.decorator = decorator;
    }

    void login() {
        try {
            if (OM == null) {
                OM = new OdinModel();
            }

            int userID = OM.getUserID(usernameField.getText(), passwordField.getText());
            switch (userID) {
                case -2:
                    System.out.println("Wrong Password");
                    usernameField.setStyle("-fx-background-color: #FFFFFF");
                    passwordField.setStyle("-fx-background-color: #FFCDD2");
                    break;
                case -1:
                    System.out.println("Wrong User");
                    usernameField.setStyle("-fx-background-color: #FFCDD2");
                    passwordField.setStyle("-fx-background-color: #FFCDD2");
                    break;
                default:
                    System.out.println("Success");
                    //Clear text after login
                    usernameField.setText("");
                    usernameField.setStyle("-fx-background-color: #FFFFFF");
                    passwordField.setText("");
                    passwordField.setStyle("-fx-background-color: #FFFFFF");
                    switchToDashboard(userID);
                    break;
            }
        }

        //No file
        catch (IOException e) {
            e.printStackTrace();
        }
        //No connection
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void switchToDashboard(int UserID) {
        try {
            if(OM != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("App/gui/Dashboard.fxml"));
                Parent dashboard = loader.load();
                persistentUser.PARENT_DASHBOARD = dashboard;
                DashboardController dashboardController = loader.getController();
                dashboardController.load(UserID, OM);
                dashboard.getStylesheets().add("App/gui/resource/css/odin_scheme.css");
                decorator.setContent(dashboard);
                //Resize window to the new scene
                decorator.getScene().getWindow().sizeToScene();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() throws Exception {
        //Throws Exception
        SVGGlyph logo = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.Odin");
        logo.setFill(Color.WHITE);
        try {
            OM = new OdinModel();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        new ConnectionStatus(OM);

        StatusIcon = new ConnectionStatus(OM);
        ((HBox) loginButton.getParent()).getChildren().add(StatusIcon.getIcon());
        System.out.println(Thread.activeCount());
        logo.setSize(200);
        ObservableList list = contentArray.getChildren();
        contentArray.getChildren().add(0, logo);
        passwordField.addEventHandler(KeyEvent.KEY_PRESSED,
                event -> {
                    if (event.getCode().equals(KeyCode.ENTER)) {
                        login();
                    }
                });
        Platform.runLater(() -> {usernameField.requestFocus();
            Root = usernameField.getScene().getRoot();});

    }
}