package Server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class OdinServer
{
    final String FILENAME = "ServerInfo.txt";
    public Connection con;
    public Statement stmt;

    public OdinServer() throws IOException, SQLException
    {
        this.con = connect();
        this.stmt = con.createStatement();

    }

    public Connection connect() throws SQLException, IOException
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

    //Edit methods
    public void editEmployee (int employeeID, String name, String position, int groupID, String username, String password) throws Exception
    {
        this.stmt.executeUpdate(  "UPDATE employees SET " +
                "Name = '" + name + "', " +
                "Position = '" + position + "', " +
                "GroupID = " + groupID  + ", " +
                "Username = '"  + username + "', " +
                "Password = '" + password + "' " +
                "WHERE EmployeeID = " + employeeID + ";");
    }

    public void editProject (int projectID, String name, String dueDate, int groupID, int projectLeadID, String description, String status) throws Exception
    {
        this.stmt.executeUpdate ( "UPDATE projects SET " +
                "Name = '" + name + "', " +
                "DueDate = " + dueDate + "', " +
                "GroupID = " + groupID + ", " +
                "ProjectLeadID = " + projectLeadID + ", " +
                "Description = '" + description + "', " +
                "Status = '" + status + "' " +
                "WHERE ProjectID = " + projectID + ";");
    }

    public void editTask (int taskID, String name, String dueDate, int projectID, String employees, String description, int size, String status) throws Exception
    {
        this.stmt.executeUpdate("UPDATE tasks SET " +
                "Name = '" + name + "', " +
                "DueDate = '" + dueDate + "', " +
                "ProjectID = " + projectID + ", " +
                "Employees = '" + employees + "', " +
                "Description = '" + description + "', " +
                "Size = " + size + ", " +
                "Status = '" + status + "' " +
                "WHERE TaskID = " + taskID + ";");
    }

    public void editWorkLog (int logID, int taskID, int employeeID, String elapsedTime, String startTime, String stopTime, String description) throws Exception
    {
        this.stmt.executeUpdate("UPDATE worklogs SET " +
                "TaskID = " + taskID + ", " +
                "EmployeeID = " + employeeID + ", " +
                "ElapsedTime = '" + elapsedTime + "', " +
                "StartTime = '" + startTime + "', " +
                "StopTime = '" + stopTime + "', " +
                "Description = '" + description + "' " +
                "WHERE LogID = " + logID + ";" );
    }

    public void setMessage_Read (int MessageID, String message, int recipientID, int senderID) throws Exception
    {
        //this probably is going to need to be worked on, i don't know where to take it
        String status = "read";
        this.stmt.executeUpdate(  "UPDATE messages SET " +
                "MessageID = '" + MessageID + "', " +
                "Message = '" + message + "', " +
                "Status = " + status  + ", " +
                "RecipientID = " + recipientID  + ", " +
                "SenderID = '"  + senderID + ";");
    }

    //Add methods
    public void addEmployee(String name, String position, int groupID, String username, String password) throws Exception
    {
        this.stmt.executeUpdate("INSERT INTO employees (Name, Position, GroupID, Username, Password) " +
                "VALUES ('" + name + "', '" + position + "', " + groupID + ", '" + username + "', '" + password + "');");
    }

    public void addProject(String name, Date dueDate, int groupID, int projectLeadID, String description, String status) throws Exception
    {
        this.stmt.executeUpdate("INSERT INTO projects (Name, DueDate, GroupID, ProjectLeadID, Description, Status) " +
                "VALUES ('" + name + "', '" + dueDate + "', " + groupID + ", " + projectLeadID + ", '" + description + "', '" + status + "');");
    }

    public void addTask(String name, String dueDate, int projectID, String employees, String description, int size, String status) throws Exception
    {
        this.stmt.executeUpdate("INSERT INTO tasks (Name, DueDate, ProjectID, Employees, Description, Size, Status) " +
                "VALUES ('" + name + "', '" + dueDate + "', " + projectID + ", '" + employees + "', '" + description + "', " + size + ", '" + status + "');");
    }

    public void addWorkLog(int taskID, int employeeID, String elapsedTime, String startTime, String stopTime, String description) throws Exception
    {
        this.stmt.executeUpdate("INSERT INTO worklogs (TaskID, EmployeeID, ElapsedTime, StartTime, StopTime, Description) " +
                "VALUES (" + taskID + ", " + employeeID + ", '" + elapsedTime + "', '" + startTime + "', '" + stopTime + "', '" + description + "');");
    }

    public void startWorkLog(int taskID, int employeeID, String startTime) throws Exception
    {
        this.stmt.executeUpdate("INSERT INTO worklogs (TaskID, EmployeeID, StartTime)" +
                "VALUES (" + taskID + ", " + employeeID + ", '" + startTime + "');");
    }

    public void addMessage(String message, int recipientID, int senderID) throws Exception
    {
        this.stmt.executeUpdate("INSERT INTO messages (message, recipientID, senderID) " +
                "VALUES ('" + message + "', " + recipientID + ", " + senderID + ");");
    }

    //Deletes
    public void deleteEmployee_EmployeeID(int employeeID) throws Exception //might need some tweaking based on how tasks store employees
    {
        this.stmt.executeUpdate("DELETE FROM employees WHERE employeeID = " + employeeID + ";");
        this.stmt.executeUpdate("UPDATE tasks SET Employees = REPLACE(Employees, '" + employeeID + "', '') WHERE Employees LIKE '%" + employeeID + ",%';");

    }

    public void deleteTask_TaskID(int taskID) throws Exception
    {
        this.stmt.executeUpdate("DELETE FROM tasks WHERE taskID = " + taskID + ";");
    }

    public void deleteMessage_MessageID(int messageID) throws Exception
    {
        this.stmt.executeUpdate("DELETE FROM messages WHERE messageID = " + messageID + ";");
    }

    public void deleteMessages_RecipientID(int recipientID) throws Exception
    {
        this.stmt.executeUpdate("DELETE FROM messages WHERE recipientID = " + recipientID + ";");
    }

    public void deleteMessages_SenderID(int senderID) throws Exception
    {
        this.stmt.executeUpdate("DELETE FROM messages WHERE senderID = " + senderID + ";");
    }

    //Get Singles
    public Employee getEmployee_EmployeeID(int employeeID) throws SQLException
    {
        Employee employee;
        ResultSet myRS = this.stmt.executeQuery("SELECT * FROM employees WHERE EmployeeID = " + employeeID + ";");
        if(myRS.next())
        {
            employee = new Employee(myRS);
            myRS.close();
            return employee;
        }
        return null;
    }

    public Employee getEmployee_Username(String username) throws SQLException
    {
        Employee employee;
        ResultSet myRS = this.stmt.executeQuery("SELECT * FROM employees WHERE Username = '" + username + "';");
        if(myRS.next())
        {
            employee = new Employee(myRS);
            myRS.close();
            return employee;
        }
        return null;
    }

    public Project getProject_ProjectID(int projectID) throws Exception
    {
        Project project;
        ResultSet myRS = this.stmt.executeQuery("SELECT * FROM projects WHERE ProjectID = " + projectID + ";");
        if(myRS.next())
        {
            project = new Project(myRS);
            myRS.close();
            return project;
        }
        return null;
    }

    public Task getTask_TaskID(int taskID) throws Exception
    {
        Task task;
        ResultSet myRS = this.stmt.executeQuery("SELECT * FROM tasks WHERE TaskID = " + taskID + ";");
        if(myRS.next())
        {
            task = new Task(myRS);
            myRS.close();
            return task;
        }
        return null;
    }

    public WorkLog getWorkLog_LogID(int workLogID) throws Exception
    {
        WorkLog workLog;
        ResultSet myRS = this.stmt.executeQuery("SELECT * FROM worklogs WHERE LogID = " + workLogID + ";");
        if(myRS.next())
        {
            workLog = new WorkLog(myRS);
            myRS.close();
            return workLog;
        }
        return null;
    }

    public Message getMessage_MessageID(int messageID) throws Exception
    {
        Message message;
        ResultSet myRS = this.stmt.executeQuery("SELECT * FROM messages WHERE messageID = " + messageID + ";");
        if(myRS.next())
        {
            message = new Message(myRS);
            myRS.close();
            return message;
        }
        return null;
    }

    //Get Sets
    public List<Employee> getEmployees() throws SQLException
    {
        List<Employee> employees = new ArrayList<>();
        ResultSet myRS = this.stmt.executeQuery("SELECT * FROM employees;");
        while(myRS.next()) employees.add(new Employee(myRS));
        myRS.close();
        return employees;
    }

    public List<Employee> getEmployees_GroupID(int groupID) throws Exception
    {
        List<Employee> employees = new ArrayList<>();
        ResultSet myRS = this.stmt.executeQuery("SELECT * FROM employees WHERE GroupID = " + groupID + ";");
        while(myRS.next()) employees.add(new Employee(myRS));
        myRS.close();
        return employees;
    }

    public List<Project> getProjects() throws Exception
    {
        List<Project> projects = new ArrayList<>();
        ResultSet myRS = this.stmt.executeQuery("SELECT * FROM projects;");
        while(myRS.next()) projects.add(new Project(myRS));
        myRS.close();
        return projects;
    }

    public List<Project> getProject_GroupID(int groupID) throws Exception
    {
        List<Project> projects = new ArrayList<>();
        ResultSet myRS = this.stmt.executeQuery("SELECT * FROM projects WHERE GroupID = " + groupID + ";");
        while(myRS.next()) projects.add(new Project(myRS));
        myRS.close();
        return projects;
    }

    public List<Project> getProject_ProjectLeadID(int projectLeadID) throws Exception
    {
        List<Project> projects = new ArrayList<>();
        ResultSet myRS = this.stmt.executeQuery("SELECT * FROM projects WHERE ProjectLeadID = " + projectLeadID + ";");
        while(myRS.next()) projects.add(new Project(myRS));
        myRS.close();
        return projects;
    }

    public List<Project> getProjects_Status(String status) throws Exception
    {
        List<Project> projects = new ArrayList<>();
        ResultSet myRS = this.stmt.executeQuery("SELECT * FROM projects WHERE status = " + status + ";");
        while (myRS.next()) projects.add(new Project(myRS));
        myRS.close();
        return projects;
    }

    public List<Task> getTasks() throws Exception
    {
        List<Task> tasks = new ArrayList<>();
        ResultSet myRS = this.stmt.executeQuery("SELECT * FROM tasks;");
        while(myRS.next()) tasks.add(new Task(myRS));
        myRS.close();
        return tasks;
    }

    public List<Task> getTasks_ProjectID(int projectID) throws Exception
    {
        List<Task> tasks = new ArrayList<>();
        ResultSet myRS = this.stmt.executeQuery("SELECT * FROM tasks WHERE ProjectID = " + projectID + ";");
        while(myRS.next()) tasks.add(new Task(myRS));
        myRS.close();
        return tasks;
    }

    public List<Task> getTasks_EmployeeID(int employeeID) throws Exception
    {
        List<Task> tasks = new ArrayList<>();
        ResultSet myRS = this.stmt.executeQuery("SELECT * FROM tasks WHERE EmployeeID = " + employeeID + ";");
        while(myRS.next()) tasks.add(new Task(myRS));
        myRS.close();
        return tasks;
    }

    public List<WorkLog> getWorkLogs() throws Exception
    {
        List<WorkLog> workLogs = new ArrayList<>();
        ResultSet myRS = this.stmt.executeQuery("SELECT * FROM worklogs;");
        while(myRS.next()) workLogs.add(new WorkLog(myRS));
        myRS.close();
        return workLogs;
    }

    public List<WorkLog> getWorkLogs_EmployeeID(int employeeID) throws Exception
    {
        List<WorkLog> workLogs = new ArrayList<>();
        ResultSet myRS = this.stmt.executeQuery("SELECT * FROM worklog WHERE EmployeeID = " + employeeID + ";");
        while(myRS.next()) workLogs.add(new WorkLog(myRS));
        myRS.close();
        return workLogs;
    }

    public List<WorkLog> getWorkLogs_TaskID(int taskID) throws Exception
    {
        List<WorkLog> workLogs = new ArrayList<>();
        ResultSet myRS = this.stmt.executeQuery("SELECT * FROM worklog WHERE TaskID = " + taskID + ";");
        while(myRS.next()) workLogs.add(new WorkLog(myRS));
        myRS.close();
        return workLogs;
    }

    public List<Message> getMessages() throws Exception
    {
        List<Message> messages = new ArrayList<>();
        ResultSet myRS = this.stmt.executeQuery("SELECT * FROM messages;");
        while(myRS.next()) messages.add(new Message(myRS));
        myRS.close();
        return messages;
    }

    public List<Message> getMessages_RecipientID(int recipientID) throws Exception
    {
        List<Message> messages = new ArrayList<>();
        ResultSet myRS = this.stmt.executeQuery("SELECT * FROM messages WHERE recipientID = " + recipientID + ";");
        while(myRS.next()) messages.add(new Message(myRS));
        myRS.close();
        return messages;
    }

    public List<Message> getMessages_SenderID(int senderID) throws Exception
    {
        List<Message> messages = new ArrayList<>();
        ResultSet myRS = this.stmt.executeQuery("SELECT * FROM messages WHERE senderID = " + senderID + ";");
        while(myRS.next()) messages.add(new Message(myRS));
        myRS.close();
        return messages;
    }
}