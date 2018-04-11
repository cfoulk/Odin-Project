package Model;

import Server.*;

import java.util.ArrayList;
import java.util.List;

public class OdinModel implements OdinInterface
{
    OdinServer OS;

    public OdinModel() {
        OS = new OdinServer();
    }

    //Returns the employee's ID number on success.
    //Returns -1 if the username does not exist.
    //Returns -2 if the username exists, but the password is wrong.
    public int getUserID(String userName, String password) {
        Employee emp;
        try {
            emp = OS.getEmployee(userName);
        } catch (Exception e) {
            return -1;
        }
        if (emp.passwordCheck(password)) return emp.employeeID;
        return -2;
    }

    public boolean editEmployee(int employeeID, String name, String position, int groupID, String username, String password) {
        return false;
    }

    public boolean editProject(int projectID, String name, String dueDate, int groupID, int projectLeadID, String description, String status) {
        return false;
    }

    public boolean editTask(int taskID, String name, String dueDate, int employeeID, int projectID, String description, int size) {
        return false;
    }

    public boolean editWorkLog(int logID, String employeeID, String entryType, int taskID, String description) {
        return false;
    }

    public boolean addEmployee(String name, String position, int groupID, String username, String password)
    {
        try {
            try {
                OS.getEmployee(username);
            }
            catch(Exception e) {
                OS.addEmployee(name, position, groupID, username, password);
                return true;
            }
        } catch (Exception e) {
            System.err.println("Unable to add user");
            //Debug
            e.printStackTrace();
        }
        return false;
    }

    public boolean addProject(String name, String dueDate, int groupID, int projectLeadID, String description, String status) {
        return false;
    }

    public boolean addTask(String name, String dueDate, int employeeID, int projectID, String description, int size) {
        return false;
    }

    public boolean addWorkLog(String employeeID, String entryType, int taskID, String description) {
        return false;
    }


    public Employee getEmployee(int employeeID) {
        Employee ret;
        try {
            ret = OS.getEmployee(employeeID);
        } catch (Exception e) {
            return null;
        }
        return ret;
    }

    public List<Project> getProjects() throws Exception {
        List<Project> projects = OS.getProjects();
        return projects;
    }


    private void print() {
        //TODO shorter print statement
    }

    public void closeConnection() {
        try {
            OS.con.close();
            OS.stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}