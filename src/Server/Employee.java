package Server;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Employee
{
    public int employeeID;
    public int groupID;
    public String name;
    public String position;
    public String username;
    public String password;
    public String status;

    public Employee(ResultSet myRS) throws SQLException {
        this.employeeID = myRS.getInt("EmployeeID");
        this.groupID = myRS.getInt("GroupID");
        this.name = myRS.getString("Name");
        this.position = myRS.getString("Position");
        this.username = myRS.getString("Username");
        this.password = myRS.getString("Password");
        this.status = myRS.getString("Status");
    }

    public Employee(int employeeID, int groupID, String name, String position, String username, String password, String status) {
        this.employeeID = employeeID;
        this.groupID = groupID;
        this.name = name;
        this.position = position;
        this.username = username;
        this.password = password;
        this.status = status;
    }

    public boolean passwordCheck(String input) { return (this.password.compareTo(input) == 0); }
}
