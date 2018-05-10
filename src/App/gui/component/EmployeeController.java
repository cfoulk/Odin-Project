package App.gui.component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.Event;
import App.gui.persistentUser;
import Model.OdinModel;
import Server.Employee;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class EmployeeController {


    public EmployeeController() {}

    @FXML
    private JFXDecorator decorator;

    @FXML
    private StackPane stackPane;

    @FXML
    private SplitPane splitPane;

    @FXML
    private VBox View;

    private List<Employee> Employees;

    private HBox empLineButtons = new HBox();

    @FXML
    void initialize() throws Exception{
        persistentUser.initiateSampleData();
        OdinModel OM = new OdinModel();
        Employees = OM.getEmployees();

        initView();
    }

    private void initView() {
        for(int i = 0; i < Employees.size(); ++i)
        {
            HBox empLine = createEmpLine(Employees.get(i));
            empLine.setId(Integer.toString(i));
            empLine.setStyle("-fx-background-color: #2b5797");
            View.getChildren().add(empLine);
        }
    }

    public HBox createEmpLine(Employee employee) {
        HBox empLine = new HBox(new Label(employee.name));
        empLine.getStylesheets().add("empLine");
        EventHandler a = new EventHandler() {
            @Override
            public void handle(Event event) {
                if(event.getEventType().equals(MouseEvent.MOUSE_ENTERED)){
                    empLine.getChildren().add(initEmpControlButtons(empLine));
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

    private HBox initEmpControlButtons(HBox empLine) {
        HBox empLineButtons = new HBox();
        empLineButtons.getChildren().add(createIconButton("View", "View Employee"));
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
}