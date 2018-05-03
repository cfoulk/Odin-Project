package Server;

import java.sql.ResultSet;

public class WorkLog
{
    public int employeeID;
    public int taskID;
    public int logID;
    public String entryType;
    public String description;

    public WorkLog(ResultSet myRS) throws Exception
    {
        this.employeeID = myRS.getInt("EmployeeID");
        this.taskID = myRS.getInt("TaskID");
        this.logID = myRS.getInt("LogID");
        this.entryType = myRS.getString("EntryID");
        this.description = myRS.getString("Description");
    }

    public WorkLog(int employeeID, int taskID, int logID, String entryType, String description) {
        this.employeeID = employeeID;
        this.taskID = taskID;
        this.logID = logID;
        this.entryType = entryType;
        this.description = description;
    }
}
