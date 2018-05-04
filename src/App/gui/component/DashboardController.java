package App.gui.component;

import App.gui.persistentUser;
import Server.Employee;
import Server.Project;
import com.jfoenix.controls.*;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javax.tools.Tool;

import java.util.List;

public class DashboardController {

    @FXML
    private HBox UserBar;

    @FXML
    private SplitPane splitPane;

    @FXML
    private Text Username;

    @FXML
    private StackPane stackPane;

    public double heightHeader;

    public void initialize() throws Exception {
        UserBar.getChildren().add(createIconButton("Message", "Messenger"));
        UserBar.getChildren().add(createIconButton("Gear", "Settings"));
        UserBar.getChildren().add(createIconButton("Exit", "Logout"));

        persistentUser.initiateSampleData();

        List<Project> a = persistentUser.projectList;


        heightHeader = 0.162;
        p(heightHeader);
        p(splitPane.getDividers().get(0).getPosition());
        splitPane.setDividerPosition(0,heightHeader);
        p(splitPane.getDividers().get(0).getPosition());

    }

    private JFXRippler createIconButton(String iconName, String message) throws Exception {

        SVGGlyph glyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg."+iconName);
        StackPane pane = new StackPane();

        glyph.setSize(30);
        glyph.setFill(Color.valueOf("#FFFFFF"));
        pane.getChildren().add(glyph);
        pane.setPadding(new Insets(11));

        JFXRippler rippler = new JFXRippler(pane);
        rippler.getRipplerRadius();
        rippler.getStyleClass().add("icon-rippler");
//        rippler.setRipplerFill(Color.valueOf("#254d87"));

        if(message != null || message != "") {
            Tooltip tooltip = new Tooltip(message);
            Tooltip.install(rippler,tooltip);
        }



        return rippler;
    }

    //Easy Debug print statement
    private void p(Object a){ System.out.println(a); }

    @FXML
    void createEmployee(ActionEvent event)
    {
        Employee newEmployee = null;
        loadEmployeeDialog(newEmployee);
    }

    @FXML
    void editEmployee(ActionEvent event)
    {
        Employee oldEmployee = null;//new Employee(1,2, "Jim", "Manager", "SlimJim", "snap", "Active");
        loadEmployeeDialog(oldEmployee);
    }

    void loadEmployeeDialog(Employee employee)
    {
        JFXDialogLayout content = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXTextField    name = new JFXTextField(),
                        position = new JFXTextField(),
                        groupID = new JFXTextField(),
                        username = new JFXTextField(),
                        password = new JFXTextField();
        JFXButton confirm = new JFXButton("Confirm");
        JFXButton cancel = new JFXButton("Cancel");
        name.setPromptText("Name");
        position.setPromptText("Position");
        groupID.setPromptText("Group Number");
        username.setPromptText("Username");
        password.setPromptText("Password");
        if(employee != null)
        {
            content.setHeading(new Text("Edit Employee"));
            name.setText(employee.name);
            position.setText(employee.position);
            groupID.setText(Integer.toString(employee.groupID));
            username.setText(employee.username);
            password.setText(employee.password);
            confirm.setOnAction(event -> dialog.close());
        }
        else {
            content.setHeading(new Text("Add Employee"));
            confirm.setOnAction(event -> dialog.close());
        }
        cancel.setOnAction(event -> dialog.close());
        content.setBody(new VBox(name, position, groupID, username, password));
        content.setActions(confirm, cancel);
        dialog.show();
        /*
        JFXDialogLayout content = new JFXDialogLayout();
        VBox elements = new VBox();
        content.setHeading(new Text("OdinDialog"));
        JFXTextField name = new JFXTextField("Name");
        JFXTextField username = new JFXTextField("Username");
        JFXButton exit = new JFXButton("Exit");
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        content.setActions(exit);
        content.setBody(new VBox(name,username));
        dialog.show();
        */

    }
}
