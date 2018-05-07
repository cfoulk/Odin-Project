package Server;

import java.sql.ResultSet;

public class WorkLog
{
    public int logID;
    public int taskID;
    public int employeeID;
    public String elapsedTime;
    public String startTime;
    public String stopTime;
    public String description;

    public WorkLog(ResultSet myRS) throws Exception
    {
        this.logID = myRS.getInt("LogID");
        this.taskID = myRS.getInt("TaskID");
        this.employeeID = myRS.getInt("EmployeeID");
        this.elapsedTime = myRS.getString("ElapsedTime");
        this.startTime = myRS.getString("StartTime");
        this.stopTime = myRS.getString("StopTime");
        this.description = myRS.getString("Description");
    }

    public WorkLog(int logID, int taskID, int employeeID, String elapsedTime, String startTime, String stopTime, String description)
    {
        this.logID = logID;
        this.taskID = taskID;
        this.employeeID = employeeID;
        this.elapsedTime = elapsedTime;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.description = description;
    }
}
