package App.gui.component.util;

import Model.OdinModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import javafx.event.ActionEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.SQLException;

public class ConnectionStatus {

    OdinModel OM = null;
    Thread liveThread = null;

    boolean run = true;

    public JFXRippler connectionStatus = null;

    private SVGGlyph connection;

    public ConnectionStatus(OdinModel OM) {
        this.OM = OM;
        try {
            connection = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.Connection-Good");
            connection.setSize(25);
            connection.setFill(Color.valueOf("#9E9E9E"));
            connection.setStyle("-fx-cursor: hand");
        } catch (Exception e) {
            System.out.println("Connection symbol unavailable");
            connection = new SVGGlyph("");
        }

        connectionStatus = new JFXRippler();
        connectionStatus.setStyle("-jfx-mask-type: CIRCLE");
        connectionStatus.getChildren().add(connection);
    }

    private class checkThread extends Thread {
        public void run() {
            while (run || !Thread.currentThread().isInterrupted()) {
                checkConnection();
                try {
                    Thread.sleep((long) 2000);
                } catch (InterruptedException e) {
                    System.out.println("End");
                }
            }
        }
    }

    public JFXRippler getIcon() {
        if (liveThread == null) {
            liveThread = new checkThread();
            liveThread.setDaemon(true);
            liveThread.start();
        }
        return connectionStatus;
    }

    public void close() {
        if (!liveThread.isInterrupted()) {
            liveThread.interrupt();
            System.out.println("Closed");
        }

        run = false;
    }

    void checkConnection() {
        try {
            if (OM == null) {
                OM = new OdinModel();
            }
            if (!OM.isClosed()) {
                connection.setStyle("-fx-background-color: #00E676");
                return;
            }
        } catch (SQLException e) {
        } catch (IOException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        connection.setStyle("-fx-background-color: #D32F2F");
    }
}
