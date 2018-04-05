package Server;

import java.sql.ResultSet;

public class Project
{
    int projectID;
    int groupID;
    int projectLeadID;
    String name;
    String description;
    String status;
    String dueDate;

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

    String projectLine()
    {
        return (this.projectID + "\t" + this.name + "\t" + this.status + "\t" +
                this.groupID + "\t" + this.projectLeadID + "\t" + this.dueDate + "\t" +
                this.description);
    }
}
