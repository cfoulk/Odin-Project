package App.gui.component;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import Model.OdinModel;

public class loginController {

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private JFXButton loginButton;


    @FXML
    void login(ActionEvent event) {

        OdinModel a = new OdinModel();

        //TODO
        if(a.getUserID(usernameField.getText(), passwordField.getText()) != -1){
            System.out.println("Success");
        }
        else
            System.out.println("Wrong User");
    }

    public void initialize(){

    }




}