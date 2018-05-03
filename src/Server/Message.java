package Server;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Message {
    public int messageID;
    String message;
    public String status;
    public int employeeID;
    public int senderID;

    public Message(ResultSet myRS) throws SQLException {
        this.messageID = myRS.getInt("messageID");
        this.message = myRS.getString("message");
        this.status = myRS.getString("status");
        this.employeeID = myRS.getInt("employeeID");
        this.senderID = myRS.getInt("senderID");
    }

    public Message(int messageID, String message, String status, int employeeID, int senderID) {
        this.messageID = messageID;
        this.message = message;
        this.status = status;
        this.employeeID = employeeID;
        this.senderID = senderID;
    }
}
