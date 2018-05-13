package App.gui.component;

import App.gui.persistentUser;
import Model.OdinModel;
import Server.Employee;
import Server.Project;
import Server.Task;
import Server.WorkLog;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.mysql.jdbc.StringUtils;
import javafx.application.Platform;
import javafx.event.Event;
import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DashboardController {

    static Employee User;

    static OdinModel OM;

    @FXML
    private VBox HeaderVBox;

    @FXML
    private HBox UserBar;

    @FXML
    private SplitPane splitPane;

    @FXML
    private Text UserName;

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

    private List<WorkLog> Worklogs;

    private List<Employee> Employees;

    private JFXButton connectionStatus = null;

    public boolean load(int UserID, OdinModel OM, JFXButton connectionStatus) {
        User = OM.getEmployee_EmployeeID(UserID);
        this.OM = OM;
        this.connectionStatus = connectionStatus;
        return true;
    }

    public void initialize() throws Exception {
        Platform.runLater(() -> {
            Projects = OM.getProjects();
            Tasks = OM.getTasks();
            Worklogs = OM.getWorkLogs();
            Employees = OM.getEmployees();
            initHeader();
            initView();
        });

        heightHeader = 0.162;
//        p(heightHeader);
//        p(splitPane.getDividers().get(0).getPosition());
        splitPane.setDividerPosition(0, heightHeader);
//        p(splitPane.getDividers().get(0).getPosition());
    }

    public void initHeader(){
        UserName.setText("Hello, " + User.name);
        UserBar.getChildren().add(createIconButton("Message", "Messenger"));
        String privileges = User.position;
        if(privileges.equals("Manager")){
            JFXRippler manageEmployeeButton = createIconButton("Group", "Manage Employees");
            manageEmployeeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> loadEmployeeWindow(new Stage(), User, Employees));
            UserBar.getChildren().add(manageEmployeeButton);
            p(1);
        }
        else if(privileges.equals("Project Lead")){
            p(2);
        }
        else if(privileges.equals("Employee")){
            p(3);
        }

        JFXRippler logOut = createIconButton("Exit", "Logout");
        JFXRippler refresh = createIconButton("Refresh", "Refresh");

        logOut.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> switchToLogin(new ActionEvent()));

        HBox rightAligned = new HBox(refresh, logOut);
        if(connectionStatus != null){
            rightAligned.getChildren().add(1,connectionStatus);
        }
        HBox.setHgrow(rightAligned, Priority.ALWAYS);
        rightAligned.getStyleClass().add("lineButtons");
        UserBar.getChildren().add(rightAligned);


    }


    //Should initialize ProjectButtons based on PRIVILEGES of User
    public HBox initProjectControlButtons(HBox projectLine) {
        HBox projectLineButtons = new HBox();
//        projectLineButtons.getChildren().add(createIconButton("View", "View Project"));
//        projectLineButtons.getChildren().add(createIconButton("Group-Info", "Assigned Employees"));


        JFXRippler expand = createIconButton("Arrowhead-Down", "Expand");
        //If already collapsed rotate button to collapse position
        if (projectIsCollapsed(projectLine)) {
            expand.setRotate(180);
        }
        expand.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
            double rotate = expand.getRotate();
            if (rotate == (double) 0 && showTasks((HBox) expand.getParent().getParent())) {
                expand.setRotate(180);
            } else if (rotate == (double) 180) {
                closeTasks((HBox) expand.getParent().getParent());
                expand.setRotate(0);
            }
        });
        projectLineButtons.getChildren().add(expand);

        projectLineButtons.getStyleClass().add("lineButtons");
        HBox.setHgrow(projectLineButtons, Priority.ALWAYS);
        this.projectLineButtons = projectLineButtons;
        return projectLineButtons;
    }

    //Should initialize view (with collapsed projects)
    private void initView() {
        for (int i = 0; i < Projects.size(); i++) {
            //TODO show only tasks that are w/ Empl ID or privelege
            Project project = Projects.get(i);
            HBox projectline = createProjectLine(project);
            //Set id of Hbox as related to the array list
            projectline.setId(Integer.toString(project.projectID));
            View.getChildren().add(projectline);
        }
        //Adds Create button TODO privelage base
        HBox newProject = new HBox(createIconButton("Add", "Add Project"),new Label("Add Project"));
        newProject.getStyleClass().add("projectLine");
        View.getChildren().addAll(newProject);
    }

    private boolean projectIsCollapsed(HBox projectLine) {
        int index;
        if ((index = View.getChildren().indexOf(projectLine)) + 1 < View.getChildren().size() && View.getChildren().get(index + 1) instanceof VBox) {
            return true;
        }
        return false;
    }

    //Creates a project line
    public HBox createProjectLine(Project project) {
        //Start project line with Project name
        HBox projectLine = new HBox(new Label(project.name));
        projectLine.getStyleClass().add("projectLine");
//        projectLine.hoverProperty().addListener(ChangeListener<>{});
        //add Listener
        EventHandler a = new EventHandler() {
            @Override
            public void handle(Event event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_ENTERED)) {
                    projectLine.getChildren().add(initProjectControlButtons(projectLine));
                }
                if (event.getEventType().equals(MouseEvent.MOUSE_EXITED)) {
                    projectLine.getChildren().remove(projectLineButtons);
                }
            }
        };
        projectLine.addEventHandler(MouseEvent.MOUSE_ENTERED, a);
        projectLine.addEventHandler(MouseEvent.MOUSE_EXITED, a);
        return projectLine;
    }

    public void closeTasks(HBox projectLine) {
        Object nxtItem;
        int index;
        if ((index = View.getChildren().indexOf(projectLine)) < View.getChildren().size() + 1 && (nxtItem = View.getChildren().get(index + 1)) instanceof VBox) {
            View.getChildren().remove(nxtItem);
        }
    }

    //Will show the tasks in the project
    public boolean showTasks(HBox projectLine) {
        boolean status = false;
        VBox taskBox = new VBox();
        //Filters for tasks by project id
        int projID = Integer.parseInt(projectLine.getId());

        for (int i = 0; i < Tasks.size(); i++) {
            if (projID == Tasks.get(i).projectID) {
                Task task = Tasks.get(i);
                HBox taskLine = createTaskLine(task);
                taskBox.getChildren().add(taskLine);
                taskLine.setId(String.valueOf(task.taskID));
            }
        }
        taskBox.setPadding(new Insets(0, 5, 0, 40));
        taskBox.setSpacing(2);
        if (taskBox.getChildren().size() != 0) {
            View.getChildren().add(View.getChildren().indexOf(projectLine) + 1, taskBox);
            status = true;
        }

        //Adds Create button TODO privelage base
        HBox newTask = new HBox(createIconButton("Add", "Add Task"),new Label("Add Task"));
        newTask.getStyleClass().add("taskLine");
        taskBox.getChildren().add(newTask);

        return status;
    }

    //Create a task line
    public HBox createTaskLine(Task task) {
        //Start task line with Project name
        HBox taskLine = new HBox(new Label(task.name));
        taskLine.getStyleClass().add("taskLine");

        //add Listener
        EventHandler a = new EventHandler() {
            @Override
            public void handle(Event event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_ENTERED)) {
                    taskLine.getChildren().add(initTaskControlButtons(taskLine));
                }
                if (event.getEventType().equals(MouseEvent.MOUSE_EXITED)) {
                    taskLine.getChildren().remove(taskLineButtons);
                }
                if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {

                }
            }
        };
        taskLine.addEventHandler(MouseEvent.MOUSE_ENTERED, a);
        taskLine.addEventHandler(MouseEvent.MOUSE_EXITED, a);
        return taskLine;
    }

    //Should initialize taskButtons based on PRIVILEGES of User
    public HBox initTaskControlButtons(HBox taskLine) {

        JFXRippler view = createIconButton("View", "View Project");
        JFXRippler startTime = createIconButton("StartTime", "Start Work");


        JFXRippler expand = createIconButton("Arrowhead-Down", "View Worklog");
        //If already collapsed rotate button to collapse position
        if (taskIsCollapsed(taskLine)) {
            expand.setRotate(180);
        }
        expand.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
            double rotate = expand.getRotate();
            if (rotate == (double) 0 && showWorklog((HBox) expand.getParent().getParent())) {
                expand.setRotate(180);
            } else if (rotate == (double) 180) {
                closeWorklog((HBox) expand.getParent().getParent());
                expand.setRotate(0);
            }
        });

        HBox taskLineButtons = new HBox();
        taskLineButtons.getChildren().addAll(view,expand,startTime);
        taskLineButtons.getStyleClass().add("lineButtons");
        HBox.setHgrow(taskLineButtons, Priority.ALWAYS);
        this.taskLineButtons = taskLineButtons;
        return taskLineButtons;
    }

    public void closeWorklog(HBox taskLine) {
        Object nxtItem;
        int index;
        VBox taskview = (VBox) taskLine.getParent();
        if ((index = taskview.getChildren().indexOf(taskLine)) < taskview.getChildren().size() + 1 && (nxtItem = taskview.getChildren().get(index + 1)) instanceof ScrollPane) {
            taskview.getChildren().remove(nxtItem);
        }
    }

    public void switchToLogin(ActionEvent event) {
        JFXDecorator decorator = persistentUser.DECORATOR;
        decorator.setContent(persistentUser.PARENT_LOGIN);
        decorator.getScene().getWindow().sizeToScene();

    }

    private boolean taskIsCollapsed(HBox taskLine) {
        int index;
        VBox taskview = (VBox) taskLine.getParent();
        taskview.getChildren();
        if ((index = taskview.getChildren().indexOf(taskLine)) + 1 < taskview.getChildren().size() && taskview.getChildren().get(index + 1) instanceof ScrollPane) {
            return true;
        }
        return false;
    }

    //TODO
    public boolean showWorklog(HBox taskLine) {
        VBox worklogBox = new VBox();

        ScrollPane worklogPane = new ScrollPane();
        worklogPane.setFitToWidth(true);

        worklogBox.setFillWidth(true);

        worklogPane.setContent(worklogBox);
        worklogPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        worklogPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        worklogPane.setPrefHeight(140);
        worklogPane.setFitToHeight(false);
        DragResizer.makeResizable(worklogPane, DragResizer.SOUTH);

        worklogPane.setStyle("-fx-border-width: 0 0 2 0; -fx-border-color: grey;");

        // Init taskID to filter worklogs
        int taskID = Integer.parseInt(taskLine.getId());

        for (int i = 0; i < Worklogs.size(); i++) {
            if (taskID == Worklogs.get(i).taskID) {
                HBox worklog = createWorkLogLine(Worklogs.get(i));
                worklogBox.getChildren().add(worklog);
                //TODO <see above TODO>
                worklog.setId(String.valueOf(i));
            }
        }
        worklogBox.setPadding(new Insets(0, 5, 5, 10));
        worklogBox.setSpacing(2);

        if (worklogBox.getChildren().size() != 0 && taskLine.getParent() instanceof VBox) {
            VBox taskbox = (VBox) taskLine.getParent();
            taskbox.getChildren().add(taskbox.getChildren().indexOf(taskLine) + 1, worklogPane);
            return true;
        }
        return false;
    }

    //Create a Worklog line
    public HBox createWorkLogLine(WorkLog workLog) {
        //Start worlog line with Project name
        HBox workLogLine = new HBox(new Label(Integer.toString(workLog.employeeID)));
        workLogLine.getStyleClass().add("worklogLine");

        //add Listener
        EventHandler a = new EventHandler() {
            @Override
            public void handle(Event event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_ENTERED)) {
                    workLogLine.getChildren().add(initTaskControlButtons(workLogLine));
                }
                if (event.getEventType().equals(MouseEvent.MOUSE_EXITED)) {
                    workLogLine.getChildren().remove(taskLineButtons);
                }
                if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {

                }
            }
        };
        workLogLine.addEventHandler(MouseEvent.MOUSE_ENTERED, a);
        workLogLine.addEventHandler(MouseEvent.MOUSE_EXITED, a);
        return workLogLine;
    }

    public HBox initWorkLogControlButtons(HBox workLog) {
        //TODO
        return null;
    }

    private JFXRippler createIconButton(String iconName, String tooltip) {

        Node glyph = null;

        //We will try and load glyph. If not available replace glyph with text
        try {
            glyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg." + iconName);
            ((SVGGlyph) glyph).setSize(26);
            ((SVGGlyph) glyph).setFill(Color.valueOf("#FFFFFF"));
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

        if (tooltip != null || tooltip != "") {
            Tooltip toolTip = new Tooltip(tooltip);
            Tooltip.install(rippler, toolTip);
        }

        return rippler;
    }

    void loadProjectDialog(Project project)
    {
        JFXDialogLayout content = new JFXDialogLayout();
        content.getStyleClass().add("dialog");
        content.lookup(".jfx-layout-actions").setStyle("-fx-alignment: CENTER; -fx-spacing: 100");
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXTextField groupID = new JFXTextField(),
                projectLeadID = new JFXTextField(),
                name = new JFXTextField(),
                description = new JFXTextField(),
                status = new JFXTextField(),
                dueDate = new JFXTextField();

        JFXRippler confirm = createIconButton("User-Check", "Confirm");
        JFXRippler cancel = createIconButton("User-Cancel", "Confrim");

        groupID.setPromptText("Group Number");
        projectLeadID.setPromptText("Project Leader");
        name.setPromptText("Name");
        description.setPromptText("Description of the project");
        status.setPromptText("Open or Closed");
        dueDate.setPromptText("Due Date must be YYYY-MM-DD HH:MM:SS");

        Text text = new Text();
        text.setFill(Paint.valueOf("#FFFFFF"));
        text.setStyle("-fx-font: bold 16px \"System\" ;");

        if(project != null) {
            text.setText("Edit Project");
            content.setHeading(text);
            groupID.setText(Integer.toString(project.groupID));
            projectLeadID.setText(Integer.toString(project.projectLeadID));
            name.setText(project.name);
            description.setText(project.description);
            status.setText(project.status);
            dueDate.setText(project.dueDate);
            confirm.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                boolean successful;
                if(projectIsValid(groupID, projectLeadID, name, description, status, dueDate)) {
                    successful = OM.editProject(
                            project.projectID,
                            name.getText(),
                            dueDate.getText(),
                            Integer.parseInt(groupID.getText()),
                            Integer.parseInt(projectLeadID.getText()),
                            description.getText(),
                            status.getText()
                    );
                    if (successful) {
                        refresh();
                        dialog.close();
                    } else {
                        projectLeadID.setPromptText("Invalid Project Leader");
                        projectLeadID.setStyle("-fx-background-color: #FFCDD2");
                        projectLeadID.clear();
                    }
                }
            });
        }
        else {
            text.setText("Add project");
            content.setHeading(text);
            confirm.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                boolean successful;
                if(projectIsValid(groupID, projectLeadID, name, description, status, dueDate)) {
                    successful = OM.addProject(
                            name.getText(),
                            dueDate.getText(),
                            Integer.parseInt(groupID.getText()),
                            Integer.parseInt(projectLeadID.getText()),
                            description.getText(),
                            status.getText()
                    );
                    if(successful) {
                        refresh();
                        dialog.close();
                    }
                    else
                    {
                        projectLeadID.setPromptText("Invalid Project Leader");
                        projectLeadID.setStyle("-fx-background-color: #FFCDD2");
                        projectLeadID.clear();
                    }
                }
            });
        }
        cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> dialog.close());
        VBox vBox = new VBox(groupID, projectLeadID, name, description, status, dueDate);
        vBox.setStyle("-fx-spacing: 15");
        content.setBody(vBox);
        content.setActions(confirm, cancel);
        dialog.show();
    }

    private boolean projectIsValid( JFXTextField name, JFXTextField dueDate, JFXTextField groupID,
                                    JFXTextField projectLeadID, JFXTextField description, JFXTextField status)
    {
        boolean valid = true;
        //name
        if(name.getText().isEmpty())
        {
            name.setPromptText("Name cannot be empty");
            name.setStyle("-fx-background-color: #FFCDD2");
            valid = false;
        }
        else if(name.getText().matches("(.*)[0-9](.*)") ||
                name.getText().matches("(.*)[!\"#$%&'()*+,./:;<=>?@^_`{|}~-](.*)"))
        {
            name.clear();
            name.setPromptText("Name can only alphabetic characters");
            name.setStyle("-fx-background-color: #FFCDD2");
            valid = false;
        }
        else name.setStyle("-fx-background-color: #FFFFFF");

        //dueDate
        try
        {
            LocalDateTime.parse(dueDate.getText());
            dueDate.setStyle("-fx-background-color: #FFFFFF");
        }
        catch (DateTimeException e)
        {
            dueDate.clear();
            status.setStyle("-fx-background-color: #FFCDD2");
            valid = false;
        }

        //groupID
        if(groupID.getText().isEmpty())
        {
            groupID.setPromptText("Group ID cannot be empty");
            groupID.setStyle("-fx-background-color: #FFCDD2");
            valid = false;
        }
        else if(!StringUtils.isStrictlyNumeric(groupID.getText()))
        {
            groupID.clear();
            groupID.setPromptText("Group ID must be an integer");
            groupID.setStyle("-fx-background-color: #FFCDD2");
            valid = false;
        }
        else groupID.setStyle("-fx-background-color: #FFFFFF");

        //projectLeadID
        if(projectLeadID.getText().isEmpty())
        {
            projectLeadID.setPromptText("Project Lead ID cannot be empty");
            projectLeadID.setStyle("-fx-background-color: #FFCDD2");
            valid = false;
        }
        else if(!StringUtils.isStrictlyNumeric(groupID.getText()))
        {
            projectLeadID.clear();
            projectLeadID.setPromptText("Project Lead ID must be an integer");
            projectLeadID.setStyle("-fx-background-color: #FFCDD2");
            valid = false;
        }
        else projectLeadID.setStyle("-fx-background-color: #FFFFFF");


        //description
        if(description.getText().matches("(.*)[\'\";](.*)"))
        {
            description.clear();
            description.setPromptText("Description cannot contain special characters");
            description.setStyle("-fx-background-color: #FFCDD2");
            valid = false;
        }
        else description.setStyle("-fx-background-color: #FFFFFF");

        //status
        if(status.getText().isEmpty() ||
            !(status.getText().equals("Open") ||
              status.getText().equals("Closed")))
        {
            status.clear();
            status.setPromptText("Status must be Open or Closed");
            status.setStyle("-fx-background-color: #FFCDD2");
            valid = false;
        }
        else status.setStyle("-fx-background-color: #FFFFFF");

        return valid;
    }

    void viewProjectDialog(Project project)
    {
        JFXDialogLayout content = new JFXDialogLayout();
        content.getStyleClass().add("dialog");
        content.lookup(".jfx-layout-actions").setStyle("-fx-alignment: CENTER; -fx-spacing: 100");
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        Text groupID = new Text(String.valueOf(project.groupID)),
             projectLeadID = new Text(String.valueOf(project.projectLeadID)),
             name = new Text(project.name),
             description = new Text(project.description),
             status = new Text(project.status),
             dueDate = new Text(project.dueDate);
        VBox vBox = new VBox(groupID, projectLeadID, name, description, status, dueDate);
        vBox.setStyle("-fx-spacing: 15");
        content.setBody(vBox);
        dialog.show();
    }

    void loadTaskDialog(Task task)
    {

    }

    void viewTaskDialog(Task task)
    {/*
        JFXDialogLayout content = new JFXDialogLayout();
        content.getStyleClass().add("dialog");
        content.lookup(".jfx-layout-actions").setStyle("-fx-alignment: CENTER; -fx-spacing: 100");
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        Text groupID = new Text(String.valueOf(project.groupID)),
                projectLeadID = new Text(String.valueOf(project.projectLeadID)),
                name = new Text(project.name),
                description = new Text(project.description),
                status = new Text(project.status),
                dueDate = new Text(project.dueDate);
        VBox vBox = new VBox(groupID, projectLeadID, name, description, status, dueDate);
        vBox.setStyle("-fx-spacing: 15");
        content.setBody(vBox);
        dialog.show();
        */
    }

    void loadWorkLogDialog(WorkLog worklog)
    {

    }

    void viewWorkLogDialog(WorkLog worklog)
    {

    }

    //Easy Debug print statement
    private void p(Object a) {
        System.out.println(a);
    }

    @FXML
    void loadEmployeeWindow(Stage primaryStage, Employee User, List<Employee> Employees){
        try {
            if(OM != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("App/gui/Employee.fxml"));
                Parent employee = loader.load();
                EmployeeController employeeController = new EmployeeController();
                primaryStage.setTitle("Employee Management");
                JFXDecorator decorator = new JFXDecorator(primaryStage, employee);
                primaryStage.setScene(new Scene(decorator));
                decorator.setContent(employee);
                employeeController.setDecorator(decorator);
                employee.getStylesheets().add("App/gui/resource/css/odin_scheme.css");
                decorator.getStylesheets().add("App/gui/resource/css/odin_scheme.css");
                employeeController.load(User, Employees, OM);
                primaryStage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void refresh()
    {
        View = new VBox();
        try{ initialize(); }
        catch(Exception e){ System.out.println("Error"); }
    }
}