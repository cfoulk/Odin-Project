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

    OdinServer()
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

    List<Project> getProjects() throws Exception
    {
        ResultSet myRS = this.stmt.executeQuery("SELECT * FROM projects;");
        List<Project> projects = new ArrayList<>();
        while(myRS.next()) projects.add(new Project(myRS));
        myRS.close();
        return projects;
    }

    List<Employee> getEmployees() throws Exception
    {
        ResultSet myRS = this.stmt.executeQuery("SELECT * FROM employees;");
        List<Employee> employees = new ArrayList<>();
        while(myRS.next()) employees.add(new Employee(myRS));
        myRS.close();
        return employees;
    }

    void addEmployees (String name, String position, String password, int groupID, int employeeID, String username) throws Exception
    {
        this.stmt.executeQuery(SQL "INSERT INTO employees (" +
                    "Name, Position, Password, GroupID, EmployeeID, Username)" +
                ") VALUES(" + name + " " + position + " " password + " " groupID + " "employeeID + " " + username + ");");
            this.stmt.close();

    }

    void addProject(String name, String dueDate, int groupID, int projectLeadID,
                    String description, String status, int projectID) throws Exception
    {
        this.stmt.executeQuery("INSERT INTO projects (" +
                "Name, DueDate, GroupID, ProjectLeadID, Description, Status, ProjectID" +
                ") VALUES (" +
                name + " " + groupID + " " + projectID + " " + description + " " + status + " " + projectID + ");");
        this.stmt.close();
    }
}