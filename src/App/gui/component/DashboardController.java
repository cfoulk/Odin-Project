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
import java.util.List;

public class DashboardController {

    static Employee User;
    static String Privelage = "";
    static String MANAGER = "Manager";
    static String PROJECT_LEAD = "Project Lead";
    static String EMPLOYEE = "Employee";

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
        Privelage = User.position;
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

    public void initHeader() {
        UserName.setText("Hello, " + User.name);
        UserBar.getChildren().add(createIconButton("Message", "Messenger"));
        if (Privelage.equals(MANAGER)) {
            JFXRippler manageEmployeeButton = createIconButton("Group", "Manage Employees");
            manageEmployeeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> loadEmployeeWindow(new Stage(), User, Employees));
            UserBar.getChildren().add(manageEmployeeButton);
            p(1);
        } else if (Privelage.equals(PROJECT_LEAD)) {
            p(2);
        } else if (Privelage.equals(EMPLOYEE)) {
            p(3);
        }

        JFXRippler logOut = createIconButton("Exit", "Logout");
        JFXRippler refresh = createIconButton("Refresh", "Refresh");

        logOut.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> switchToLogin(new ActionEvent()));

        HBox rightAligned = new HBox(refresh, logOut);
        if (connectionStatus != null) {
            rightAligned.getChildren().add(1, connectionStatus);
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


        //EDIT Button
        if(Privelage.equals(MANAGER)){
            JFXRippler Edit = createIconButton("Edit", "Edit Project");
            projectLineButtons.getChildren().add(Edit);
        }

        //EXPAND/COLLAPSE Button
        if(projectHasTasks(projectLine)) {
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
        }

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
        HBox newProject = new HBox(createIconButton("Add", "Add Project"), new Label("Add Project"));
        newProject.getStyleClass().add("projectLine");
        newProject.setStyle("-fx-background-color: #1a555b");
        View.getChildren().addAll(newProject);
    }

    private boolean projectHasTasks(HBox projectLine) {
        int projID = Integer.parseInt(projectLine.getId());
        for (int i = 0; i < Tasks.size();i++) {
            if(projID == Tasks.get(i).projectID){
                return true;
            }
        }
        return false;
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
        //Event handler for hover
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
        HBox newTask = new HBox(createIconButton("Add", "Add Task"), new Label("Add Task"));
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
        taskLineButtons.getChildren().addAll(view, expand, startTime);
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

    //Easy Debug print statement
    private void p(Object a) {
        System.out.println(a);
    }

    @FXML
    void loadEmployeeWindow(Stage primaryStage, Employee User, List<Employee> Employees) {
        try {
            if (OM != null) {
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
}