package Server;

import java.sql.ResultSet;
import java.util.Objects;

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
        this.name = myRS.getString("Name");
        this.dueDate = myRS.getString("DueDate");
        this.groupID = myRS.getInt("GroupID");
        this.projectLeadID = myRS.getInt("ProjectLeadID");
        this.description = myRS.getString("Description");
        this.status = myRS.getString("Status");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return projectID == project.projectID &&
                groupID == project.groupID &&
                projectLeadID == project.projectLeadID &&
                Objects.equals(name, project.name) &&
                Objects.equals(description, project.description) &&
                Objects.equals(status, project.status) &&
                Objects.equals(dueDate, project.dueDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(projectID, groupID, projectLeadID, name, description, status, dueDate);
    }
}
