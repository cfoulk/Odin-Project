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
        model.addEmployee("John", "Employee", 1234, "JohnUser", "doe", "Active");
        model.addEmployee("Sarah", "Employee", 1234, "SarahJones", "Sarahpwd", "Active");
        model.addEmployee("Tim", "Employee", 1234, "TimSmith", "Timpwd", "Active");
        model.addEmployee("Bob", "Employee", 1234, "BobUser", "Bobpwd", "Active");
        model.addEmployee("Glen", "Employee", 1234, "GlenUser", "Glenpwd", "Active");
        model.addEmployee("Joe", "Employee", 1234, "JoeUser", "Joepwd", "Active");
        model.addEmployee("Chris", "Employee", 1234, "ChrisUser", "Chrispwd", "Active");
        model.addEmployee("Julia", "Employee", 1234, "JuliaUser", "Juliapwd", "Active");
        model.addEmployee("Hannah", "Employee", 1234, "HannahUser", "Hannahpwd", "Active");
        model.addEmployee("Lauren", "Employee", 1234, "LaurenUser", "Laurenpwd", "Active");







    }




}
