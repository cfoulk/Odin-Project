package Server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Message {
    public int messageID;
    public String message;
    public String status;
    public int recipientID;
    public int senderID;

    public Message(ResultSet myRS) throws SQLException {
        this.messageID = myRS.getInt("MessageID");
        this.message = myRS.getString("Message");
        this.status = myRS.getString("Status");
        this.recipientID = myRS.getInt("RecipientID");
        this.senderID = myRS.getInt("SenderID");
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
