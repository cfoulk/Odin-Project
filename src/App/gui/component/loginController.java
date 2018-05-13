package App.gui.component;

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

    private JFXButton connectionStatus = null;

    private SVGGlyph connection;

    OdinModel OM = null;

    @FXML
    private StackPane stackPane;

    private JFXDialog popup;

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
            if (OM.equals(null)) {
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
                    usernameField.setText("");
                    passwordField.setText("");
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


    public void liveCheck() {
        new Thread(() -> {
            while (true) {
                checkConnection(new ActionEvent());
                try {
                    Thread.sleep((long) 2000);
                } catch (InterruptedException e) {
                    System.out.println("End");
                }
            }
        }).start();
    }

    void checkConnection(ActionEvent event) {
        try {
            if (OM == null) {
                OM = new OdinModel();
            }
            if(!OM.isClosed()) {
                connection.setStyle("-fx-background-color: #00E676");
                return;
            }
        } catch (SQLException e) {
        } catch (IOException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        connection.setStyle("-fx-background-color: #D32F2F");
    }

    public void switchToDashboard(int UserID) {
        try {
            if(OM != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("App/gui/Dashboard.fxml"));
                Parent dashboard = loader.load();
                persistentUser.PARENT_DASHBOARD = dashboard;
                DashboardController dashboardController = loader.getController();
                dashboardController.load(UserID, OM, connectionStatus);
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
        if(connectionStatus == null) {
            connection = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.Connection-Good");
            connection.setSize(25);
            connection.setFill(Color.valueOf("#9E9E9E"));
            connection.setStyle("-fx-cursor: hand");
            connectionStatus = new JFXButton();
            connectionStatus.setStyle("-jfx-mask-type: CIRCLE");
            connectionStatus.setGraphic(connection);
            connectionStatus.setOnAction(this::checkConnection);
            ((HBox) loginButton.getParent()).getChildren().add(connectionStatus);
        }

        logo.setSize(200);
        ObservableList list = contentArray.getChildren();
        contentArray.getChildren().add(0, logo);
        passwordField.addEventHandler(KeyEvent.KEY_PRESSED,
                event -> {
                    if (event.getCode().equals(KeyCode.ENTER)) {
                        login();
                    }
                });
        liveCheck();
        Platform.runLater(() -> {usernameField.requestFocus();
            Root = usernameField.getScene().getRoot();});

    }
}