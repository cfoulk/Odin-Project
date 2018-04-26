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
    boolean editTask(int taskID, String name, String dueDate, String employees, int projectID, String description, int size, String status);
    boolean editWorkLog(int logID, String employeeID, String entryType, int taskID, String description);

    //Adds
    boolean addEmployee(String name, String position, int groupID, String username, String password);
    boolean addProject(String name, String dueDate, int groupID, int projectLeadID, String description, String status);
    boolean addTask(String name, String dueDate, int projectID, String employees, String description, int size, String status);
    boolean addWorkLog(int employeeID, String entryType, int taskID, String description);

    //Get Singles
    Employee getEmployee_EmployeeID(int employeeID);
    Employee getEmployee_Username(String username);
    Project getProject_ProjectID(int projectID);
    Task getTask_TaskID(int taskID);
    WorkLog getWorkLog_LogID(int logID);

    //Get Sets
    List<Employee> getEmployees();
    List<Employee> getEmployees_GroupID(int groupID);
    List<Project> getProjects();
    List<Project> getProjects_GroupID(int groupID);
    List<Project> getProjects_ProjectLeadID(int projectLeadID);
    List<Project> getProjects_Status(String status);
    List<Task> getTasks();
    List<Task> getTasks_ProjectID(int projectID);
    List<Task> getTasks_EmployeeID(int employeeID);
    List<WorkLog> getWorkLogs();
    List<WorkLog> getWorkLogs_EmployeeID(int employeeID);
    List<WorkLog> getWorkLogs_TaskID(int taskID);

    //Filter sets
    Employee filterEmployees_EmployeeID(List<Employee> list, int employeeID);
    Employee filterEmployees_Username(List<Employee> list, String username);
    List<Employee> filterEmployees_GroupID(List<Employee> list, int groupID);
    List<Employee> filterEmployees_Position(List<Employee> list, String position);
    Project filterProjects_ProjectID(List<Project> list, int projectID);
    List<Project> filterProjects_DueDate(List<Project> list, String dueDate);
    List<Project> filterProjects_GroupID(List<Project> list, int groupID);
    List<Project> filterProjects_ProjectLeadID(List<Project> list, int projectLeadID);
    List<Project> filterProjects_Status(List<Project> list, String status);
    Task filterTasks_TaskID(List<Task> list, int taskID);
    List<Task> filterTasks_DueDate(List<Task> list, String dueDate);
    List<Task> filterTasks_EmployeeID(List<Task> list, String employeeID);
    List<Task> filterTasks_ProjectID(List<Task> list, int projectID);
    List<Task> filterTasks_Status(List<Task> list, String status);
    List<Task> filterTasks_Size(List<Task> list, int size);
    WorkLog filterWorkLog_LogID(List<WorkLog> list, int logID);
    List<WorkLog> filterWorkLog_EntryType(List<WorkLog> list, String entryType);
    List<WorkLog> filterWorkLog_TaskID(List<WorkLog> list, int taskID);
    List<WorkLog> filterWorkLog_EmployeeID(List<WorkLog> list, int EmployeeID);
}
