package Model;


public interface OdinInterface
{
    //Edits
    boolean editEmployee(String name, String position, int groupID, String username, String password);
    boolean editProject(String name, String dueDate, int groupID, int projectLeadID, String description, String status);
    boolean editTask(String name, String dueDate, int employeeID, int projectID, String description, int size);
    boolean editWorkLog(String employeeID, String entryType, int taskID, String description);

    //Adds
    boolean addEmployee(String name, String position, int groupID, String username, String password);
    boolean addProject(String name, String dueDate, int groupID, int projectLeadID, String description, String status);
    boolean addTask(String name, String dueDate, int employeeID, int projectID, String description, int size);
    boolean addWorkLog(String employeeID, String entryType, int taskID, String description);
}
