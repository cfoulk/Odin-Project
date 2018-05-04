package App.gui.component;

import App.gui.persistentUser;
import Server.Project;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.sun.deploy.xml.XMLable;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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

    public double heightHeader;

    private HBox projectLineButtons = new HBox();

    public void initialize() throws Exception {
        UserBar.getChildren().add(createIconButton("Message", "Messenger"));
        UserBar.getChildren().add(createIconButton("Gear", "Settings"));
        UserBar.getChildren().add(createIconButton("Exit", "Logout"));

        persistentUser.initiateSampleData();

        List<Project> a = persistentUser.projectList;

        initProjectButtons();

        initView(a);

        heightHeader = 0.162;
        p(heightHeader);
        p(splitPane.getDividers().get(0).getPosition());
        splitPane.setDividerPosition(0,heightHeader);
        p(splitPane.getDividers().get(0).getPosition());

    }

    public void initProjectButtons() throws Exception {
        projectLineButtons.getChildren().add(createIconButton("View","ViewProject"));

        projectLineButtons.getStyleClass().add("projectLineButtons");
    }

    private void initView(List<Project> projects){
        for(int i = 0; i < projects.size();i++ ){
            View.getChildren().add(createProjectLine(projects.get(i)));
        }
    }

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

//    public EventHandler ProjectHover(){
//
//    }


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


}
