package Server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Message {
    public int messageID;
    String message;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return messageID == message1.messageID &&
                recipientID == message1.recipientID &&
                senderID == message1.senderID &&
                Objects.equals(message, message1.message) &&
                Objects.equals(status, message1.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageID, message, status, recipientID, senderID);
    }
}
