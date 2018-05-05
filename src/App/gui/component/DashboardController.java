package App.gui.component;

import App.gui.persistentUser;
import Model.OdinModel;
import Server.Employee;
import Server.Project;
import Server.Task;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.sun.deploy.xml.XMLable;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
    private VBox View;

    @FXML
    private StackPane stackPane;

    public double heightHeader;

    private HBox projectLineButtons = new HBox();

    public void initialize() throws Exception {
        UserBar.getChildren().add(createIconButton("Message", "Messenger"));
        UserBar.getChildren().add(createIconButton("Gear", "Settings"));
        UserBar.getChildren().add(createIconButton("Exit", "Logout"));

        persistentUser.initiateSampleData();

        OdinModel b = new OdinModel();

//        List<Project> a = persistentUser.projectList;
        List<Project> a = b.getProjects();


        initProjectButtons();

        initView(a);

        heightHeader = 0.162;
        p(heightHeader);
        p(splitPane.getDividers().get(0).getPosition());
        splitPane.setDividerPosition(0,heightHeader);
        p(splitPane.getDividers().get(0).getPosition());

    }


    //Should initialize ProjectButtons based on PRIVILEGES
    public void initProjectButtons() throws Exception {
        projectLineButtons.getChildren().add(createIconButton("View","View Project"));
        projectLineButtons.getChildren().add(createIconButton("Group-Info","Assigned Employees"));
        projectLineButtons.getChildren().add(createIconButton("ArrowheadHollow-Down","Expand"));
        projectLineButtons.getStyleClass().add("projectLineButtons");
        HBox.setHgrow(projectLineButtons,Priority.ALWAYS);
    }

    //Should initialize view (with collapsed projects)
    private void initView(List<Project> projects){
        for(int i = 0; i < projects.size();i++ ){
            View.getChildren().add(createProjectLine(projects.get(i)));
        }
    }

    //Creates a project line
    public HBox createProjectLine(Project project){
        //Start project line with Project name
        HBox projectLine = new HBox(new Label(project.name));
        projectLine.getStyleClass().add("projectLine");
//        projectLine.hoverProperty().addListener(ChangeListener<>{});
        //add Listener
        EventHandler a = new EventHandler() {
            @Override
            public void handle(Event event) {
                if(event.getEventType().equals(MouseEvent.MOUSE_ENTERED)){
                    projectLine.getChildren().add(projectLineButtons);
                }
                if(event.getEventType().equals(MouseEvent.MOUSE_EXITED)){
                    projectLine.getChildren().remove(projectLineButtons);
                }
            }
        };
        projectLine.addEventHandler(MouseEvent.MOUSE_ENTERED,a);
        projectLine.addEventHandler(MouseEvent.MOUSE_EXITED,a);
        return projectLine;
    }

    //Create a task line
    public HBox createTaskLine(Task task){
        return null;
    }

    //Create a Worklog line
    public HBox createWorkLogLine(Task task){
        return null;
    }



    private JFXRippler createIconButton(String iconName, String tooltip) throws Exception {

        SVGGlyph glyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg."+iconName);
        StackPane pane = new StackPane();

        glyph.setSize(27);
        glyph.setFill(Color.valueOf("#FFFFFF"));
        pane.getChildren().add(glyph);
        pane.setPadding(new Insets(10));

        JFXRippler rippler = new JFXRippler(pane);
        rippler.getRipplerRadius();
        rippler.getStyleClass().add("icon-rippler");
//        rippler.setRipplerFill(Color.valueOf("#254d87"));

        if(tooltip != null || tooltip != "") {
            Tooltip toolTip = new Tooltip(tooltip);
            Tooltip.install(rippler,toolTip);
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
