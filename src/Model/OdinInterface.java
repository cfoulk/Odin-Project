package Model;

import Server.OdinServer;

public interface OdinInterface {
    //Edits
    boolean editEmployee(String name, String position, String password, int groupID, int employeeID, String username);
    boolean editProject(String name, String dueDate, int groupID, int projectLeadID, String description, String status, int projectID);
    boolean editTask(String dueDate, int employeeID, int projectID, String description, int size, String name, int taskID);
    boolean editWorkLog(String employeeID, String entryType, int taskID, String description, int logID);
}
