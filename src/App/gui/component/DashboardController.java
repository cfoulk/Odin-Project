package App.gui.component;

import App.gui.persistentUser;
import Model.OdinModel;
import Server.Employee;
import Server.Project;
import Server.Task;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import javafx.event.Event;
import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

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

    public int selectedProject;


    private HBox taskLineButtons = new HBox();
    private HBox projectLineButtons = new HBox();

    private List<Project> Projects;

    private List<Task> Tasks;

    public void initialize() throws Exception {
        UserBar.getChildren().add(createIconButton("Message", "Messenger"));
        UserBar.getChildren().add(createIconButton("Gear", "Settings"));
        UserBar.getChildren().add(createIconButton("Exit", "Logout"));

        persistentUser.initiateSampleData();

//        Projects = persistentUser.projectList;
//        Tasks = persistentUser.taskList;

        OdinModel b = new OdinModel();
        Projects = b.getProjects();
        Tasks = b.getTasks();

        initTaskButtons();

        initView();

        heightHeader = 0.162;
//        p(heightHeader);
//        p(splitPane.getDividers().get(0).getPosition());
        splitPane.setDividerPosition(0,heightHeader);
//        p(splitPane.getDividers().get(0).getPosition());

    }

    //Should initialize taskButtons based on PRIVILEGES of User
    public void initTaskButtons() {
        taskLineButtons.getChildren().add(createIconButton("View","View Project"));
        taskLineButtons.getChildren().add(createIconButton("Group-Info","Assigned Employees"));
        taskLineButtons.getChildren().add(createIconButton("Arrowhead-Down","Expand"));
        taskLineButtons.getStyleClass().add("projectLineButtons");
        HBox.setHgrow(taskLineButtons,Priority.ALWAYS);
    }

    //Should initialize ProjectButtons based on PRIVILEGES of User
    public HBox initProjectControlButtons(HBox projectLine) {
        HBox projectLineButtons = new HBox();
        projectLineButtons.getChildren().add(createIconButton("View","View Project"));
        projectLineButtons.getChildren().add(createIconButton("Group-Info","Assigned Employees"));


        JFXRippler expand = createIconButton("Arrowhead-Down","Expand");
        //If already collapsed rotate button to collapse position
        if(projectIsCollapsed(projectLine)){
            expand.setRotate(180);
        }
        expand.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
            double rotate = expand.getRotate();
            p(rotate);
            if(rotate == (double) 0 && showTasks((HBox) expand.getParent().getParent())) {
                expand.setRotate(180);
            }
            else if(rotate == (double) 180){
                closeTasks((HBox) expand.getParent().getParent());
                expand.setRotate(0);
            }
        });
        projectLineButtons.getChildren().add(expand);

        projectLineButtons.getStyleClass().add("projectLineButtons");
        HBox.setHgrow(projectLineButtons,Priority.ALWAYS);
        this.projectLineButtons = projectLineButtons;
        return projectLineButtons;
    }

    //Should initialize view (with collapsed projects)
    private void initView(){
        for(int i = 0; i < Projects.size();i++ ){
            HBox projectline = createProjectLine(Projects.get(i));
            //Set id of Hbox as related to the array list
            projectline.setId(Integer.toString(i));
            View.getChildren().add(projectline);
        }
    }

    private boolean projectIsCollapsed(HBox projectLine){
        Object nxtItem;
        int index;
        if((index = View.getChildren().indexOf(projectLine))+1 < View.getChildren().size() && (nxtItem = View.getChildren().get(index+1)) instanceof  VBox){
            return true;
        }
        return false;
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
                    projectLine.getChildren().add(initProjectControlButtons(projectLine));
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

    public void closeTasks(HBox projectLine){
        Object nxtItem;
        int index;
        if((index = View.getChildren().indexOf(projectLine)) < View.getChildren().size()+1 && (nxtItem = View.getChildren().get(index+1)) instanceof  VBox){
            View.getChildren().remove(nxtItem);
        }
    }

    //Will show the tasks in the project
    public boolean showTasks(HBox projectLine){
        VBox taskBox = new VBox();

        Project project = Projects.get(Integer.parseInt(projectLine.getId()));

        int projID = project.projectID;

        for(int i = 0; i < Tasks.size(); i++){
            if(projID == Tasks.get(i).projectID){
                HBox taskLine = createTaskLine(Tasks.get(i));
                taskBox.getChildren().add(taskLine);
                taskLine.setId(String.valueOf(i));
            }
        }
        taskBox.setPadding(new Insets(0,5,0,40));
        taskBox.setSpacing(2);
        if(taskBox.getChildren().size() != 0) {
            View.getChildren().add(View.getChildren().indexOf(projectLine) + 1, taskBox);
            return true;
        }
        return false;

    }

    //Create a task line
    public HBox createTaskLine(Task task){
        //Start task line with Project name
        HBox taskLine = new HBox(new Label(task.name));
        taskLine.getStyleClass().add("taskLine");

        //add Listener
        EventHandler a = new EventHandler() {
            @Override
            public void handle(Event event) {
                if(event.getEventType().equals(MouseEvent.MOUSE_ENTERED)){
                    taskLine.getChildren().add(taskLineButtons);
                }
                if(event.getEventType().equals(MouseEvent.MOUSE_EXITED)){
                    taskLine.getChildren().remove(taskLineButtons);
                }
                if(event.getEventType().equals(MouseEvent.MOUSE_CLICKED)){

                }
            }
        };
        taskLine.addEventHandler(MouseEvent.MOUSE_ENTERED,a);
        taskLine.addEventHandler(MouseEvent.MOUSE_EXITED,a);
        return taskLine;
    }

    //TODO
    public VBox workLog(){
        return null;
    }

    //Create a Worklog line
    public HBox createWorkLogLine(Task task){
        return null;
    }



    private JFXRippler createIconButton(String iconName, String tooltip) {

        Node glyph = null;

        //We will try and load glyph. If not available replace glyph with text
        try {
            glyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg."+iconName);
            ((SVGGlyph)glyph).setSize(27);
            ((SVGGlyph)glyph).setFill(Color.valueOf("#FFFFFF"));
        } catch (Exception e) {
            p("Glyph does not exist!");
            glyph = new Text(iconName);
            ((Text) glyph).setFill(Color.valueOf("#FFFFFF"));
        }

        StackPane pane = new StackPane();
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

    void loadEmployeeDialog(Employee employee) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.getStyleClass().add("dialog");
        content.lookup(".jfx-layout-actions").setStyle("-fx-alignment: CENTER; -fx-spacing: 100");
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXTextField    name = new JFXTextField(),
                        position = new JFXTextField(),
                        groupID = new JFXTextField(),
                        username = new JFXTextField(),
                        password = new JFXTextField();


        JFXRippler confirm = createIconButton("Check", "Save");
        JFXRippler cancel = createIconButton("Cancel", "Cancel");

        //TODO JOEL Validator for String vs. Integer
        name.setPromptText("Name");
        position.setPromptText("Position");
        groupID.setPromptText("Group Number");
        username.setPromptText("Username");
        password.setPromptText("Password");

        Text text = new Text();
        text.setFill(Paint.valueOf("#FFFFFF"));
        text.setStyle("-fx-font: bold 16px \"System\" ;");

        if(employee != null)
        {
            text.setText("Edit Employee");
            content.setHeading(text);
            name.setText(employee.name);
            position.setText(employee.position);
            groupID.setText(Integer.toString(employee.groupID));
            username.setText(employee.username);
            password.setText(employee.password);
            confirm.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> dialog.close());
        }
        else {
            text.setText("Add Employee");
            content.setHeading(text);
            confirm.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> dialog.close());
        }
        cancel.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> dialog.close());
        VBox vBox = new VBox(name, position, groupID, username, password);
        vBox.setStyle("-fx-spacing: 15");
        content.setBody(vBox);
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
