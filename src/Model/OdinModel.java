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

	public boolean addEmployee(String name, String position, String password, int groupID, int employeeID, String userName)
    {
        Employee emp;
        try {emp = OS.getEmployee(userName);
            OS.addEmployee(name, position, password, groupID,employeeID, userName);
            return true;
        }
        catch (Exception e){
            System.err.println("Unable to add user");
            e.printStackTrace();
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

    private void print(){
	    //TODO shorter print statement
    }
}