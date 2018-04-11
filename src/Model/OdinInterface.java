package Model;


import Server.Employee;
import Server.Project;
import Server.Task;
import Server.WorkLog;

import java.util.List;

public interface OdinInterface
{
    //Edits
    boolean editEmployee(int employeeID, String name, String position, int groupID, String username, String password);
    boolean editProject(int projectID, String name, String dueDate, int groupID, int projectLeadID, String description, String status);
    boolean editTask(int taskID, String name, String dueDate, int employeeID, int projectID, String description, int size);
    boolean editWorkLog(int logID, String employeeID, String entryType, int taskID, String description);

    //Adds
    boolean addEmployee(String name, String position, int groupID, String username, String password);
    boolean addProject(String name, String dueDate, int groupID, int projectLeadID, String description, String status);
    boolean addTask(String name, String dueDate, int employeeID, int projectID, String description, int size);
    boolean addWorkLog(String employeeID, String entryType, int taskID, String description);

    //Get Singles
    Employee getEmployee(int employeeID);
    Project getProject(int projectID);
    Task getTask(int taskID);
    WorkLog getLog(int logID);

    //Get Sets
    List<Employee> getEmployees();
    List<Employee> getEmployeesGroup(int groupID);
    List<Project> getProjects();
    List<Project> getProjectsGroup(int groupID);
    List<Project> getProjectsLead(int projectLeadID);
    List<Project> getProjectsStatus(String status);
    List<Task> getTasks();
    List<Task> getTasksProject(int projectID);
    List<Task> getTasksEmployee(int employeeID);
    List<WorkLog> getLogs();
    List<WorkLog> getLogsEmployee(int employeeID);
    List<WorkLog> getLogsTask(int taskID);
}
