package Server;

import java.sql.ResultSet;

public class Project
{
    public int projectID;
    public int groupID;
    public int projectLeadID;
    public String name;
    public String description;
    public String status;
    public String dueDate;

    public Project(ResultSet myRS) throws Exception
    {
        this.projectID = myRS.getInt("ProjectID");
        this.groupID = myRS.getInt("GroupID");
        this.projectLeadID = myRS.getInt("ProjectLeadID");
        this.name = myRS.getString("Name");
        this.description = myRS.getString("Description");
        this.status = myRS.getString("Status");
        this.dueDate = myRS.getString("DueDate");
    }

    public Project(int projectID, int groupID, int projectLeadID, String name, String description, String status, String dueDate) {
        this.projectID = projectID;
        this.groupID = groupID;
        this.projectLeadID = projectLeadID;
        this.name = name;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
    }

    String projectLine()
    {
        return (this.projectID + "\t" + this.name + "\t" + this.status + "\t" +
                this.groupID + "\t" + this.projectLeadID + "\t" + this.dueDate + "\t" +
                this.description);
    }
}
