package Server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return employeeID == employee.employeeID &&
                groupID == employee.groupID &&
                Objects.equals(name, employee.name) &&
                Objects.equals(position, employee.position) &&
                Objects.equals(username, employee.username) &&
                Objects.equals(password, employee.password) &&
                Objects.equals(status, employee.status);
    }

    @Override
    public int hashCode() {

        return Objects.hash(employeeID, groupID, name, position, username, password, status);
    }
}
