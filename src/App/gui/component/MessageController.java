package App.gui.component;

import Model.OdinModel;
import Server.Employee;
import Server.Message;
import com.jfoenix.controls.*;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import javafx.application.Platform;
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
import javafx.scene.input.MouseEvent;
import java.util.List;




public class MessageController {

    private static Employee User;
    private static List<Employee> Employees;
    private static List<Message> Messages;
    private static OdinModel OM;
    private static JFXDecorator decorator;

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

    @FXML
    void initialize()
    {
        Platform.runLater(() -> {
            initHeader();
            initView(request);
        });
    }

    void initHeader()
    {
        JFXRippler composeButton = createIconButton("Add", "Create Message");
        JFXRippler viewAllButton = createIconButton("Message", "View All Messages");
        JFXRippler viewReadButton = createIconButton("Check", "View Read Messages");
        JFXRippler viewUnreadButton = createIconButton("Cancel", "View Unread Messages");
        //JFXRippler viewSentButton = createIconButton("Arrowhead-Right", "View Sent Messages");
        Label viewing = new Label("Viewing: " + request + " messages");
        composeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> composeMessage());
        viewAllButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            request = "All";
            refresh();
        });
        viewReadButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            request = "Read";
            refresh();
        });
        viewUnreadButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            request = "Unread";
            refresh();
        });
        /*viewSentButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            request = "Sent";
            refresh();
        });*/
        Header.getChildren().addAll(viewing, composeButton, viewAllButton, viewReadButton, viewUnreadButton);
    }

    void initView(String request)
    {
        List<Message> messages;
        if(request.equals("All")) messages = Messages;
        else if(request.equals("Unread")) messages = OM.filterMessages_Unread(Messages);
        else messages = OM.filterMessages_Read(Messages);
        //else messages = OM.filterMessages_SenderID(Messages, User.employeeID);
        if(messages != null && messages.size() > 0) for(Message message : messages) View.getChildren().add(createMessageLine(message));
        else
        {
            Label label = new Label("You have no messages!");
            label.setStyle("-fx-text-fill: #000000");
            View.getChildren().add(label);
        }
    }

    HBox createMessageLine(Message message)
    {
        //When viewing a message, if in Unread mode, remove it from the list.
        HBox messageLine, messageButtons;
        String from, body;
        from = OM.filterEmployees_EmployeeID(Employees, message.senderID).name;
        if(message.message.length() > 40) body = message.message.substring(0, 40) + "...";
        else body = message.message;
        messageLine = new HBox(new Label("From: " + from + " Contains: " + body));
        messageButtons = initMessageButtons(message);
        messageLine.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> messageLine.getChildren().add(messageButtons));
        messageLine.addEventHandler(MouseEvent.MOUSE_EXITED, event -> messageLine.getChildren().remove(messageButtons));
        messageLine.getStyleClass().add("empLine");
        return messageLine;
    }

    HBox initMessageButtons(Message message)
    {
        HBox messageButtons;
        JFXRippler viewMessage, markRead;
        viewMessage = createIconButton("View", "View message");
        markRead = createIconButton("Check", "Mark read");
        viewMessage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> viewMessageDialog(message));
        markRead.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> markMessageRead(message));
        messageButtons = new HBox(viewMessage, markRead);
        return messageButtons;
    }

    private void viewMessageDialog(Message message) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.getStyleClass().add("dialog");
        content.lookup(".jfx-layout-actions").setStyle("-fx-alignment: CENTER; -fx-spacing: 100");
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        String  from = OM.filterEmployees_EmployeeID(Employees, message.senderID).name,
                body = message.message;
        Label   fromLabel = new Label(from),
                bodyLabel = new Label(body);
        bodyLabel.setWrapText(true);
        VBox vBox = new VBox(fromLabel, bodyLabel);
        vBox.setStyle("-fx-spacing: 15");
        content.setBody(vBox);
        markMessageRead(message);
        refresh();
        dialog.show();
    }

    private void composeMessage(){
        JFXDialogLayout content = new JFXDialogLayout();
        content.getStyleClass().add("dialog");
        content.lookup(".jfx-layout-actions").setStyle("-fx-alignment: CENTER; -fx-spacing: 100");
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXRippler confirm = createIconButton("Check", "Confirm");
        JFXRippler cancel = createIconButton("Cancel", "Confrim");
        JFXTextField recipient = new JFXTextField();
        JFXTextArea message = new JFXTextArea();
        recipient.setPromptText("Recipient's ID");
        message.setPromptText("Enter your message");
        confirm.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(messageIsValid(recipient,message))
                OM.addMessage(message.getText(),Integer.parseInt(recipient.getText()),User.employeeID);
                refresh();
                dialog.close();
        });
        cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> dialog.close());
        VBox vBox = new VBox(recipient,message,confirm,cancel);
        vBox.setStyle("-fx-spacing: 15");
        content.setBody(vBox);
        content.setActions(confirm, cancel);
        dialog.show();
    }

    private boolean messageIsValid(JFXTextField recipient, JFXTextArea message)
    {
        boolean valid = true;

        if(recipient.getText().isEmpty())
        {
            dialogError_JFXTF(recipient,"Recipient ID cannot be empty");
            valid = false;
        }
        else if(!OM.isValidNum(recipient.getText()))
        {
            dialogError_JFXTF(recipient, "Recipient ID must be an integer");
            valid = false;
        }
        else recipient.setStyle("-fx-background-color: #FFFFFF");

        if(message.getText().isEmpty())
        {
            dialogError_JFXTF(message, "Message cannot be empty");
            valid = false;
        }
        else if(!OM.isValidString(message.getText()))
        {
            dialogError_JFXTF(message, "Description cannot contain special characters");
            valid = false;
        }
        else message.setStyle("-fx-background-color: #FFFFFF");

        return valid;
    }

    void dialogError_JFXTF(JFXTextArea input, String message)
    {
        input.clear();
        input.setPromptText(message);
        input.setStyle("-fx-background-color: #FFCDD2");
    }

    void dialogError_JFXTF(JFXTextField input, String message)
    {
        input.clear();
        input.setPromptText(message);
        input.setStyle("-fx-background-color: #FFCDD2");
    }

    private void markMessageRead(Message message) {
        OM.setMessage_Read(message.messageID);
        refresh();
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

    public void setDecorator(JFXDecorator decorator) {
        this.decorator = decorator;
    }

    private void refresh()
    {
        View.getChildren().remove(0, View.getChildren().size());
        initView(request);
    }
}
