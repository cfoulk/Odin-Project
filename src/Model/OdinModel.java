package Model;

import Server.*;
import com.sun.corba.se.spi.orbutil.threadpool.Work;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OdinModel implements OdinInterface
{
    OdinServer OS;

    public OdinModel() throws IOException, SQLException
    {
        OS = new OdinServer();
    }

    //Returns the employee's ID number on success.
    //Returns -1 if the username does not exist.
    //Returns -2 if the username exists, but the password is wrong.
    public int getUserID(String userName, String password)
    {
        Employee emp;
        try {
            emp = OS.getEmployee_Username(userName);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        if (emp == null) return -1;
        if (emp.passwordCheck(password)) return emp.employeeID;
        return -2;
    }

    //Add methods
    public boolean addEmployee(String name, String position, int groupID, String username, String password)
    {
        Employee emp;
        try
        {
            emp = OS.getEmployee_Username(username);
            if (emp == null)
            {
                OS.addEmployee(name, position, groupID, username, password);
                return true;
            }
        }
        catch(Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean addProject(String name, String dueDate, int groupID, int projectLeadID, String description, String status)
    {
        Employee lead;
        try
        {
            lead = OS.getEmployee(projectLeadID);
            if(lead != null && lead.position.compareTo("Project Lead") == 0)
            {
                OS.addProject(name, Date.valueOf(dueDate), groupID, projectLeadID, description, status);
                return true;
            }
        }
        catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean addTask(String name, String dueDate, int employeeID, int projectID, String description, int size)
    {
        Employee emp;
        try
        {
            emp = OS.getEmployee(employeeID);
            if(emp != null) OS.addTask(name, dueDate, employeeID, projectID, description, size);
            return true;
        }
        catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean addWorkLog(int employeeID, String entryType, int taskID, String description)
    {
        Employee emp;
        try
        {
            emp = OS.getEmployee(employeeID);
            if(emp != null) OS.addWorkLog(employeeID, entryType, taskID, description);
            return true;
        }
        catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    //Edit methods
    public boolean editEmployee(int employeeID, String name, String position, int groupID, String username, String password)
    {
        Employee emp;
        try
        {
            emp = OS.getEmployee(employeeID);
            if(emp != null)
            {
                emp = OS.getEmployee_Username(username);
                if(emp ==null ) {
                    OS.editEmployee(employeeID, name, position, groupID, username, password);
                    return true;
                }
            }
        }
        catch(Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean editProject(int projectID, String name, String dueDate, int groupID, int projectLeadID, String description, String status)
    {
        Project proj;
        Employee lead;
        try
        {
            lead = OS.getEmployee(projectLeadID);
            proj = OS.getProject_ProjectID(projectID);
            if ( proj != null && lead != null && lead.position.compareTo("Project Lead") == 0)
            {
                OS.editProject(projectID,name,dueDate,groupID,projectLeadID,description,status);
                return true;
            }
        }
        catch (Exception e) {e.printStackTrace();}
        return false;
    }

    public boolean editTask(int taskID, String name, String dueDate, int employeeID, int projectID, String description, int size)
    {
        Task task;
        Employee emp;
        try
        {
            emp = OS.getEmployee(employeeID);
            task = OS.getTask(taskID);
            if (task != null && emp != null)
            {
                OS.editTask(taskID,name,dueDate,employeeID,projectID,description,size);
                return true;
            }
        }
        catch (Exception e) {e.printStackTrace() ;}
        return false;
    }

    public boolean editWorkLog(int logID, String employeeID, String entryType, int taskID, String description)
    {
        WorkLog log;
        try
        {
            log = OS.getWorkLog(logID);
            if (log != null)
            {
                OS.editWorkLog(logID,employeeID,entryType, taskID,description);
                return true;
            }
        }
        catch (Exception e) {e.printStackTrace() ;}
        return false;
    }

    //Gets
    public Employee getEmployee_EmployeeID(int employeeID)
    {
        Employee employee;
        try { employee = OS.getEmployee(employeeID); }
        catch (Exception e) { return null; }
        return employee;
    }

    public Employee getEmployee_Username(String username)
    {
        Employee employee;
        try { employee = OS.getEmployee_Username(username); }
        catch (Exception e) { return null; }
        return employee;
    }

    public Project getProject_ProjectID(int projectID)
    {
        Project project;
        try { project = OS.getProject_ProjectID(projectID); }
        catch (Exception e) { return null; }
        return project;
    }

    public Task getTask_TaskID(int taskID)
    {
        Task task;
        try { task = OS.getTask(taskID); }
        catch (Exception e) { return null; }
        return task;
    }

    public WorkLog getWorkLog_LogID(int logID)
    {
        WorkLog workLog;
        try { workLog = OS.getWorkLog(logID); }
        catch (Exception e) { return null; }
        return workLog;
    }

    public List<Employee> getEmployees()
    {
        List<Employee> employees;
        try { employees = OS.getEmployees(); }
        catch (Exception e) { return null; }
        return employees;
    }

    public List<Employee> getEmployees_GroupID(int groupID)
    {
        List<Employee> employees;
        try { employees = OS.getEmployees_GroupID(groupID); }
        catch (Exception e) { return null; }
        return employees;
    }

    public List<Project> getProjects()
    {
        List<Project> projects;
        try { projects = OS.getProjects(); }
        catch(Exception e) { return null; }
        return projects;
    }

    public List<Project> getProjects_GroupID(int groupID)
    {
        List<Project> projects;
        try { projects = OS.getProject_GroupID(groupID); }
        catch(Exception e) { return null;}
        return projects;
    }

    public List<Project> getProjects_ProjectLeadID(int projectLeadID)
    {
        List<Project> projects;
        try { projects = OS.getProject_ProjectLeadID(projectLeadID); }
        catch(Exception e) { return null; }
        return projects;
    }

    public List<Project> getProjects_Status(String status)
    {
        List<Project> projects;
        try { projects = OS.getProjects(); } //place holder, need to implement correct method into Odinserver
        catch (Exception e) { return null; }
        return projects;
    }

    public List<Task> getTasks()
    {
        List<Task> tasks;
        try { tasks = OS.getTasks(); }
        catch (Exception e) { return null; }
        return tasks;
    }

    public List<Task> getTasks_ProjectID(int projectID)
    {
        List<Task> tasks;
        try { tasks = OS.getTasks_ProjectID(projectID); }
        catch (Exception e) { return null; }
        return tasks;
    }

    public List<Task> getTasks_EmployeeID(int employeeID)
    {
        List<Task> tasks;
        try { tasks = OS.getTasks_EmployeeID(employeeID); }
        catch (Exception e) { return null; }
        return tasks;
    }

    public List<WorkLog> getWorkLogs()
    {
        List<WorkLog> workLogs;
        try { workLogs = OS.getWorkLogs(); }
        catch (Exception e) { return null; }
        return workLogs;
    }

    public List<WorkLog> getWorkLogs_EmployeeID(int employeeID)
    {
        List<WorkLog> workLogs;
        try { workLogs = OS.getWorkLogs_EmployeeID(employeeID); }
        catch (Exception e) { return null;}
        return workLogs;
    }

    public List<WorkLog> getWorkLogs_TaskID(int taskID)
    {
        List<WorkLog> workLogs;
        try { workLogs = OS.getWorkLogs_TaskID(taskID); }
        catch (Exception e) { return null; }
        return workLogs;
    }

    //Filters
    public Employee filterEmployees_EmployeeID(List<Employee> list, int employeeID)
    {
        for (Employee employee : list)
            { if(employee.employeeID == employeeID) return employee; }
        return null;
    }

    public Employee filterEmployees_Username(List<Employee> list, String username)
    {
        for (Employee employee : list)
            { if(employee.username.toLowerCase().compareTo(username.toLowerCase()) == 0) return employee; }
        return null;
    }

    public List<Employee> filterEmployees_GroupID(List<Employee> list, int groupID)
    {
        List<Employee> employees = new ArrayList<>();
        for (Employee employee : list)
            { if(employee.groupID == groupID) employees.add(employee); }
        return employees;
    }

    public List<Employee> filterEmployees_Position(List<Employee> list, String position)
    {
        List<Employee> employees = new ArrayList<>();
        for (Employee employee : list)
            { if(employee.position.compareTo(position) == 0) employees.add(employee); }
        return employees;
    }

    public Project filterProjects_ProjectID(List<Project> list, int projectID)
    {
        for (Project project : list)
            { if(project.projectID == projectID) return project; }
        return null;
    }

    public List<Project> filterProjects_DueDate(List<Project> list, String dueDate)
    {
        List<Project> projects = new ArrayList<>();
        for(Project project : list)
        { if(project.dueDate.toLowerCase().compareTo(dueDate.toLowerCase()) == 0) projects.add(project); }
        return projects;
    }

    public List<Project> filterProjects_GroupID(List<Project> list, int groupID)
    {
        List<Project> projects = new ArrayList<>();
        for(Project project : list)
        { if(project.groupID == groupID) projects.add(project); }
        return projects;
    }

    public List<Project> filterProjects_ProjectLeadID(List<Project> list, int projectLeadID)
    {
        List<Project> projects = new ArrayList<>();
        for(Project project : list)
            { if(project.projectLeadID == projectLeadID) projects.add(project); }
        return projects;
    }

    public List<Project> filterProjects_Status(List<Project> list, String status)
    {
        List<Project> projects = new ArrayList<>();
        for(Project project : list)
        { if(project.status.toLowerCase().compareTo(status.toLowerCase()) == 0) projects.add(project); }
        return projects;
    }

    public Task filterTasks_TaskID(List<Task> list, int taskID)
    {
        for (Task tsk : list)
            { if(tsk.taskID == taskID) return tsk; }
        return null;
    }

    public List<Task> filterTasks_DueDate(List<Task> list, String dueDate)
    {
        List<Task> tasks = new ArrayList<>();
        for(Task task : list)
            { if(task.dueDate.toLowerCase().compareTo(dueDate.toLowerCase()) == 0) tasks.add(task); }
        return tasks;
    }

    public List<Task> filterTasks_EmployeeID(List<Task> list, String employeeID)
    {
        //We need to talk about this one bfore going forward.
        return null;
    }

    public List<Task> filterTasks_ProjectID(List<Task> list, int projectID)
    {
        List<Task> tasks = new ArrayList<>();
        for(Task task : list)
            { if(task.projectID == projectID) tasks.add(task); }
        return tasks;
    }

    public List<Task> filterTasks_Status(List<Task> list, String status)
    {
        List<Task> tasks = new ArrayList<>();
        for(Task task : list)
            { if(task.status.toLowerCase().compareTo(status.toLowerCase()) == 0) tasks.add(task); }
        return tasks;
    }

    public List<Task> filterTasks_Size(List<Task> list, int size)
    {
        List<Task> tasks = new ArrayList<>();
        for(Task task : list)
            { if(task.size == size) tasks.add(task); }
        return tasks;
    }

    public WorkLog filterWorkLog_LogID(List<WorkLog> list, int logID)
    {
        for(WorkLog log : list)
            { if(log.logID == logID) return log; }
        return null;
    }

    public List<WorkLog> filterWorkLog_EntryType(List<WorkLog> list, String entryType)
    {
        List<WorkLog> workLogs = new ArrayList<>();
        for(WorkLog workLog : list)
            { if(workLog.entryType.toLowerCase().compareTo(entryType.toLowerCase()) == 0) workLogs.add(workLog); }
        return workLogs;
    }

    public List<WorkLog> filterWorkLog_TaskID(List<WorkLog> list, int taskID)
    {
        List<WorkLog> workLogs = new ArrayList<>();
        for(WorkLog workLog : list)
            { if(workLog.taskID == taskID) workLogs.add(workLog); }
        return workLogs;
    }

    public List<WorkLog> filterWorkLog_EmployeeID(List<WorkLog> list, int employeeID)
    {
        List<WorkLog> workLogs = new ArrayList<>();
        for(WorkLog workLog : list)
        { if(workLog.employeeID == employeeID) workLogs.add(workLog); }
        return workLogs;
    }

    public void closeConnection()
    {
        try {
            OS.con.close();
            OS.stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}