package App.gui.component;

import Model.OdinModel;
import Server.Employee;
import Server.Message;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.List;

public class MessageController {

    private Employee User;
    private List<Employee> Employees;
    private List<Message> Messages;
    private OdinModel OM;

    @FXML
    private StackPane stackPane;

    @FXML
    private HBox Header;

    @FXML
    private ScrollPane View;


    public void load(Employee user, List<Employee> employees, List<Message> messages, OdinModel om)
    {
        User = user;
        Employees = employees;
        Messages = messages;
        OM = om;
    }

    void initialize()
    {
        initHeader();
        initView();
    }

    void initHeader()
    {
        JFXRippler composeButton = createIconButton("Add", "Create Message");
        //JFXRippler viewUnreadButton = createIconButton("")
    }

    void initView()
    {

    }

    private JFXRippler createIconButton(String iconName, String tooltip) {

        Node glyph = null;

        //We will try and load glyph. If not available replace glyph with text
        try {
            glyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg." + iconName);
            ((SVGGlyph) glyph).setSize(26);
            ((SVGGlyph) glyph).setFill(Color.valueOf("#FFFFFF"));
        } catch (Exception e) {
            System.out.println("Glyph does not exist!");
            glyph = new Text(iconName);
            ((Text) glyph).setFill(Color.valueOf("#FFFFFF"));
        }

        StackPane pane = new StackPane();
        pane.getChildren().add(glyph);
        pane.setPadding(new Insets(10));

        JFXRippler rippler = new JFXRippler(pane);
        rippler.getRipplerRadius();
        rippler.getStyleClass().add("icon-rippler");

        if (tooltip != null || tooltip != "") {
            Tooltip toolTip = new Tooltip(tooltip);
            Tooltip.install(rippler, toolTip);
        }

        return rippler;
    }
}
