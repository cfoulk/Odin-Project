package Server;

import java.sql.ResultSet;

public class Employee
{
    public int employeeID;
    public int groupID;
    public String name;
    public String position;
    String password;

    public Employee(ResultSet myRS) throws Exception
    {
        this.employeeID = myRS.getInt("EmployeeID");
        this.groupID = myRS.getInt("GroupID");
        this.name = myRS.getString("Name");
        this.position = myRS.getString("Position");
        this.password = myRS.getString("Password");
    }

    public boolean passwordCheck(String input) { return (this.password.compareTo(input) == 0); }
}
