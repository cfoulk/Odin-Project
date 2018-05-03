package App.gui;

import Server.Project;

import java.util.ArrayList;
import java.util.List;

public class persistentUser {

    public static ArrayList<Project> projectList = new ArrayList<>();

    public static void initiateSampleData(){
        Project user1 = new Project(33,22,131,"Finish Dashboard", "Testing class", "In-Progress", "5/11/2018");
        Project user2 = new Project(33,22,131,"Show Headers", "Testing class", "In-Progress", "5/4/2018");
        projectList.add(user1);
        projectList.add(user2);
    }
}
