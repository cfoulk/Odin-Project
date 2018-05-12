package App.gui.component;

import java.util.List;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.event.Event;
import App.gui.persistentUser;
import Model.OdinModel;
import Server.Employee;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EmployeeController {

    static Employee User;

    static List<Employee> Employees;

    static OdinModel OM;

    @FXML
    private JFXDecorator decorator;

    @FXML
    private StackPane stackPane;

    @FXML
    private SplitPane splitPane;

    @FXML
    private VBox View;

    @FXML
    private Text Username;

    @FXML
    private HBox Header;

    private HBox empLineButtons = new HBox();

    public boolean load(Employee User, List<Employee> Employees, OdinModel OM) {
        this.User = User;
        this.Employees = Employees;
        this.OM = OM;
        return true;
    }

    @FXML
    void initialize() throws Exception{
        Platform.runLater(() -> {
            initHeader();
            initView();
        });
    }

    private void initHeader()
    {
        JFXRippler addEmpButton = new JFXRippler(createIconButton("User-Add", "Add Employee"));
        addEmpButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> loadEmployeeDialog(null));
        Username.setText(User.name);
        Header.getChildren().add(addEmpButton);
    }

    private void initView() {
        for(int i = 0; i < Employees.size(); ++i)
        {
            HBox empLine = createEmpLine(Employees.get(i));
            empLine.setId(Integer.toString(i));
            View.getChildren().add(empLine);
        }
    }

    public HBox createEmpLine(Employee employee) {
        HBox empLine = new HBox(new Label(employee.name));
        empLine.getStyleClass().add("empLine");
        EventHandler a = new EventHandler() {
            @Override
            public void handle(Event event) {
                if(event.getEventType().equals(MouseEvent.MOUSE_ENTERED)){
                    empLine.getChildren().add(initEmpControlButtons(employee));
                }
                if(event.getEventType().equals(MouseEvent.MOUSE_EXITED)){
                    empLine.getChildren().remove(empLineButtons);
                }
            }
        };
        empLine.addEventHandler(MouseEvent.MOUSE_ENTERED, a);
        empLine.addEventHandler(MouseEvent.MOUSE_EXITED, a);
        return empLine;
    }

    private HBox initEmpControlButtons(Employee employee) {
        HBox empLineButtons = new HBox();
        JFXRippler viewEmpButton = createIconButton("User-Info", "View Employee");
        JFXRippler editEmpButton = createIconButton("User-Edit", "Edit Employee");
        editEmpButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> loadEmployeeDialog(employee));
        viewEmpButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> viewEmployeeDialog(employee));
        empLineButtons.getChildren().add(editEmpButton);
        empLineButtons.getChildren().add(viewEmpButton);
        empLineButtons.getStyleClass().add("empLineButtons");
        this.empLineButtons = empLineButtons;
        return empLineButtons;
    }

    private JFXRippler createIconButton(String iconName, String tooltip) {

        Node glyph = null;

        //We will try and load glyph. If not available replace glyph with text
        try {
            glyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg."+iconName);
            ((SVGGlyph)glyph).setSize(27);
            ((SVGGlyph)glyph).setFill(javafx.scene.paint.Color.valueOf("#FFFFFF"));
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

    public void setDecorator(JFXDecorator decorator) {
        this.decorator = decorator;
    }

    private void p(Object a){ System.out.println(a); }

    void loadEmployeeDialog(Employee employee) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.getStyleClass().add("dialog");
        content.lookup(".jfx-layout-actions").setStyle("-fx-alignment: CENTER; -fx-spacing: 100");
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXTextField name = new JFXTextField(),
                position = new JFXTextField(),
                groupID = new JFXTextField(),
                username = new JFXTextField(),
                password = new JFXTextField();

        JFXRippler confirm = createIconButton("User-Check", "Confirm");
        JFXRippler cancel = createIconButton("User-Cancel", "Confrim");

        //TODO JOEL Validator for String vs. Integer
        name.setPromptText("Name");
        position.setPromptText("Position");
        groupID.setPromptText("Group Number");
        username.setPromptText("UserName");
        password.setPromptText("Password");

        Text text = new Text();
        text.setFill(Paint.valueOf("#FFFFFF"));
        text.setStyle("-fx-font: bold 16px \"System\" ;");

        if(employee != null) {
            text.setText("Edit Employee");
            content.setHeading(text);
            name.setText(employee.name);
            position.setText(employee.position);
            groupID.setText(Integer.toString(employee.groupID));
            username.setText(employee.username);
            password.setText(employee.password);
            confirm.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                OM.editEmployee(
                        employee.employeeID,
                        name.getText(),
                        position.getText(),
                        Integer.parseInt(groupID.getText()),
                        username.getText(),
                        password.getText()
                );
                refresh();
                dialog.close();
            });
        }
        else {
            text.setText("Add Employee");
            content.setHeading(text);
            confirm.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                OM.addEmployee(
                        name.getText(),
                        position.getText(),
                        Integer.parseInt(groupID.getText()),
                        username.getText(),
                        password.getText()
                );
                refresh();
                dialog.close();
            });
        }
        cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> dialog.close());
        VBox vBox = new VBox(name, position, groupID, username, password);
        vBox.setStyle("-fx-spacing: 15");
        content.setBody(vBox);
        content.setActions(confirm, cancel);
        dialog.show();
    }

    void viewEmployeeDialog(Employee employee) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.getStyleClass().add("dialog");
        content.lookup(".jfx-layout-actions").setStyle("-fx-alignment: CENTER; -fx-spacing: 100");
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        Text name = new Text(employee.name),
                position = new Text(employee.position),
                groupID = new Text(String.valueOf(employee.groupID)),
                username = new Text(employee.username),
                password = new Text(employee.password);
        VBox vBox = new VBox(name, position, groupID, username, password);
        vBox.setStyle("-fx-spacing: 15");
        content.setBody(vBox);
        dialog.show();
    }

    void refresh()
    {
        View = new VBox();
        Employees = OM.getEmployees();
        initView();
    }
}