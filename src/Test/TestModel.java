package Test;
import Model.OdinModel;
import Server.Employee;
import Server.OdinServer;
import Server.Project;
import Server.Task;
import Server.WorkLog;

import javax.xml.transform.Result;

public class TestModel {

    public static void main(String[] args) throws Exception {

        String EMP_NAME = "John";

        //Testing employee operations - populate and read functions
        Employee employee;
        OdinModel model = new OdinModel();

        model.addEmployee(EMP_NAME, "Manager", 1234, "JohnUser", "doe", "Active");
        employee = model.getEmployee_Username(("JohnUser"));

        boolean isEmpUpdated = false;
        isEmpUpdated = model.editEmployee(employee.employeeID, employee.name, employee.position, employee.groupID, employee.username, "xyz", "Active");

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
        isTaskUpdated = model.editTask(5, "Another Test", "2018-05-12", "125", 3, "Changed description", 4, "Open");

        if(isTaskUpdated)
        {
            System.out.println("model.addTask test passed");
        }
        else
        {
            System.out.println("model.addTask test failed");
        }

        //Testing worklog operations - populate and read functions
        WorkLog worklog;
        //model.startWork(6, 12);
        //worklog = model.getWorkLog_LogID();
        //model.startWork(6, 12);
//        //worklog = model.getWorkLog_LogID();

    }

    //Please don't use this until you have populateTables finished.
    void emptyTables() throws Exception
    {
        OdinServer OS = new OdinServer();
        OS.stmt.executeUpdate(
                    "TRUNCATE TABLE employees;" +
                        "TRUNCATE TABLE projects;" +
                        "TRUNCATE TABLE tasks;" +
                        "TRUNCATE TABLE worklogs;" +
                        "TRUNCATE TABLE messges;");
    }

    void fillTables() throws Exception
    {
        OdinModel OM = new OdinModel();


    }
}

