package Test;
import Model.OdinModel;
import Server.Employee;
import java.io.PrintWriter;
import java.io.File;
import java.io.*;
import java.util.ArrayList;


public class TestModel {

    public static void main(String []args) throws Exception
    {
        //Testing employee operations
        Employee employee;

        OdinModel model = new OdinModel();
        model.addEmployee("John","Manager", 1234, "John", "doe");
        employee =  model.getEmployee_Username(("John"));

        if(model.editEmployee(employee.employeeID, employee.name, employee.position, employee.groupID, employee.username, "xyz")
        {
            System.out.println("model.addEmployee test passed")
        }

       try
       {
           PrintWriter printWriter = new PrintWriter("ModelServerTest");
           printWriter.print("");
           printWriter.close();

           FileWriter fileWriter = new FileWriter("ModelServerTest");

           BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

           ArrayList<String> elements = new ArrayList<>();



       }

       catch (IOException e)
       {
           System.out.println(e);
       }




    }


}
