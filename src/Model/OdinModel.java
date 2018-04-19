package Model;

import Server.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class OdinModel implements OdinInterface
{
    OdinServer OS;

    public OdinModel() throws IOException, SQLException {
        OS = new OdinServer();
    }

    //Returns the employee's ID number on success.
    //Returns -1 if the username does not exist.
    //Returns -2 if the username exists, but the password is wrong.
    public int getUserID(String userName, String password) {
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

    public boolean addWorkLog(int employeeID, String entryType, int taskID, String description) {
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

    public Employee getEmployee(int employeeID) {
        Employee ret;
        try { ret = OS.getEmployee(employeeID); }
        catch (Exception e) { return null; }
        return ret;
    }

    public Project getProject(int projectID) {
        Project ret;
        try { ret = OS.getProject_ProjectID(projectID); }
        catch (Exception e) { return null; }
        return ret;
    }

    public Task getTask(int taskID) {
        Task ret;
        try { ret = OS.getTask(taskID); }
        catch (Exception e) { return null; }
        return ret;
    }

    public WorkLog getWorkLog(int logID) {
        WorkLog ret;
        try { ret = OS.getWorkLog(logID); }
        catch (Exception e) { return null; }
        return ret;
    }

    public List<Employee> getEmployees() {
        List<Employee> ret;
        try { ret = OS.getEmployees(); }
        catch (Exception e) { return null; }
        return ret;
    }

    public List<Employee> getEmployees_GroupID(int groupID) {
        List<Employee> ret;
        try { ret = OS.getEmployees_GroupID(groupID); }
        catch (Exception e) { return null; }
        return ret;
    }

    public List<Project> getProjects() {
        List<Project> ret;
        try { ret = OS.getProjects(); }
        catch(Exception e) { return null; }
        return ret;
    }

    public List<Project> getProjects_GroupID(int groupID) {
        List<Project> ret;
        try { ret = OS.getProject_GroupID(groupID); }
        catch(Exception e) { return null;}
        return ret;
    }

    public List<Project> getProjects_ProjectLeadID(int projectLeadID) {
        List<Project> ret;
        try { ret = OS.getProject_ProjectLeadID(projectLeadID); }
        catch(Exception e) { return null; }
        return ret;
    }

    public List<Project> getProjects_Status(String status) {
        List<Project> ret;
        try { ret = OS.getProjects(); } //place holder, need to implement correct method into Odinserver
        catch (Exception e) { return null; }
        return ret;
    }

    public List<Task> getTasks() {
        return null;
    }

    public List<Task> getTasks_ProjectID(int projectID) {
        List<Task> ret;
        try { ret = OS.getTasks_ProjectID(projectID); }
        catch (Exception e) { return null; }
        return ret;
    }

    public List<Task> getTasks_EmployeeID(int employeeID) {
        List<Task> ret;
        try { ret = OS.getTasks_EmployeeID(employeeID); }
        catch (Exception e) { return null; }
        return ret;
    }

    public List<WorkLog> getWorkLogs() {
        List<WorkLog> ret;
        try { ret = OS.getWorkLogs(); }
        catch (Exception e) { return null; }
        return ret;
    }

    public List<WorkLog> getWorkLogs_EmployeeID(int employeeID) {
        List<WorkLog> ret;
        try { ret = OS.getWorkLogs_EmployeeID(employeeID); }
        catch (Exception e) { return null;}
        return ret;
    }

    public List<WorkLog> getWorkLogs_TaskID(int taskID) { return null; //this was all me
    }

    private void print() {
        //TODO shorter print statement
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