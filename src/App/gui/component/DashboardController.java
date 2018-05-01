package App.gui.component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javax.tools.Tool;

public class DashboardController {

    @FXML
    private HBox UserBar;

    @FXML
    private SplitPane splitPane;

    @FXML
    private Text Username;

    public double heightHeader;

    public void initialize() throws Exception {
        UserBar.getChildren().add(createIconButton("Message", "Messenger"));
        UserBar.getChildren().add(createIconButton("Gear", "Settings"));
        UserBar.getChildren().add(createIconButton("Exit", "Logout"));



        heightHeader = 0.162;
        p(heightHeader);
        p(splitPane.getDividers().get(0).getPosition());

        splitPane.setDividerPosition(0,heightHeader);

        p(splitPane.getDividers().get(0).getPosition());



    }

    private JFXRippler createIconButton(String iconName, String message) throws Exception {

        SVGGlyph glyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg."+iconName);
        StackPane pane = new StackPane();

        glyph.setSize(30);
        glyph.setFill(Color.valueOf("#FFFFFF"));
        pane.getChildren().add(glyph);
        pane.setPadding(new Insets(11));

        JFXRippler rippler = new JFXRippler(pane);
        rippler.getRipplerRadius();
        rippler.getStyleClass().add("icon-rippler");
//        rippler.setRipplerFill(Color.valueOf("#254d87"));

        if(message != null || message != "") {
            Tooltip tooltip = new Tooltip(message);
            Tooltip.install(rippler,tooltip);
        }



        return rippler;
    }

    private void p(Object a){
        System.out.println(a);
    }


}
