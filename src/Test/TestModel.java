package Test;
import Model.OdinModel;
import Server.Employee;
import javax.xml.transform.Result;

public class TestModel {

    public static void main(String[] args) throws Exception {

        String EMP_NAME = "John";

        //Testing employee operations - populate and read functions
        Employee employee;
        OdinModel model = new OdinModel();
        model.addEmployee(EMP_NAME, "Manager", 1234, "JohnUser", "doe");
        employee = model.getEmployee_Username(("JohnUser"));

        boolean isEmpUpdated = false;
        isEmpUpdated = model.editEmployee(employee.employeeID, employee.name, employee.position, employee.groupID, employee.username, "xyz");

        if (isEmpUpdated)
        {
            System.out.println("model.addEmployee test passed");
        }
        else
        {
            System.out.println("model.addEmployee test failed");
        }

        //Testing employee - delete function
        employee = model.getEmployee_Username(EMP_NAME);
        if (employee != null)
        {
            model.deleteEmployee_EmployeeID(employee.employeeID);
            System.out.println("deleteEmployee_EmployeeID method passed");
        }



    }
}

