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
        //-----------------------------------------------------------------employee data-----------------------------------------------------
        //user: employees
        /*model.addEmployee("John", "Employee", 1, "JohnUser", "doe", "Active"); //1
        model.addEmployee("Sarah", "Employee", 1, "SarahJones", "Sarahpwd", "Active"); //2
        model.addEmployee("Tim", "Employee", 2, "TimSmith", "Timpwd", "Active"); //3
        model.addEmployee("Bob", "Employee", 2, "BobUser", "Bobpwd", "Active"); //4
        model.addEmployee("Glen", "Employee", 3, "GlenUser", "Glenpwd", "Active"); //5
        model.addEmployee("Joe", "Employee", 3, "JoeUser", "Joepwd", "Active"); //6
        model.addEmployee("Chris", "Employee", 4, "ChrisUser", "Chrispwd", "Active"); //7
        model.addEmployee("Julia", "Employee", 4, "JuliaUser", "Juliapwd", "Active"); //8
        model.addEmployee("Hannah", "Employee", 5, "HannahUser", "Hannahpwd", "Active"); //9
        model.addEmployee("Lauren", "Employee", 5, "LaurenUser", "Laurenpwd", "Active"); //10

        //user: Project Lead
        model.addEmployee("Jane", "Project Lead", 1, "JaneUser", "Janepwd", "Active"); //11
        model.addEmployee("Zoe", "Project Lead", 2, "ZoeUser", "Zoepwd", "Active"); //12
        model.addEmployee("Simon", "Project Lead", 3, "SimonUser", "Simonpwd", "Active"); //13
        model.addEmployee("George", "Project Lead", 4, "GeorgeUser", "Georgepwd", "Active"); //14
        model.addEmployee("Max", "Project Lead", 5, "MaxUser", "Maxpwd", "Active"); //15

        //user: Manager
        model.addEmployee("Sam", "Manager", 1, "SamUser", "Sampwd", "Active"); //16
        model.addEmployee("Alex", "Manager", 2, "AlexUser", "Alexpwd", "Active"); //17
        model.addEmployee("Jim", "Manager", 3, "JimUser", "Jimpwd", "Active"); //18
        model.addEmployee("Tom", "Manager", 4, "TomUser", "Tompwd", "Active"); //19
        model.addEmployee("Maddy", "Manager", 5, "MaddyUser", "Maddypwd", "Active"); */ //20



        //--------------------------------------------------------------project data--------------------------------------------------------------
        /*model.addProject("Time Management", "2018-02-05",1, 11, "Manage Time", "Open");
        model.addProject("Payroll Management", "2018-03-17",2, 12, "Payroll fix", "Closed");
        model.addProject("Student Counseling", "2018-01-22",3, 13, "counsel", "Open");
        model.addProject("Movie Management", "2018-07-13",4, 14, "manage movies", "Open");
        model.addProject("Library Management", "2018-12-31",5, 15, "library file fix", "Closed"); */


        //--------------------------------------------------------------task data-----------------------------------------------------------------
        //Project 1
        model.addTask("Fix Time","2018-02-09",1, "1, 2", "Time taken", 2, "Active");
        model.addTask("Find payroll","2018-07-12",1, "3, 4", "payroll calculation", 3, "Inactive");
        model.addTask("Check payroll","2018-08-01",1, "2, 3, 4", "compare to previous payroll", 1, "Active");

        //Project 2
        model.addTask("Organize Files","2018-04-11",2, "3, 5", "File Organization", 4, "Active");
        model.addTask("Update Files","2018-07-12",2, "1, 4", "File update", 3, "Active");
        model.addTask("Check Files","2018-07-23",2, "2, 5, 4", "compare to previous payroll", 1, "Active");

        //Project 3
        model.addTask("Computer fixture","2018-05-05",3, "1, 4", "File Organization", 3, "Inactive");
        model.addTask("Fix computer problems","2018-05-17",3, "2, 3", "File update", 5, "Active");
        model.addTask("Check computer problems.","2018-06-11",3, "1, 3, 4", "check if comp problems fixed", 1, "Active");









    }


}
