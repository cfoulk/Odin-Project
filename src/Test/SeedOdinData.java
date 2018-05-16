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
        //user: employees
        model.addEmployee("John", "Employee", 1, "JohnUser", "doe", "Active");
        model.addEmployee("Sarah", "Employee", 1, "SarahJones", "Sarahpwd", "Active");
        model.addEmployee("Tim", "Employee", 2, "TimSmith", "Timpwd", "Active");
        model.addEmployee("Bob", "Employee", 2, "BobUser", "Bobpwd", "Active");
        model.addEmployee("Glen", "Employee", 3, "GlenUser", "Glenpwd", "Active");
        model.addEmployee("Joe", "Employee", 3, "JoeUser", "Joepwd", "Active");
        model.addEmployee("Chris", "Employee", 4, "ChrisUser", "Chrispwd", "Active");
        model.addEmployee("Julia", "Employee", 4, "JuliaUser", "Juliapwd", "Active");
        model.addEmployee("Hannah", "Employee", 5, "HannahUser", "Hannahpwd", "Active");
        model.addEmployee("Lauren", "Employee", 5, "LaurenUser", "Laurenpwd", "Active");

        //user: Project Lead
        model.addEmployee("Jane", "Project Lead", 1, "JaneUser", "Janepwd", "Active");
        model.addEmployee("Zoe", "Project Lead", 2, "ZoeUser", "Zoepwd", "Active");
        model.addEmployee("Simon", "Project Lead", 3, "SimonUser", "Simonpwd", "Active");
        model.addEmployee("George", "Project Lead", 4, "GeorgeUser", "Georgepwd", "Active");
        model.addEmployee("Max", "Project Lead", 5, "MaxUser", "Maxpwd", "Active");


        //user: ProjectLead
        model.addEmployee("Hannah", "Manager", 123, "HannahUser", "Hannahpwd", "Active");
        model.addEmployee("Alex", "Manager", 45, "AlexUser", "Alexpwd", "Active");









    }




}
