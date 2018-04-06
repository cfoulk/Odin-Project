package Server;

import java.sql.ResultSet;

public class Task {
    public int dueDate;
    public int employeeID;
    public int projectID;
    public int size;
    public int taskID;
    public String name;
    public String description;

    public Task(ResultSet myRS) throws Exception {
        this.dueDate = myRS.getInt("DueDate");
        this.employeeID = myRS.getInt("EmployeeID");
        this.projectID = myRS.getInt("ProjectID");
        this.name = myRS.getString("Name");
        this.description = myRS.getString("Description");
        this.size = myRS.getInt("Size");
        this.taskID = myRS.getInt("TaskID");
    }

    String projectLine()
    {
        return (this.employeeID + "\t" + this.name + "\t" + this.projectID + "\t" +
                this.description + "\t" + this.dueDate + "\t" + this.size + "\t" +
                this.taskID);
    }
}
