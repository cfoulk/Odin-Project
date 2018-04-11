package Server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class OdinServer
{
    final String FILENAME = "ServerInfo.txt";
    Connection con;
    Statement stmt;

    public OdinServer()
    {
        try
        {
            this.con = connect();
            this.stmt = con.createStatement();
        }
        catch(Exception e){ e.printStackTrace(); }
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

    public List<Employee> getEmployees() throws Exception
    {
        ResultSet myRS = this.stmt.executeQuery("SELECT * FROM employees;");
        List<Employee> employees = new ArrayList<>();
        while(myRS.next()) employees.add(new Employee(myRS));
        myRS.close();
        return employees;
    }

    public Employee getEmployee(int employeeID) throws Exception
    {
        Employee ret;
        ResultSet myRS = this.stmt.executeQuery("SELECT * FROM employees WHERE EmployeeID = " + employeeID + ";");
        ret = new Employee(myRS);
        this.stmt.close();
        myRS.close();
        return ret;
    }

    public Employee getEmployee(String username)
    {
        Employee ret;
        try {
            ResultSet myRS = this.stmt.executeQuery("SELECT * FROM employees WHERE Username = " + username + ";");
            ret = new Employee(myRS);
            this.stmt.close();
            myRS.close();
            return ret;
        }catch (Exception e){
            return null;
        }
    }

    public List<Project> getProjects() throws Exception
    {
        ResultSet myRS = this.stmt.executeQuery("SELECT * FROM projects;");
        List<Project> projects = new ArrayList<>();
        while(myRS.next()) projects.add(new Project(myRS));
        myRS.close();
        return projects;
    }

    //Requires a Project ID because we won't be needing the full list as far as I can tell.
    public List<Task> getTasks(int projectID) throws Exception
    {
        ResultSet myRS =
                this.stmt.executeQuery("SELECT * FROM tasks WHERE ProjectID = " + projectID + ";");
        List<Task> tasks = new ArrayList<>();
        while(myRS.next()) tasks.add(new Task(myRS));
        myRS.close();
        return tasks;
    }

    //This is for the specific employee's worklog.
    public List<WorkLog> getWorkLog(int employeeID) throws Exception
    {
        ResultSet myRS =
                this.stmt.executeQuery("SELECT * FROM worklog WHERE EmployeeID = " + employeeID + ";");
        List<WorkLog> workLogs = new ArrayList<>();
        while(myRS.next()) workLogs.add(new WorkLog(myRS));
        myRS.close();
        return workLogs;
    }

    public void addEmployee(String name, String position, String password, int groupID, int employeeID, String username) throws Exception
    {
        this.stmt.executeQuery("INSERT INTO employees (" +
                    "Name, Position, Password, GroupID, EmployeeID, Username)" +
                ") VALUES(" + name + ", " + position + ", " + password + ", " + groupID + ", " + employeeID + ", " + username + ");");
        this.stmt.close();

    }

    public void addProject(String name, String dueDate, int groupID, int projectLeadID,
                    String description, String status, int projectID) throws Exception
    {
        this.stmt.executeQuery("INSERT INTO projects (" +
                "Name, DueDate, GroupID, ProjectLeadID, Description, Status, ProjectID" +
                ") VALUES (" +
                name + " " + groupID + " " + projectID + " " + description + " " + status + " " + projectID + ");");
        this.stmt.close();
    }

    public void addTasks(String dueDate, int employeeID, int projectID, String description, int size, String name, int taskID) throws Exception
    {
        this.stmt.executeQuery("INSERT INTO tasks (" +
                "Name, DueDate, ProjectID, EmployeeID, Description, Size, TaskID" +
            ") VALUES (" +
                dueDate + " " + employeeID + " " + projectID + " " + description + " " + size + " " + name + " " + taskID + "):");
        this.stmt.close();
    }

    public void addWorkLog(String employeeID, String entryType, int taskID, String description, int logID) throws Exception
    {
        this.stmt.executeQuery("INSERT INTO WorkLog (" +
            "EntryType, EmployeeID, TaskID, Description, LogID" +
        ") VALUES (" +
                employeeID + " " + entryType + " " + taskID + " " + description + " " + logID + "):");
        this.stmt.close();
    }


///edit function
    public void editEmployee (String name, String position, String password, int groupID, int employeeID, String username) throws Exception
    {
        this.stmt.executeQuery(  "UPDATE employees SET " +
                "Name = \"" + name + "\", " +
                "Position = \" " + position + "\", " +
                "Password = \"" + password + "\", " +
                "GroupID =  " + groupID  + ", " +
                "EmployeeID = " + employeeID + ", " +
                "Username =  \""  + username + "\" " +
                "WHERE EmployeeID = " + employeeID + ";" );
        this.stmt.close();
    }

    public  void editProject (String name, String dueDate, int groupID, int projectLeadID,
                              String description, String status, int projectID) throws Exception
    {
        this.stmt.executeQuery ( "UPDATE projects SET " +
                "Name = \"" + name + "\", " +
                "DueDate = \"" + dueDate + "\", " +
                "GroupID = " + groupID + ", " +
                "ProjectLeadID = " + projectLeadID + ", " +
                "Description = \"" + description + "\", " +
                "Status = \"" + status + "\", " +
                "ProjectID = " + projectID + " " +
                "WHERE ProjectID = " + projectID + ";");
        this.stmt.close();
    }

    public void  editTasks (String dueDate, int employeeID, int projectID, String description, int size, String name, int taskID) throws Exception
    {
        this.stmt.executeQuery("Update tasks SET " +
                "Duedate = \"" + dueDate + "\", " +
                "Description = \"" + description + "\", " +
                "Size = " + size + ", " +
                "Name = \"" + name + "\", " +
                "TaskID = " + taskID + ", " +
                "EmployeeID = " + employeeID + ", " +
                "ProjectID = " + projectID + " " +
                "WHERE TaskID = " + taskID + ";");
        this.stmt.close();
    }

    public void editWorkLog (String employeeID, String entryType, int taskID, String description, int logID) throws Exception
    {
        this.stmt.executeQuery("UPDATE work log SET " +
                "EmployeeID = \"" + employeeID + "\", " +
                "EntryType = \"" + entryType + "\", " +
                "TaskID = " + taskID + ", " +
                "Description = \"" + description + "\", " +
                "LogID = " + logID + " " +
                "WHERE LogID = " + logID + ";" );
        this.stmt.close();
    }
}