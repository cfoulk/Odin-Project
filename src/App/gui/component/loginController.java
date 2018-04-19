package App.gui.component;

import Model.OdinModel;
import com.jfoenix.controls.*;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.SQLException;

public class loginController {

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private JFXButton loginButton;

    @FXML
    private VBox contentArray;

    private JFXButton connectionStatus;

    private SVGGlyph connection;

    @FXML
    private StackPane stackPane;

    private JFXDialog popup;

    @FXML
    void login(ActionEvent event) {
        OdinModel a = null;
        try {
            a = new OdinModel();

            int userID = a.getUserID(usernameField.getText(), passwordField.getText());
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
                    break;
            }
            connection.setStyle("-fx-background-color: #00E676");
        }
        //No file
        catch (IOException e) {
            connection.setStyle("-fx-background-color: #D32F2F");
            e.printStackTrace();
        }
        //No connection
        catch (SQLException e) {
            switchToScene(event);
            connection.setStyle("-fx-background-color: #D32F2F");
            e.printStackTrace();
        }
    }

    void checkConnection(ActionEvent event) {
        try {
            new OdinModel();
            connection.setStyle("-fx-background-color: #00E676");
        } catch (SQLException e) {
            connection.setStyle("-fx-background-color: #D32F2F");
        } catch (IOException e) {
            connection.setStyle("-fx-background-color: #D32F2F");
        }
    }

    public void switchToScene(ActionEvent event){
        Window root = loginButton.getScene().getWindow();
        try {
            Parent dashboard = FXMLLoader.load(getClass().getResource("/App/gui/Dashboard.fxml"));
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(dashboard));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void initialize() throws Exception {
        //Throws Exception
        SVGGlyph logo = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.Odin01");
        connection = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.connection-good");
        connection.setSize(35);
        connection.setFill(Color.valueOf("#9E9E9E"));
        connection.setStyle("-fx-cursor: hand");

        logo.setFill(Color.WHITE);
//        SVGGlyph logo = new SVGGlyph(0, "Odin01", "M664.475 167.314l-0.669-8.871-151.805-87.703-151.805 87.703-0.669 8.871 152.476-88.205zM845.236 263.217l-76.154-44.018-0.669 8.033 69.626 40.169v376.586l-177.246 102.264-1.171 9.038 185.615-107.118zM185.959 643.988v-376.586l69.626-40.169-0.669-8.033-76.154 44.018v384.955l185.615 107.118-1.171-9.038zM823.812 635.786v-360.183l-56.404-32.637 16.235 30.127 22.428 12.888v339.429l-140.255 81.008-2.845 22.261zM217.927 625.409v-339.429l22.428-12.888 16.235-30.127-56.404 32.637v360.183l160.844 92.891-2.845-22.261zM668.157 206.311l-2.009-21.759-154.149-89.041-154.149 89.041-2.009 21.759 156.158-90.046zM392.161 240.79l-0.502 19.248 22.763-12.888-2.511-16.57zM429.485 268.573l1.339 18.913 19.081-10.21-3.682-18.746zM439.527 336.861l5.524 0.167 30.629 10.879-2.845 7.364-20.754 3.18-11.213-8.369zM631.837 240.79l0.502 19.248-22.763-12.888 2.511-16.57zM594.513 268.573l-1.339 18.913-19.081-10.21 3.682-18.746zM751.675 236.605l-8.536-4.017 7.866-3.013 0.669-156.492-55.065-41.843-17.406-55.4-46.696-11.213-6.695-22.261-41.843-6.36 96.908 170.886 4.351 152.476 12.553 14.897h-20.921l-9.54 14.729-1.171 45.19-28.955 35.316 18.579-38.998 1.339-61.426-6.193-33.641-41.843-21.759-49.542-17.908-47.534-6.695-47.534 6.695-49.542 17.908-41.676 21.926-6.193 33.641 1.339 61.426 18.579 38.998-29.123-35.316-1.171-45.19-9.54-14.729h-20.921l12.553-14.897 4.351-152.476 97.076-171.053-41.843 6.36-6.862 22.261-46.529 11.381-17.406 55.4-55.065 41.843 0.669 156.492 7.866 3.013-8.536 4.017-26.612 46.864 66.948 138.080 15.398-7.699 20.754 43.349 6.36-10.377-1.674-28.955 14.226-8.536-8.536 11.213 5.022 26.947-10.544 39.834 1.842 17.072 67.618 117.662 25.273-29.123-21.759-72.806-0.335-21.087 16.402-70.798-4.686-15.398-49.709-74.481 27.783-26.11 19.582-3.849 58.58 29.625 11.716-8.871h11.716l11.716 8.871 58.58-29.625 19.582 3.849 27.783 26.11-49.709 74.481-4.686 15.398 16.402 70.798-0.335 21.087-21.759 72.806 25.273 29.123 67.618-117.495 1.842-17.072-10.544-39.834 5.022-26.947-8.536-11.213 14.226 8.536-1.674 28.955 6.36 10.377 20.754-43.349 15.398 7.699 66.948-138.080-26.445-47.198zM385.968 272.423l1.339-33.307 28.955-15.231 4.351 27.114-34.646 21.424zM426.138 300.040l-1.842-35.316 26.612-14.897 5.691 32.972-30.461 17.239zM511.999 326.317l-29.791-46.362 29.791-34.814 29.791 34.814-29.791 46.362zM597.86 300.040l-30.461-17.239 5.691-32.972 26.612 14.897-1.842 35.316zM603.384 251l4.351-27.114 28.955 15.231 1.339 33.307-34.646-21.424zM511.999 252.506l-20.754 27.783 20.754 25.273 20.754-25.273zM568.068 486.491l-13.724-29.791-10.544-11.716-10.711-2.009-4.184 1.674-9.206 8.871-7.699-0.335-7.699 0.335-9.206-8.871-4.184-1.674-10.711 2.009-10.544 11.716-13.724 29.791-0.502 42.847 19.248-27.615 23.265-17.741 14.059-4.853 14.059 4.853 23.265 17.741 19.248 27.615zM521.874 491.847l-9.875-3.013-9.875 3.013-5.189 4.184 7.531 1.842 7.531 0.167 7.531-0.167 7.531-1.842zM474.34 781.065l-18.077-14.897-10.879-15.398 23.934-23.766 29.791-37.323-3.18 8.871-23.432 32.972-16.904 21.591 4.017 6.862 15.398 16.068 12.218 6.695 7.364 3.013h-8.033l-12.218-4.686zM503.966 763.658h16.068l12.051-2.845 7.531-4.351 3.18-3.682-1.842 5.524-8.202 5.524-11.884 4.853-8.704 1.674-8.704-1.674-11.884-4.853-8.202-5.524-1.842-5.524 3.18 3.682 7.531 4.351 11.716 2.845zM536.435 852.198l-9.875 7.866-10.21 11.884-2.845 13.724-1.506 16.57-1.506-16.57-2.845-13.724-10.21-11.884-9.875-7.866-0.669-3.18 6.527-7.197 4.017-9.038-5.691-5.022 1.004-9.206 4.184-5.524-2.845-8.369 1.842-8.033 6.695-4.853 4.519 2.009c0 0-6.527 4.686-6.36 4.686s-1.506 6.026-1.506 6.026l2.511 7.364 7.866 3.849 2.511 3.347 2.511-3.347 7.866-3.849 2.511-7.364c0 0-1.674-6.026-1.506-6.026s-6.36-4.686-6.36-4.686l4.519-2.009 6.695 4.853 1.842 8.033-2.845 8.369 4.184 5.524 1.004 9.206-5.691 5.022 4.017 9.038 6.527 7.197-1.004 3.18zM567.734 766.168l-18.077 14.897-12.218 4.853h-8.033l7.364-3.013 12.218-6.695 15.398-16.068 4.017-6.862-16.904-21.591-23.432-32.972-3.18-8.871 29.791 37.323 23.934 23.766-10.879 15.231zM535.431 651.519l-7.531 8.536-4.351 0.837 22.763-37.826-9.373-9.206-14.394 11.884 0.837-3.347 8.871-12.218-16.068-15.733-4.184 7.531-4.184-7.531-16.068 15.733 8.871 12.218 0.837 3.347-14.394-11.884-9.373 9.206 22.763 37.826-4.351-0.837-7.531-8.536-50.211 55.065 5.356 23.934-12.72-24.101 52.889-61.593-12.051-16.402-34.646 30.127 4.184-8.033 66.781-65.777 4.017-33.307 4.017 33.307 66.781 65.777 4.184 8.033-34.646-30.127-12.051 16.402 52.889 61.593-12.72 24.101 5.356-23.934-50.546-55.065zM615.268 882.994l12.386-79.334 31.466-115.486 28.453-79.836-23.766-52.555-11.549 11.381 24.604 42.345-14.561 22.763-9.875 17.741 4.017-17.574 11.381-21.591-13.892-20.419-8.369-18.579-24.771 42.68 22.093 19.248-18.913 30.963-7.866 17.741 3.18-19.917 13.724-27.281-16.737-13.055-11.716 19.248-38.663-47.701 4.519-13.892-43.349-42.178 0.502-26.779 5.691 21.926 40.504 38.663 12.888-49.542v-25.106l-8.536-5.858 1.339 30.963-9.708 31.299-8.369-19.75-18.746-28.621-7.029-4.017-14.226 5.022-9.373 0.837-9.373-0.837-14.226-5.022-7.029 4.017-18.746 28.621-8.369 19.75-9.708-31.299 1.339-30.963-8.536 5.858v25.106l12.888 49.542 40.504-38.663 5.691-21.926 0.502 26.779-43.349 42.178 4.519 13.892-38.663 47.701-11.716-19.248-16.737 13.055 13.724 27.281 3.18 19.917-7.866-17.741-18.913-30.963 22.093-19.248-24.771-42.847-8.369 18.579-13.892 20.419 11.381 21.591 4.017 17.574-9.875-17.741-14.561-22.763 24.604-42.345-11.549-11.381-23.766 52.555 28.453 79.836 31.466 115.486 12.386 79.334 15.064 9.708 2.511-27.281-1.506-35.818-13.222-50.881-19.582-68.957 6.695 11.213 20.586 57.242 14.561 49.207 2.511 39.667-1.171 32.805 22.261 14.226 2.343-6.695 1.171-29.625-2.343-26.445-1.674-7.531 4.686 7.197 5.524 26.779v31.801l-1.339 10.711 25.608 22.261 2.678-18.244-0.167-16.737-3.849-22.428 4.351 8.704 3.682 13.892 2.511 18.579v24.268l11.046 9.54 11.046-9.54v-24.268l2.511-18.579 3.682-13.892 4.351-8.704-3.849 22.428-0.167 16.737 2.678 18.244 25.608-22.261-1.339-10.711v-31.801l5.524-26.779 4.686-7.197-1.674 7.531-2.343 26.445 1.171 29.625 2.343 6.695 22.261-14.226-1.171-32.805 2.511-39.667 14.561-49.207 20.586-57.242 6.695-11.213-18.913 69.124-13.222 50.881-1.506 35.818 2.511 27.281 15.064-9.708zM524.050 816.213l-7.699 5.189-4.351 4.351-4.351-4.351-7.699-5.189-4.184 5.022v5.356l8.202 5.189 5.356 4.519 2.678 4.184 2.678-4.184 5.356-4.519 8.202-5.189v-5.356zM525.389 844.331l-2.845-8.369-6.026 4.853-4.519 7.531-4.519-7.531-6.026-4.853-2.845 8.369-5.022 5.189 8.202 6.862 8.871 9.038 1.339 6.193 1.339-6.193 8.871-9.038 8.202-6.862zM584.471 336.861l-5.524 0.167-30.629 10.879 2.845 7.364 20.754 3.18 11.213-8.369zM600.873 369.666l24.268-28.119-8.536-2.845-5.858 3.682-11.549 2.678 4.017-8.871-1.674-13.724-12.553-3.013-53.558 24.939-4.184 6.862-0.167 22.93 4.351 20.252-11.213-16.737-1.171-34.479-6.862-5.356h-8.369l-6.862 5.356-1.171 34.479-11.213 16.737 4.351-20.252-0.167-22.93-4.184-6.862-53.391-24.939-12.553 3.013-1.674 13.724 4.017 8.871-11.549-2.678-5.858-3.682-8.536 2.845 24.268 28.119 18.411 9.54 13.892 3.347-0.837 3.515-12.888 1.842-4.017 4.686 0.837 6.862 10.377 13.557 3.013 13.557-2.511 21.424-9.875 36.989 8.704-5.022 12.051-27.281 13.39-14.897-0.837-12.218 12.888-13.557 14.394-21.424-9.54 24.771-10.711 11.549 1.171 7.531 9.875-2.009 8.871 3.18 6.527 7.364 5.691 0.837 5.691-0.837 6.527-7.364 8.871-3.18 9.875 2.009 1.171-7.531-10.711-11.549-9.54-24.771 14.394 21.424 12.888 13.557-0.837 12.218 13.39 14.897 12.051 27.281 8.704 5.022-9.875-36.989-2.511-21.424 3.013-13.557 10.377-13.557 0.837-6.862-4.017-4.686-12.888-1.842-0.837-3.515 13.892-3.347 18.244-9.54zM476.349 358.62l-6.695 3.013-17.908 2.343-9.038-3.849-6.695-8.536-1.674-18.579 11.213-0.335 30.127 10.711 5.022 4.853-4.351 10.377zM524.552 415.024l-8.033-1.506-4.519-0.335-4.519 0.335-8.033 1.506 8.202-5.356h8.704l8.202 5.356zM572.42 363.975l-17.908-2.343-6.695-3.013-4.351-10.21 5.022-4.853 30.127-10.711 11.213 0.335-1.674 18.579-6.695 8.536-9.038 3.682z", Color.WHITE);
        connectionStatus = new JFXButton();
        connectionStatus.setStyle("-jfx-mask-type: CIRCLE");
//        connectionStatus.setShape();
        connectionStatus.setGraphic(connection);

        connectionStatus.setOnAction(this::checkConnection);

        ((HBox) loginButton.getParent()).getChildren().add(connectionStatus);

        logo.setSize(200);
        ObservableList list = contentArray.getChildren();

//        contentArray.getChildren().add(0, button);
        contentArray.getChildren().add(0, logo);

//        usernameField.requestFocus(); //Doesn't work
        Platform.runLater(() -> usernameField.requestFocus());
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                usernameField.requestFocus();
//            }
//        });

    }


}