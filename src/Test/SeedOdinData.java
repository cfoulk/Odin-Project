package Test;
import Model.OdinModel;
import Server.Employee;
import Server.OdinServer;
import Server.Project;
import Server.Task;
import Server.WorkLog;

public class SeedOdinData {

    public static void main(String[] args) throws Exception {


        OdinModel model = new OdinModel();
        //see employee data
        model.addEmployee("John", "Manager", 1234, "JohnUser", "doe", "Active");
        model.addEmployee("Sarah", "Manager", 1234, "SarahJones", "Sarahpwd", "Active");
        model.addEmployee("Tim", "Project Lead", 1234, "TimSmith", "Timpwd", "Active");
        model.addEmployee("Test4", "Manager", 1234, "JohnUser", "doe", "Active");

    }




}
