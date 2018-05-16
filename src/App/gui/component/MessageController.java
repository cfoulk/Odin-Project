package App.gui.component;

import Model.OdinModel;
import Server.Employee;
import Server.Message;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.awt.event.MouseEvent;
import java.util.List;




public class MessageController {

    private Employee User;
    private List<Employee> Employees;
    private List<Message> Messages;
    private OdinModel OM;

    private String request = "All";

    @FXML
    private StackPane stackPane;

    @FXML
    private HBox Header;

    @FXML
    private VBox View;


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
        initView(request);
    }

    void initHeader()
    {
        JFXRippler composeButton = createIconButton("Add", "Create Message");
        JFXRippler viewAllButton = createIconButton("Message", "View All Messages");
        JFXRippler viewReadButton = createIconButton("Check", "View Read Messages");
        JFXRippler viewUnreadButton = createIconButton("Cancel", "View Unread Messages");
        Header.getChildren().addAll(composeButton, viewAllButton, viewReadButton, viewUnreadButton);
    }

    void initView(String request)
    {
        List<Message> messages;
        if(request.equals("All")) messages = Messages;
        else if(request.equals("Unread")) messages = OM.filterMessages_Unread(Messages);
        else messages = OM.filterMessages_Read(Messages);
        for(Message message : messages) View.getChildren().add(createMessageLine(message));
    }

    HBox createMessageLine(Message message)
    {
        //When viewing a message, if in Unread mode, remove it from the list.
        HBox messageLine, messageButtons;
        String from, body;
        from = OM.filterEmployees_EmployeeID(Employees, message.senderID).name;
        if(message.message.length() > 10) body = message.message.substring(0, 10) + "...";
        else body = message.message;
        messageLine = new HBox(new Label("From: " + from + " Contains: " + body));
        messageButtons = initMessageButtons();
        EventHandler a = new EventHandler() {
            @Override
            public void handle(Event event) {
                if (event.getEventType().equals(javafx.scene.input.MouseEvent.MOUSE_ENTERED)) {
                    messageButtons.getChildren().add(messageButtons);
                }
                if (event.getEventType().equals(javafx.scene.input.MouseEvent.MOUSE_EXITED)) {
                    messageButtons.getChildren().remove(messageButtons);
                }
            }
        };
        return messageLine;
    }

    HBox initMessageButtons()
    {
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
