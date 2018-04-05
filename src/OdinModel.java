import java.io.*;
import java.sql.*;
import java.util.*;

public class OdinModel
{
	//I'm being told that this needs to be static, anyone know why?
	final String FILENAME = "ServerInfo.txt";
	
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
			myConn = connect();
			myStmt = myConn.createStatement();
			employees = getEmployees(myStmt);
			employees.forEach(p->System.out.println(p.employeeLine()));
			myConn.close();
			myStmt.close();
		}catch (Exception e){ e.printStackTrace(); } 
	}
	
	public Connection connect() throws Exception
	{
		StringTokenizer stk;
		Connection con;
		BufferedReader reader;
		reader = new BufferedReader(new FileReader(FILENAME));
		stk = new StringTokenizer(reader.readLine(), "\t");
		con = DriverManager.getConnection(stk.nextToken(), stk.nextToken(), stk.nextToken());
		reader.close();
		return con;
	}
	
	List<Employee> getEmployees(Statement myStmt) throws Exception
	{
		ResultSet myRS = myStmt.executeQuery("SELECT * FROM employees;");
		List<Employee> employees = new ArrayList<Employee>();
		while(myRS.next()) employees.add(new Employee(myRS));
		myRS.close();
		return employees;
	}
	
	List<Project> getProjects(Statement myStmt) throws Exception
	{
		ResultSet myRS = myStmt.executeQuery("SELECT * FROM projects;");
		List<Project> projects = new ArrayList<Project>();
		while(myRS.next()) projects.add(new Project(myRS));
		myRS.close();
		return projects;
	}
}

class Employee
{
	int employeeID;
	int groupID;
	String name;
	String position;
	String password;
	
	Employee(ResultSet myRS) throws Exception
	{
		this.employeeID = myRS.getInt("EmployeeID");
		this.groupID = myRS.getInt("GroupID");
		this.name = myRS.getString("Name");
		this.position = myRS.getString("Position");
		this.password = myRS.getString("Password");
	}
	
	String employeeLine()
	{
		return (this.employeeID + "\t" + this.name + "\t" + this.position + "\t" + this.groupID);
	}
}

class Project
{
	int projectID;
	int groupID;
	int projectLeadID;
	String name;
	String description;
	String status;
	String dueDate;
	
	Project(ResultSet myRS) throws Exception
	{
		this.projectID = myRS.getInt("ProjectID");
		this.groupID = myRS.getInt("GroupID");
		this.projectLeadID = myRS.getInt("ProjectLeadID");
		this.name = myRS.getString("Name");
		this.description = myRS.getString("Description");
		this.status = myRS.getString("Status");
		this.dueDate = myRS.getString("DueDate");
	}
	
	String projectLine()
	{
		return (this.projectID + "\t" + this.name + "\t" + this.status + "\t" +
				this.groupID + "\t" + this.projectLeadID + "\t" + this.dueDate + "\t" +
				this.description);
	}
}

class Task
{
	
}

class WorkLog
{
		int employeeID;
		int taskID;
		int logID;
		String entryType;
		String description;

		WorkLog(ResultSet myRS) throws Exception
		{
			this.employeeID = myRS.getInt("EmployeeID");
			this.taskID = myRS.getInt("TaskID");
			this.logID = myRS.getInt("LogID");
			this.entryType = myRS.getString("EntryID");
			this.description = myRS.getString("Description");
		}


}