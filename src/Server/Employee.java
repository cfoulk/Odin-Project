package Server;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Employee
{
    public int employeeID;
    public int groupID;
    public String name;
    public String position;
    String password;
    String status;

    public Employee(ResultSet myRS) throws SQLException {
        this.employeeID = myRS.getInt("EmployeeID");
        this.groupID = myRS.getInt("GroupID");
        this.name = myRS.getString("Name");
        this.position = myRS.getString("Position");
        this.password = myRS.getString("Password");
        //this.status = myRS.getString("Status");
    }

    public boolean passwordCheck(String input) { return (this.password.compareTo(input) == 0); }
}
