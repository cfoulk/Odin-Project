package Model;

import Server.*;
import java.sql.*;
import java.util.*;

public class OdinModel
{
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
		try
		{
			myConn = OdinServer.connect();
			myStmt = myConn.createStatement();
			employees = getEmployees(myStmt);
			employees.forEach(p->System.out.println(p.employeeLine()));
			myConn.close();
			myStmt.close();
		}catch (Exception e){ e.printStackTrace(); } 
	}

	public int getUserID(String userName, String password){
		//TODO Joel, login
		return -1;
	}

	public Employee getEmployee(){
		//TODO
		return null;
	}
}