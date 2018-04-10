package Model;

import Server.*;

public class OdinModel
{
    //Returns the employee's ID number on success.
    //Returns -1 if the username does not exist.
    //Returns -2 if the username exists, but the password is wrong.
    int loginRequest(String username, String password)
    {
        OdinServer OS = new OdinServer();
        Employee emp = null;
        try{ emp = OS.getEmployee(username); }
        catch(Exception e) { e.printStackTrace(); }
        if(emp == null) return -1;
        if(emp.passwordCheck(password)) return emp.employeeID;
        return -2;
    }
	/*
	public static void main(String[] args) throws Exception
	{
		OdinModel OM = new OdinModel();
		OM.appMain(args);
	}
	
	public void appMain(String[] args)
	{
		Connection myConn = null;
		Statement myStmt = null;
		List<Employee> employees = new ArrayList<Employee>();
		OdinServer OS = new OdinServer();
		try
		{
			myConn = OS.connect();
			myStmt = myConn.createStatement();
			employees = getEmployees(myStmt);
			employees.forEach(p->System.out.println(p.employeeLine()));
			myConn.close();
			myStmt.close();
		}catch (Exception e){ e.printStackTrace(); } 
	}
	*/

	public int getUserID(String userName, String password){
		//TODO Joel, login
        OdinServer OS = new OdinServer();
        Employee emp = null;
        try{ emp = OS.getEmployee(userName); }
        catch(Exception e) { e.printStackTrace(); }
        if(emp == null) return -1;
        if(emp.passwordCheck(password)) return emp.employeeID;
        return -2;
	}

	public Employee getEmployee(){
		//TODO
		return null;
	}
}