package Server;

import java.sql.ResultSet;

public class Task {
    int dueDate;
    int employeeID;
    int projectID;
    int size;
    int taskID;
    String name;
    String description;

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
