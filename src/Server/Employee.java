package Server;

import java.sql.ResultSet;

public class Employee
{
    int employeeID;
    int groupID;
    String name;
    String position;
    String password;

    public Employee(ResultSet myRS) throws Exception
    {
        this.employeeID = myRS.getInt("EmployeeID");
        this.groupID = myRS.getInt("GroupID");
        this.name = myRS.getString("Name");
        this.position = myRS.getString("Position");
        this.password = myRS.getString("Password");
    }
}
