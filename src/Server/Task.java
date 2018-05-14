package Server;

import java.sql.ResultSet;
import java.sql.Date;

public class Task {
    public int taskID;
    public String name; //
    public String dueDate; //
    public int projectID;//
    public String employees; //
    public String description;
    public int size;//
    public String status; //

    public Task(ResultSet myRS) throws Exception {
        this.taskID = myRS.getInt("TaskID");
        this.name = myRS.getString("Name");
        this.dueDate = myRS.getString("DueDate");
        this.projectID = myRS.getInt("ProjectID");
        this.employees = myRS.getString   ("Employees");
        this.description = myRS.getString("Description");
        this.size = myRS.getInt("Size");
        this.status = myRS.getString("Status");
    }

    public Task(int taskID, String name, String dueDate, int projectID, String employees, String description, int size, String status) {
        this.taskID = taskID;
        this.name = name;
        this.dueDate = dueDate;
        this.projectID = projectID;
        this.employees = employees;
        this.description = description;
        this.size = size;
        this.status = status;
    }

    public Task(String name, String dueDate, int projectID, String employees, String description, int size, String status)
    {
        this.name = name;
        this.dueDate = dueDate;
        this.projectID = projectID;
        this.employees = employees;
        this.description = description;
        this.size = size;
        this.status = status;
    }

    public boolean hasEmployee(int employeeID)
    {
        return(this.employees.contains("," + employeeID + ","));
    }

    String taskLine()
    {
        return (this.employees + "\t" + this.name + "\t" + this.projectID + "\t" +
                this.description + "\t" + this.dueDate + "\t" + this.size + "\t" +
                this.taskID);
    }
}
