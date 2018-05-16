package Model;


import Server.Employee;
import Server.Project;
import Server.Task;
import Server.WorkLog;
import Server.Message;

import java.util.List;

public interface OdinInterface
{
    //Edits
    boolean editEmployee(int employeeID, String name, String position, int groupID, String username, String password, String status);
    boolean editProject(int projectID, String name, String dueDate, int groupID, int projectLeadID, String description, String status);
    boolean editTask(int taskID, String name, String dueDate, int projectID, String employees, String description, int size, String status);
    public boolean editWorkLog(int logID, String startTime, String stopTime, String elapsedTime, String description, int taskID, int employeeID);
    public boolean stopWork(int logID, String description);
    boolean setMessage_Read(int MessageID);
    boolean setProject_Closed(int ProjectID);

    //Adds
    boolean addEmployee(String name, String position, int groupID, String username, String password, String status);
    boolean addProject(String name, String dueDate, int groupID, int projectLeadID, String description, String status);
    boolean addTask(String name, String dueDate, int projectID, String employees, String description, int size, String status);
    boolean addWorkLog(String startTime, String stopTime, String elapsedTime, String description, int taskID, int employeeID);
    public int startWork(int taskID, int employeeID);
    boolean addMessage(String message, int recipientID, int senderID);

    //deletes
    boolean deleteEmployee_EmployeeID(int employeeID);
    boolean deleteTask_TaskID(int taskID);
    boolean deleteMessage_MessageID(int messageID); //
    boolean deleteMessages_RecipientID(int recipientID);
    boolean deleteMessages_SenderID(int senderID);

    //Get Singles
    Employee getEmployee_EmployeeID(int employeeID);
    Employee getEmployee_Username(String username);
    Project getProject_ProjectID(int projectID);
    Task getTask_TaskID(int taskID);
    WorkLog getWorkLog_LogID(int logID);
    Message getMessage_MessageID(int messageID);

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
    List<Message> getMessages();
    List<Message> getMessages_RecipientID(int employeeID);
    List<Message> getMessages_SenderID(int senderID);

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
    List<Task> filterTasks_EmployeeID(List<Task> list, int employeeID);
    List<Task> filterTasks_ProjectID(List<Task> list, int projectID);
    List<Task> filterTasks_Status(List<Task> list, String status);
    List<Task> filterTasks_Size(List<Task> list, int size);
    WorkLog filterWorkLog_LogID(List<WorkLog> list, int logID);
    List<WorkLog> filterWorkLog_TaskID(List<WorkLog> list, int taskID);
    List<WorkLog> filterWorkLog_EmployeeID(List<WorkLog> list, int EmployeeID);
    List<Message> filterMessages_RecipientID(List<Message> list, int RecipientID);
    List<Message> filterMessages_SenderID(List<Message> list, int SenderID, int RecipientID);
    List<Message> filterMessages_Read(List<Message> list);
    List<Message> filterMessages_Unread(List<Message> list);
}
