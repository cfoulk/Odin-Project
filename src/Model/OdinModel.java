package Model;

import Server.*;

import java.util.ArrayList;
import java.util.List;

public class OdinModel
{
    OdinServer OS;

    public OdinModel()
    {
        OS = new OdinServer();
    }

    //Returns the employee's ID number on success.
    //Returns -1 if the username does not exist.
    //Returns -2 if the username exists, but the password is wrong.
	public int getUserID(String userName, String password){
        Employee emp;
        try{ emp = OS.getEmployee(userName); }
        catch(Exception e) { return -1; }
        if(emp.passwordCheck(password)) return emp.employeeID;
        return -2;
	}

	public boolean addEmployee(String name, String position, String password, int groupID, int employeeID, String userName) throws Exception
    {
        Employee emp;
        try {emp = OS.getEmployee(userName);}
        catch (Exception e)
        {
            OS.addEmployee(name, position, password, groupID,employeeID, userName);
            return true;
        }
        return false;
    }


	public Employee getEmployee(int employeeID)
    {
		Employee ret;
		try
        {
		    ret = OS.getEmployee(employeeID);
        }
        catch(Exception e)
        {
            return null;
        }
        return ret;
	}

	public List<Project> getProjects() throws Exception
    {
        List<Project>  projects =  OS.getProjects();
        return projects;
    }

    public void closeConnection()
    {
        try
        {
            OS.con.close();
            OS.stmt.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}