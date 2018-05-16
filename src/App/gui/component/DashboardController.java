package App.gui.component;

import App.gui.component.util.ConnectionStatus;
import App.gui.persistentUser;
import Model.OdinModel;
import Server.*;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.mysql.jdbc.StringUtils;
import com.sun.corba.se.spi.orbutil.threadpool.Work;
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

import javax.swing.*;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    static Employee User;
    static String Privelage = "";
    static String MANAGER = "Manager";
    static String PROJECT_LEAD = "Project Lead";
    static String EMPLOYEE = "Employee";

    static OdinModel OM;
    ConnectionStatus StatusIcon;

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
    private HBox workLogButtons = new HBox();

    private List<Project> Projects;

    private List<Task> Tasks;

    private List<WorkLog> Worklogs;

    private List<Employee> Employees;

    private List<Message> Messages;

    private String responseString;

    public boolean load(int UserID, OdinModel OM) {
        User = OM.getEmployee_EmployeeID(UserID);
        Privelage = User.position;
                DashboardController.OM = OM;
        return true;
    }

    public void initialize() {
        Platform.runLater(() -> {
            //fetchAppropriateObjects();
            Projects = OM.getProjects();
            Tasks = OM.getTasks();
            Worklogs = OM.getWorkLogs();
            Employees = OM.getEmployees();
            Messages = OM.getMessages();
            initHeader();
            initView();
        });

        heightHeader = 0.162;
//        p(heightHeader);
//        p(splitPane.getDividers().get(0).getPosition());
        splitPane.setDividerPosition(0, heightHeader);
//        p(splitPane.getDividers().get(0).getPosition());
    }

    private void fetchAppropriateObjects()
    {
        switch(User.position)
        {
            case("Manager"):
                Employees = OM.getEmployees();
                Projects = OM.getProjects();
                Tasks = OM.getTasks();
                Worklogs = OM.getWorkLogs();
                break;
            case("Project Lead"):
                Projects = OM.getProjects_ProjectLeadID(User.employeeID);
                Tasks = new ArrayList<>();
                for(Project project : Projects) Tasks.addAll(OM.getTasks_ProjectID(project.projectID));
                Worklogs = new ArrayList<>();
                for(Task task : Tasks) Worklogs.addAll(OM.getWorkLogs_TaskID(task.taskID));
                break;
            case("Employee"):
                Projects = OM.getProjects_GroupID(User.groupID);
                Tasks = new ArrayList<>();
                for(Project project : Projects) Tasks.addAll(OM.getTasks_ProjectID(project.projectID));
                Worklogs = OM.getWorkLogs_EmployeeID(User.employeeID);
                break;
        }
    }

    public void initHeader() {
        UserName.setText("Hello, " + User.name);
        JFXRippler messenger = createIconButton("Message", "Messenger");
        messenger.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> loadMessageWindow());
        UserBar.getChildren().add(messenger);
        if (Privelage.equals(MANAGER)) {
            JFXRippler manageEmployeeButton = createIconButton("Group", "Manage Employees");
            manageEmployeeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> loadEmployeeWindow());
            UserBar.getChildren().add(manageEmployeeButton);
            p(1);
        } else if (Privelage.equals(PROJECT_LEAD)) {
            p(2);
        } else if (Privelage.equals(EMPLOYEE)) {
            p(3);
        }

        JFXRippler logOut = createIconButton("Exit", "Logout");
        JFXRippler refresh = createIconButton("Refresh", "Refresh");

        refresh.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> refresh());
        logOut.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> switchToLogin(new ActionEvent()));

        HBox rightAligned = new HBox(refresh, logOut);



        StatusIcon = new ConnectionStatus(OM);
        rightAligned.getChildren().add(1,StatusIcon.getIcon());
        HBox.setHgrow(rightAligned, Priority.ALWAYS);
        rightAligned.getStyleClass().add("lineButtons");
        UserBar.getChildren().add(rightAligned);


    }

    //Should initialize ProjectButtons based on PRIVILEGES of User
    public HBox initProjectControlButtons(HBox projectLine, Project project) {
        HBox projectLineButtons = new HBox();
//        projectLineButtons.getChildren().add(createIconButton("View", "View Project"));
//        projectLineButtons.getChildren().add(createIconButton("Group-Info", "Assigned Employees"));


        //EDIT Button
        if(Privelage.equals(MANAGER)){
            JFXRippler Edit = createIconButton("Edit", "Edit Project");
            Edit.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> loadProjectDialog(project));
            projectLineButtons.getChildren().add(Edit);
        }

        JFXRippler View = createIconButton("View", "View Project");
        View.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> viewProjectDialog(project));
        projectLineButtons.getChildren().add(View);

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
        Project project;
        HBox projectline;
        for (int i = 0; i < Projects.size(); i++) {
            project = Projects.get(i);
            if(User.position.equals("Manager")) {
                projectline = createProjectLine(project);
                //Set id of Hbox as related to the array list
                projectline.setId(Integer.toString(project.projectID));
                View.getChildren().add(projectline);
            }
            else if(project.groupID == User.groupID)
            {
                projectline = createProjectLine(project);
                projectline.setId(Integer.toString(project.projectID));
                View.getChildren().add(projectline);
            }
        }
        if(User.position.equals("Manager")) {
            JFXRippler addProject = createIconButton("Add", "Add Project");
            addProject.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> loadProjectDialog(null));
            HBox newProject = new HBox(addProject, new Label("Add Project"));
            newProject.getStyleClass().add("projectLine");
            newProject.setStyle("-fx-background-color: #1a555b");
            View.getChildren().addAll(newProject);
        }
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
        return (index = View.getChildren().indexOf(projectLine)) + 1 < View.getChildren().size() && View.getChildren().get(index + 1) instanceof VBox;
    }

    //Creates a project line
    public HBox createProjectLine(Project project) {
        //Start project line with Project name
        HBox projectLine;
        String title = "(PID: " + project.projectID + ") " + project.name;
        if(project.status.equals("Closed")) title = title + " CLOSED";
        projectLine = new HBox(new Label(title));
        projectLine.getStyleClass().add("projectLine");
        //Event handler for hover
        EventHandler a = new EventHandler() {
            @Override
            public void handle(Event event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_ENTERED)) {
                    projectLine.getChildren().add(initProjectControlButtons(projectLine, project));
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

        //Adds Create button
        if((User.position.equals("Manager") || User.position.equals("Project Lead")) &&
                OM.filterProjects_ProjectID(Projects, projID).status.equals("Open"))
        {
            HBox newTask = new HBox(createIconButton("Add", "Add Task"), new Label("Add Task"));
            newTask.getStyleClass().add("taskLine");
            newTask.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> loadTaskDialog(null, Integer.parseInt(projectLine.getId())));
            taskBox.getChildren().add(newTask);
        }
        return status;
    }

    //Create a task line
    public HBox createTaskLine(Task task) {
        //Start task line with Project name
        HBox taskLine = new HBox(new Label(task.name));
        taskLine.getStyleClass().add("taskLine");
        taskLine.setId(String.valueOf(task.taskID));

        //add Listener
        EventHandler a = new EventHandler() {
            @Override
            public void handle(Event event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_ENTERED)) {
                    taskLine.getChildren().add(initTaskControlButtons(taskLine, task));
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
    public HBox initTaskControlButtons(HBox taskLine, Task task) {
        Project relaventProject = OM.filterProjects_ProjectID(Projects, task.projectID);
        boolean taskStarted;
        int lastLogID = getLastLog(task.taskID, User.employeeID);

        taskStarted = (lastLogID != -1);

        JFXRippler workButton;
        JFXRippler view = createIconButton("View", "View Task");
        JFXRippler edit = createIconButton("Edit", "Edit Task");
        JFXRippler expand = createIconButton("Arrowhead-Down", "View Work Logs");
        if(!taskStarted)
            workButton = createIconButton("StartTime", "Start Work");
        else
            workButton = createIconButton("StopTime", "Stop Work");

        view.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> viewTaskDialog(task));
        edit.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> loadTaskDialog(task, -1));

        workButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(!taskStarted)
            {
                OM.startWork(task.taskID, User.employeeID);
                refresh();
            }
            else getStopDesc(lastLogID);
        });

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
        if(User.position.equals("Manager") || relaventProject.projectLeadID == User.employeeID)
            taskLineButtons.getChildren().addAll(view, edit, workButton, expand);
        else
            taskLineButtons.getChildren().addAll(view, workButton, expand);
        taskLineButtons.getStyleClass().add("lineButtons");
        if(task.status.equals("Closed"))
            taskLineButtons.getChildren().remove(workButton);
        HBox.setHgrow(taskLineButtons, Priority.ALWAYS);
        this.taskLineButtons = taskLineButtons;
        return taskLineButtons;
    }

    private boolean taskHasLogs(HBox taskLine) {
        int taskID = Integer.parseInt(taskLine.getId());
        for (int i = 0; i < Worklogs.size();i++) {
            if(taskID == Worklogs.get(i).taskID){
                return true;
            }
        }
        return false;
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
        StatusIcon.close();
        JFXDecorator decorator = persistentUser.DECORATOR;
        decorator.setContent(persistentUser.PARENT_LOGIN);
        decorator.getScene().getWindow().sizeToScene();

    }

    private boolean taskIsCollapsed(HBox taskLine) {
        int index;
        VBox taskview = (VBox) taskLine.getParent();
        taskview.getChildren();
        return (index = taskview.getChildren().indexOf(taskLine)) + 1 < taskview.getChildren().size() && taskview.getChildren().get(index + 1) instanceof ScrollPane;
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

        WorkLog currentLog;
        for (int i = 0; i < Worklogs.size(); i++) {
            currentLog = Worklogs.get(i);
            if (currentLog.taskID == taskID &&
               (!User.position.equals("Employee") || currentLog.employeeID == User.employeeID)) {
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
        String label;
        HBox workLogLine;

        if(workLog.stopTime == null)
            label = "EMP: " + Integer.toString(workLog.employeeID) + " START: " + workLog.startTime + ". IP.";
        else
            label = "EMP: " + Integer.toString(workLog.employeeID) + " START: " + workLog.startTime + ". STOP: " + workLog.stopTime + ".";

        workLogLine = new HBox(new Label(label));
        workLogLine.getStyleClass().add("worklogLine");

        //add Listener
        EventHandler a = new EventHandler() {
            @Override
            public void handle(Event event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_ENTERED)) {
                    workLogLine.getChildren().add(initWorkLogControlButtons(workLog));
                }
                if (event.getEventType().equals(MouseEvent.MOUSE_EXITED)) {
                    workLogLine.getChildren().remove(workLogButtons);
                }
                if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {

                }
            }
        };
        workLogLine.addEventHandler(MouseEvent.MOUSE_ENTERED, a);
        workLogLine.addEventHandler(MouseEvent.MOUSE_EXITED, a);
        return workLogLine;
    }

    public HBox initWorkLogControlButtons(WorkLog workLog) {
        Project relevantProject = OM.filterProjects_ProjectID(Projects, OM.filterTasks_TaskID(Tasks, workLog.taskID).projectID);
        HBox workLogLineButtons = new HBox();
        JFXRippler viewWorkLogButton = createIconButton("View", "View WorkLog");
        JFXRippler editWorkLogButton = createIconButton("Edit", "Edit WorkLog");
        viewWorkLogButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> viewWorkLogDialog(workLog));
        editWorkLogButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> loadWorkLogDialog(workLog));
        workLogLineButtons.getChildren().add(viewWorkLogButton);
        if(User.position.equals("Manager") || (User.position.equals("Project Lead") && User.employeeID == relevantProject.projectLeadID))
            workLogLineButtons.getChildren().add(editWorkLogButton);
        this.workLogButtons = workLogLineButtons;
        return workLogLineButtons;
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

    private boolean projectIsValid( JFXTextField name, JFXTextField dueDate, JFXTextField groupID,
                                    JFXTextField projectLeadID, JFXTextField description, JFXTextField status)
    {
        boolean valid = true;
        System.out.println(dueDate.getText());

        //name
        if(name.getText().isEmpty())
        {
            dialogError_JFXTF(name, "Name cannot be empty");
            valid = false;
        }
        else if(!OM.isValidName(name.getText()))
        {
            dialogError_JFXTF(name, "Name can only be alphabetic characters");
            valid = false;
        }
        else name.setStyle("-fx-background-color: #FFFFFF");

        //dueDate
        if(!OM.isValidDate(dueDate.getText()))
        {
            dialogError_JFXTF(dueDate, "Due Date must be in YYYY-MM-DD format");
            valid = false;
        }
        else dueDate.setStyle("-fx-background-color: #FFFFFF");

        //groupID
        if(groupID.getText().isEmpty())
        {
            dialogError_JFXTF(groupID,"Group ID cannot be empty");
            valid = false;
        }
        else if(!OM.isValidNum(groupID.getText()))
        {
            dialogError_JFXTF(groupID, "Group ID must be an integer");
            valid = false;
        }
        else groupID.setStyle("-fx-background-color: #FFFFFF");

        //projectLeadID
        if(projectLeadID.getText().isEmpty())
        {
            dialogError_JFXTF(projectLeadID, "Project Lead ID cannot be empty");
            valid = false;
        }
        else if(!OM.isValidNum(projectLeadID.getText()))
        {
            projectLeadID.clear();
            dialogError_JFXTF(projectLeadID,"Project Lead ID must be an integer");
            valid = false;
        }
        else projectLeadID.setStyle("-fx-background-color: #FFFFFF");


        //description
        if(!OM.isValidString(description.getText()))
        {
            dialogError_JFXTF(description, "Description cannot contain special characters");
            valid = false;
        }
        else description.setStyle("-fx-background-color: #FFFFFF");

        //status
        if(status.getText().isEmpty() || !OM.isValidWorkStatus(status.getText()))
        {
            dialogError_JFXTF(status, "Status must be Open or Closed");
            valid = false;
        }
        else status.setStyle("-fx-background-color: #FFFFFF");

        return valid;
    }

    void loadProjectDialog(Project project)
    {
        JFXDialogLayout content = new JFXDialogLayout();
        content.getStyleClass().add("dialog");
        content.lookup(".jfx-layout-actions").setStyle("-fx-alignment: CENTER; -fx-spacing: 100");
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXTextField    name = new JFXTextField(),
                        dueDate = new JFXTextField(),
                        groupID = new JFXTextField(),
                        projectLeadID = new JFXTextField(),
                        description = new JFXTextField(),
                        status = new JFXTextField();

        JFXRippler confirm = createIconButton("Check", "Confirm");
        JFXRippler cancel = createIconButton("Cancel", "Cancel");
        JFXRippler finish = createIconButton("Finish", "Close Project");

        name.setLabelFloat(true);
        dueDate.setLabelFloat(true);
        groupID.setLabelFloat(true);
        projectLeadID.setLabelFloat(true);
        description.setLabelFloat(true);
        status.setLabelFloat(true);

        name.setPromptText("Name");
        dueDate.setPromptText("Due Date must be YYYY-MM-DD");
        groupID.setPromptText("Group Number");
        projectLeadID.setPromptText("Project Leader ID");
        description.setPromptText("Description of the project");
        status.setPromptText("Open or Closed");

        Text text = new Text();
        text.setFill(Paint.valueOf("#FFFFFF"));
        text.setStyle("-fx-font: bold 16px \"System\" ;");

        if(project != null) {
            text.setText("Edit Project");
            content.setHeading(text);
            name.setText(project.name);
            dueDate.setText(project.dueDate);
            groupID.setText(Integer.toString(project.groupID));
            projectLeadID.setText(Integer.toString(project.projectLeadID));
            description.setText(project.description);
            status.setText(project.status);
            status.setDisable(true);
            finish.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> loadFinishProjectDialog(project, dialog));
            confirm.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                boolean successful;
                if(projectIsValid(name, dueDate, groupID, projectLeadID, description, status)) {
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
                    }
                    else { dialogError_JFXTF(projectLeadID, "Invalid Project Lead"); }
                }
            });
        }
        else {
            text.setText("Add project");
            content.setHeading(text);
            confirm.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                boolean successful;
                if(projectIsValid(name, dueDate, groupID, projectLeadID, description, status)) {
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
                    else { dialogError_JFXTF(projectLeadID, "Invalid Project Lead"); }
                }
            });
        }
        cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> dialog.close());
        VBox vBox = new VBox(name, dueDate, groupID, projectLeadID, description, status);
        vBox.setStyle("-fx-spacing: 15");
        content.setBody(vBox);
        if(project != null) content.setActions(finish, confirm, cancel);
        dialog.show();
    }

    void loadFinishProjectDialog(Project project, JFXDialog parent)
    {
        JFXDialogLayout content = new JFXDialogLayout();
        content.getStyleClass().add("dialog");
        content.lookup(".jfx-layout-actions").setStyle("-fx-alignment: CENTER; -fx-spacing: 100");
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXRippler confirm = createIconButton("Check", "Confirm");
        JFXRippler cancel = createIconButton("Cancel", "Confrim");
        Label prompt;
        if(project.status.equals("Open")) prompt = new Label("Are you sure you want to close " + project.name + "?");
        else prompt = new Label("Are you sure you want to open " + project.name + "?");
        confirm.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
        {
            if(project.status.equals("Open"))
            {
                OM.setProject_Closed(project.projectID);
                for(Task task : Tasks) if(task.projectID == project.projectID) OM.setTask_Closed(task.taskID);
            }
            else
            {
                OM.setProject_Open(project.projectID);
                for(Task task : Tasks) if(task.projectID == project.projectID) OM.setTask_Open(task.taskID);
            }
            refresh();
            dialog.close();
            parent.close();
        });
        cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> dialog.close());
        VBox vBox = new VBox(prompt);
        vBox.setStyle("-fx-spacing: 15");
        content.setBody(vBox);
        content.setActions(confirm, cancel);
        dialog.show();
    }

    void viewProjectDialog(Project project)
    {
        JFXDialogLayout content = new JFXDialogLayout();
        content.getStyleClass().add("dialog");
        content.lookup(".jfx-layout-actions").setStyle("-fx-alignment: CENTER; -fx-spacing: 100");
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        String timeString = OM.durationAsString(OM.calcDuration(OM.getWorkLogs(project, Tasks, Worklogs)));
        JFXRippler addTask = new JFXRippler(new Label("Add Task"));
        Label name = new Label("Name: " + project.name),
             dueDate = new Label("Due Date: " + project.dueDate),
             groupID = new Label("Group ID: " + String.valueOf(project.groupID)),
             projectLeadID = new Label("Project Lead: " + String.valueOf(project.projectLeadID)),
             description = new Label("Description: " + project.description),
             status = new Label("Status: " + project.status),
             timeSpent = new Label("Time spent on " + project.name + ": " + timeString);
        name.setWrapText(true);
        dueDate.setWrapText(true);
        groupID.setWrapText(true);
        projectLeadID.setWrapText(true);
        description.setWrapText(true);
        status.setWrapText(true);
        timeSpent.setWrapText(true);
        addTask.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            dialog.close();
            loadTaskDialog(null, project.projectID);
        });
        VBox vBox = new VBox(groupID, projectLeadID, name, description, status, dueDate, timeSpent, addTask);
        vBox.setStyle("-fx-spacing: 15");
        content.setBody(vBox);
        dialog.show();
    }

    private boolean taskIsValid( JFXTextField name, JFXTextField dueDate, JFXTextField projectID,
                                 JFXTextField employees, JFXTextField description, JFXTextField size,
                                 JFXTextField status)
    {
        boolean valid = true;

        //name
        if(name.getText().isEmpty())
        {
            dialogError_JFXTF(name, "Name cannot be empty");
            valid = false;
        }
        else if(!OM.isValidName(name.getText()))
        {
            dialogError_JFXTF(name, "Name can only be alphabetic characters");
            valid = false;
        }
        else name.setStyle("-fx-background-color: #FFFFFF");

        //dueDate
        if(!OM.isValidDate(dueDate.getText()))
        {
            dialogError_JFXTF(dueDate, "Due Date must be in YYYY-MM-DD format");
            valid = false;
        }
        else dueDate.setStyle("-fx-background-color: #FFFFFF");

        //projectID
        if(projectID.getText().isEmpty())
        {
            dialogError_JFXTF(projectID,"Project ID cannot be empty");
            valid = false;
        }
        else if(!OM.isValidNum(projectID.getText()))
        {
            dialogError_JFXTF(projectID, "Project ID must be an integer");
            valid = false;
        }
        else projectID.setStyle("-fx-background-color: #FFFFFF");

        //employees
        if(employees.getText().isEmpty())
        {
            dialogError_JFXTF(employees, "Employees cannot be empty");
        }
        else if(!OM.isValidEmpList(employees.getText()))
        {
            dialogError_JFXTF(employees, "Employees must be comma separated list of digits");
            valid = false;
        }
        else employees.setStyle("-fx-background-color: #FFFFFF");

        //description
        if(!OM.isValidString(description.getText()))
        {
            dialogError_JFXTF(description, "Description cannot contain special characters");
            valid = false;
        }
        else description.setStyle("-fx-background-color: #FFFFFF");

        //size
        if(size.getText().isEmpty())
        {
            dialogError_JFXTF(size, "Size cannot be empty");
            valid = false;
        }
        else if(!OM.isValidNum(size.getText()))
        {
            size.clear();
            dialogError_JFXTF(size,"Size must be an integer");
            valid = false;
        }
        else size.setStyle("-fx-background-color: #FFFFFF");

        //status
        if(status.getText().isEmpty() || !OM.isValidWorkStatus(status.getText()))
        {
            dialogError_JFXTF(status, "Status must be Open or Closed");
            valid = false;
        }
        else status.setStyle("-fx-background-color: #FFFFFF");

        return valid;
    }

    void loadTaskDialog(Task task, int projSeed)
    {
        JFXDialogLayout content = new JFXDialogLayout();
        content.getStyleClass().add("dialog");
        content.lookup(".jfx-layout-actions").setStyle("-fx-alignment: CENTER; -fx-spacing: 100");
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXTextField    name = new JFXTextField(),
                        dueDate = new JFXTextField(),
                        projectID = new JFXTextField(),
                        employees = new JFXTextField(),
                        description = new JFXTextField(),
                        size = new JFXTextField(),
                        status = new JFXTextField();

        JFXRippler confirm = createIconButton("Check", "Confirm");
        JFXRippler cancel = createIconButton("Cancel", "Confrim");
        JFXRippler finish = createIconButton("Finish", "Close Task");

        name.setLabelFloat(true);
        dueDate.setLabelFloat(true);
        projectID.setLabelFloat(true);
        employees.setLabelFloat(true);
        description.setLabelFloat(true);
        size.setLabelFloat(true);
        status.setLabelFloat(true);

        name.setPromptText("Name");
        dueDate.setPromptText("Due Date must be YYYY-MM-DD");
        projectID.setPromptText("Project ID");
        employees.setPromptText("Employees on the task");
        description.setPromptText("Description of the task");
        size.setPromptText("Size of task (1-10)");
        status.setPromptText("Open or Closed");

        Text text = new Text();
        text.setFill(Paint.valueOf("#FFFFFF"));
        text.setStyle("-fx-font: bold 16px \"System\" ;");

        if(task != null) {
            text.setText("Edit Task");
            content.setHeading(text);
            name.setText(task.name);
            dueDate.setText(task.dueDate);
            projectID.setText(Integer.toString(task.projectID));
            employees.setText(OM.empListToString(task.employees));
            description.setText(task.description);
            size.setText(Integer.toString(task.size));
            status.setText(task.status);
            status.setDisable(true);
            confirm.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                boolean successful;
                if(taskIsValid(name, dueDate, projectID, employees, description, size, status)) {
                    successful = OM.editTask(
                            task.taskID,
                            name.getText(),
                            dueDate.getText(),
                            Integer.parseInt(projectID.getText()),
                            employees.getText(),
                            description.getText(),
                            Integer.parseInt(size.getText()),
                            status.getText()
                    );
                    if (successful) {
                        refresh();
                        dialog.close();
                    }
                    else { dialogError_JFXTF(projectID, "Invalid project or employees not in project"); }
                }
            });
            finish.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> loadFinishTaskDialog(task, dialog));
        }
        else {
            text.setText("Add Task");
            content.setHeading(text);
            if(projSeed != -1)
            {
                projectID.setText(String.valueOf(projSeed));
                projectID.setDisable(true);
            }
            confirm.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                boolean successful;
                if(taskIsValid(name, dueDate, projectID, employees, description, size, status)) {
                    successful = OM.addTask(
                            name.getText(),
                            dueDate.getText(),
                            Integer.parseInt(projectID.getText()),
                            employees.getText(),
                            description.getText(),
                            Integer.parseInt(size.getText()),
                            status.getText()
                    );
                    if(successful) {
                        refresh();
                        dialog.close();
                    }
                    else { dialogError_JFXTF(employees, "Invalid project or employees not in project"); }
                }
            });
        }
        cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> dialog.close());
        VBox vBox = new VBox(name, dueDate, projectID, employees, description, size, status);
        vBox.setStyle("-fx-spacing: 15");
        content.setBody(vBox);
        content.setActions(finish, confirm, cancel);
        dialog.show();
    }

    void loadFinishTaskDialog(Task task, JFXDialog parent)
    {
        JFXDialogLayout content = new JFXDialogLayout();
        content.getStyleClass().add("dialog");
        content.lookup(".jfx-layout-actions").setStyle("-fx-alignment: CENTER; -fx-spacing: 100");
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXRippler confirm = createIconButton("Check", "Confirm");
        JFXRippler cancel = createIconButton("Cancel", "Confrim");
        Label prompt;
        if(task.status.equals("Open")) prompt = new Label("Are you sure you want to close " + task.name + "?");
        else prompt = new Label("Are you sure you want to open " + task.name + "?");
        confirm.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
        {
            if(task.status.equals("Open")) OM.setTask_Closed(task.taskID);
            else OM.setTask_Open(task.taskID);
            refresh();
            dialog.close();
            parent.close();
        });
        cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> dialog.close());
        VBox vBox = new VBox(prompt);
        vBox.setStyle("-fx-spacing: 15");
        content.setBody(vBox);
        content.setActions(confirm, cancel);
        dialog.show();
    }

    void viewTaskDialog(Task task)
    {
        JFXDialogLayout content = new JFXDialogLayout();
        content.getStyleClass().add("dialog");
        content.lookup(".jfx-layout-actions").setStyle("-fx-alignment: CENTER; -fx-spacing: 100");
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        Label    name = new Label("Name: " + task.name),
                dueDate = new Label("Due Date: " + task.dueDate),
                projectID = new Label("Project ID: " + String.valueOf(task.projectID)),
                employees = new Label("Employees: " + OM.empListToString(task.employees)),
                description = new Label("Description: " + task.description),
                size = new Label("Size: " + String.valueOf(task.size)),
                status = new Label("Status: " + task.status);
        VBox vBox = new VBox(name, dueDate, projectID, employees, description, size, status);
        vBox.setStyle("-fx-spacing: 15");
        content.setBody(vBox);
        dialog.show();
    }

    private void getStopDesc(int taskID)
    {
        responseString = "";
        JFXDialogLayout content = new JFXDialogLayout();
        content.getStyleClass().add("dialog");
        content.lookup(".jfx-layout-actions").setStyle("-fx-alignment: CENTER; -fx-spacing: 100");
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXTextField description = new JFXTextField();
        JFXRippler confirm = createIconButton("Check", "Confirm");
        JFXRippler cancel = createIconButton("Cancel", "Cancel");

        description.setPromptText("Description of work");

        confirm.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                OM.stopWork(taskID, description.getText());
            refresh();
            dialog.close();
        });
        cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            dialog.close();
        });
        VBox vBox = new VBox(description, confirm, cancel);
        vBox.setStyle("-fx-spacing: 15");
        content.setBody(vBox);
        content.setActions(confirm, cancel);
        dialog.show();
    }

    boolean workLogIsValid(JFXTextField startTime, JFXTextField stopTime, JFXTextField description,
                           JFXTextField taskID, JFXTextField employeeID)
    {
        boolean valid = true;

        //stop time
        if(startTime.getText().isEmpty() || !OM.isValidDateTime(startTime.getText()))
        {
            dialogError_JFXTF(startTime, "Start Time must be YYYY-MM-DD HH:mm:ss");
            valid = false;
        }
        else startTime.setStyle("-fx-background-color: #FFFFFF");

        //stop time
        if(stopTime.getText().isEmpty() || !OM.isValidDateTime(stopTime.getText()))
        {
            dialogError_JFXTF(stopTime, "Stop Time must be YYYY-MM-DD HH:mm:ss");
            valid = false;
        }
        else startTime.setStyle("-fx-background-color: #FFFFFF");

        //description
        if(!OM.isValidString(description.getText()))
        {
            dialogError_JFXTF(description, "Description cannot contain special characters");
            valid = false;
        }
        else description.setStyle("-fx-background-color: #FFFFFF");

        //taskID
        if(taskID.getText().isEmpty())
        {
            dialogError_JFXTF(taskID,"Task ID cannot be empty");
            valid = false;
        }
        else if(!OM.isValidNum(taskID.getText()))
        {
            dialogError_JFXTF(taskID, "Task ID must be an integer");
            valid = false;
        }
        else taskID.setStyle("-fx-background-color: #FFFFFF");

        //employeeID
        if(employeeID.getText().isEmpty())
        {
            dialogError_JFXTF(taskID,"Employee ID cannot be empty");
            valid = false;
        }
        else if(!OM.isValidNum(employeeID.getText()))
        {
            dialogError_JFXTF(employeeID, "Employee ID must be an integer");
            valid = false;
        }
        else employeeID.setStyle("-fx-background-color: #FFFFFF");
        return valid;
    }

    void loadWorkLogDialog(WorkLog worklog)
    {
        JFXDialogLayout content = new JFXDialogLayout();
        content.getStyleClass().add("dialog");
        content.lookup(".jfx-layout-actions").setStyle("-fx-alignment: CENTER; -fx-spacing: 100");
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXTextField    startTime = new JFXTextField(),
                        stopTime = new JFXTextField(),
                        elapsedTime = new JFXTextField(),
                        description = new JFXTextField(),
                        taskID = new JFXTextField(),
                        employeeID = new JFXTextField();

        JFXRippler confirm = createIconButton("Check", "Confirm");
        JFXRippler cancel = createIconButton("Cancel", "Confrim");

        startTime.setPromptText("Stop time must be YYYY-MM-DD HH:mm:ss");
        stopTime.setPromptText("Stop time must be YYYY-MM-DD HH:mm:ss");
        elapsedTime.setPromptText("Elapsed Time");
        description.setPromptText("Description of the work done");
        taskID.setPromptText("Task ID");
        employeeID.setPromptText("Employee ID");

        elapsedTime.setDisable(true);

        Text text = new Text();
        text.setFill(Paint.valueOf("#FFFFFF"));
        text.setStyle("-fx-font: bold 16px \"System\" ;");

        if(worklog != null) {
            text.setText("Edit work log");
            content.setHeading(text);
            if(worklog.startTime != null)
                startTime.setText(worklog.startTime.substring(0,19));
            if(worklog.stopTime != null)
                stopTime.setText(worklog.stopTime.substring(0,19));
            elapsedTime.setText(worklog.elapsedTime);
            description.setText(worklog.description);
            taskID.setText(Integer.toString(worklog.logID));
            employeeID.setText(Integer.toString(worklog.employeeID));
            confirm.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                boolean successful;
                if(workLogIsValid(startTime, stopTime, description, taskID, employeeID)) {
                    successful = OM.editWorkLog(
                            worklog.logID,
                            startTime.getText(),
                            stopTime.getText(),
                            OM.calcElapsedTime(startTime.getText(), stopTime.getText()),
                            description.getText(),
                            Integer.parseInt(taskID.getText()),
                            Integer.parseInt(employeeID.getText())
                    );
                    if (successful) {
                        refresh();
                        dialog.close();
                    }
                    else { dialogError_JFXTF(taskID, "Invalid task or employees not in task"); }
                }
            });
        }
        else {
            text.setText("Add work log");
            content.setHeading(text);
            confirm.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                boolean successful;
                elapsedTime.setText(OM.calcElapsedTime(startTime.getText(), stopTime.getText()));
                if(workLogIsValid(startTime, stopTime, description, taskID, employeeID)) {
                    successful = OM.addWorkLog(
                            startTime.getText(),
                            stopTime.getText(),
                            elapsedTime.getText(),
                            description.getText(),
                            Integer.parseInt(taskID.getText()),
                            Integer.parseInt(employeeID.getText())
                    );
                    if (successful) {
                        refresh();
                        dialog.close();
                    }
                    else { dialogError_JFXTF(taskID, "Invalid task or employees not in task"); }
                }
            });
        }
        cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> dialog.close());
        VBox vBox = new VBox(startTime, stopTime, elapsedTime, description, taskID, employeeID);
        vBox.setStyle("-fx-spacing: 15");
        content.setBody(vBox);
        content.setActions(confirm, cancel);
        dialog.show();
    }

    void viewWorkLogDialog(WorkLog worklog)
    {
        JFXDialogLayout content = new JFXDialogLayout();
        content.getStyleClass().add("dialog");
        content.lookup(".jfx-layout-actions").setStyle("-fx-alignment: CENTER; -fx-spacing: 100");
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        String  stop = (worklog.stopTime == null) ? "In Progress" : worklog.stopTime,
                desc = (worklog.description == null) ? "In Progress" : worklog.description,
                totalTime = (worklog.elapsedTime == null) ? "In Progress" : worklog.elapsedTime;
        Label   startTime = new Label("Started: " + worklog.startTime),
                stopTime = new Label("Stopped: " + stop),
                elapsedTime = new Label("Total time: " + totalTime),
                description = new Label("Description: " + desc),
                taskID = new Label("Task: " + String.valueOf(worklog.taskID)),
                employeeID = new Label("Employee: " + String.valueOf(worklog.employeeID));
        VBox vBox = new VBox(startTime, stopTime, elapsedTime, description, taskID, employeeID);
        vBox.setStyle("-fx-spacing: 15");
        content.setBody(vBox);
        dialog.show();
    }

    int getLastLog(int taskID, int empID)
    {
        List<WorkLog> relevantLogs;
        relevantLogs = OM.filterWorkLog_EmployeeID(OM.filterWorkLog_TaskID(Worklogs, taskID), empID);
        if(relevantLogs.isEmpty()) return -1;
        for(WorkLog workLog : relevantLogs) if(workLog.stopTime == null) return workLog.logID;
        return -1;
    }

    void dialogError_JFXTF(JFXTextField input, String message)
    {
        input.clear();
        input.setPromptText(message);
        input.setStyle("-fx-background-color: #FFCDD2");
    }

    //Easy Debug print statement
    private void p(Object a) {
        System.out.println(a);
    }

    @FXML
    void loadEmployeeWindow() {
        try {
            if (OM != null) {
                Stage primaryStage = new Stage();
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

    private void loadMessageWindow() {
        try {
            if (OM != null) {
                Stage primaryStage = new Stage();
                List<Message> correctMessages = OM.filterMessages_RecipientID(Messages, User.employeeID);
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("App/gui/Messenger.fxml"));
                Parent messenger = loader.load();
                MessageController messageController = new MessageController();
                primaryStage.setTitle("Messenger");
                JFXDecorator decorator = new JFXDecorator(primaryStage, messenger);
                primaryStage.setScene(new Scene(decorator));
                decorator.setContent(messenger);
                messageController.setDecorator(decorator);
                messenger.getStylesheets().add("App/gui/resource/css/odin_scheme.css");
                decorator.getStylesheets().add("App/gui/resource/css/odin_scheme.css");
                messageController.load(User, Employees, correctMessages, OM);
                primaryStage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void refresh()
    {
        View.getChildren().remove(0,View.getChildren().size());
        try{
            Projects = OM.getProjects();
            Tasks = OM.getTasks();
            Worklogs = OM.getWorkLogs();
            Employees = OM.getEmployees();
            initView();
        }
        catch(Exception e){ System.out.println("Error"); }
    }
}