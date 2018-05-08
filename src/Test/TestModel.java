package Test;
import Model.OdinModel;
import Server.Employee;
import Server.Project;
import Server.Task;
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


        //Testing project operations - populate and read functions
        Project project;
        model.addProject("TestProject", "2018-01-08", 3, 125, "testCode", "Open");
        project = model.getProject_ProjectID(1);

        boolean isProjUpdated = false;
        isProjUpdated = model.editProject(4, "TestProject", "2018-01-08", 3, 125, "Testing incomplete", "Open");

        if(isProjUpdated)
        {
            System.out.println("model.addProject test passed");
        }
        else
        {
            System.out.println("model.addProject test failed");
        }


        //Testing task operations - populate and read functions
        Task task;
        model.addTask("Another test", "2018-05-12", 3, "2", "and another one!", 3, "Open");
        task = model.getTask_TaskID(5);

        boolean isTaskUpdated = false;
        isTaskUpdated = model.editTask(5, "Another Test", "2018-05-12", "2", 3, "Changed description", 4, "Open");

        if(isTaskUpdated)
        {
            System.out.println("model.addTask test passed");
        }

    }
}

