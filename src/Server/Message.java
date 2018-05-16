package Server;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Message {
    public int messageID;
    public String message;
    public String status;
    public int recipientID;
    public int senderID;

    public Message(ResultSet myRS) throws SQLException {
        this.messageID = myRS.getInt("messageID");
        this.message = myRS.getString("message");
        this.status = myRS.getString("status");
        this.recipientID = myRS.getInt("recipientID");
        this.senderID = myRS.getInt("senderID");
    }

    public Message(int messageID, String message, String status, int recipientID, int senderID) {
        this.messageID = messageID;
        this.message = message;
        this.status = status;
        this.recipientID = recipientID;
        this.senderID = senderID;
    }
}
