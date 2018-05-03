package Test;
import Model.OdinModel;
import Server.Employee;
import javax.xml.transform.Result;

public class TestModel {

    public static void main(String[] args) throws Exception {

        //Testing employee operations
        Employee employee;
        OdinModel model = new OdinModel();
        model.addEmployee("John", "Manager", 1234, "John", "doe");
        employee = model.getEmployee_Username(("John"));

        if (model.editEmployee(employee.employeeID, employee.name, employee.position, employee.groupID, employee.username, "xyz"))
        {
            System.out.println("model.addEmployee test passed");
        }
        else {
            System.out.println("model.addEmployee test failed");
        }

    }
}

