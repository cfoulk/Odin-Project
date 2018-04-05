package Server;

import java.sql.ResultSet;

public class WorkLog
{
    int employeeID;
    int taskID;
    int logID;
    String entryType;
    String description;

    public WorkLog(ResultSet myRS) throws Exception
    {
        this.employeeID = myRS.getInt("EmployeeID");
        this.taskID = myRS.getInt("TaskID");
        this.logID = myRS.getInt("LogID");
        this.entryType = myRS.getString("EntryID");
        this.description = myRS.getString("Description");
    }


}
