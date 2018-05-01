package Server;

import java.sql.ResultSet;
import java.sql.Date;

public class Task {
    public String employees; //
    public int projectID;//
    public int size;//
    public int taskID;
    public String name; //
    public String description;
    public String dueDate; //
    public String status; //

    public Task(ResultSet myRS) throws Exception {
        this.employees = myRS.getString   ("Employees");
        this.projectID = myRS.getInt("ProjectID");
        this.name = myRS.getString("Name");
        this.description = myRS.getString("Description");
        this.size = myRS.getInt("Size");
        this.taskID = myRS.getInt("TaskID");
        this.dueDate = myRS.getString("DueDate");
        this.status = myRS.getString("Status");
    }

    String taskLine()
    {
        return (this.employees + "\t" + this.name + "\t" + this.projectID + "\t" +
                this.description + "\t" + this.dueDate + "\t" + this.size + "\t" +
                this.taskID);
    }
}
