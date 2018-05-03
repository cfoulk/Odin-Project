package App.gui;

import Server.*;
import Model.OdinModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class persistentUser {

    public static Employee currentUser;
    public static List<Project> projectList = new ArrayList<>();
    public static List<Task> taskList;
    public static List<WorkLog> workLogList;
    public static List<Message> messageList;

    public static void initiateSampleData(){
        Project user1 = new Project(33,22,131,"Finish Dashboard", "Testing class", "In-Progress", "5/11/2018");
        Project user2 = new Project(33,22,131,"Show Headers", "Testing class", "In-Progress", "5/4/2018");
        projectList.add(user1);
        projectList.add(user2);
    }

    public static void getSessionData(OdinModel OM, String username)
    {
        List<Task> holdTasks;
        currentUser = OM.getEmployee_Username(username);
        projectList = OM.getProjects_GroupID(currentUser.groupID);
        taskList = new ArrayList<>();
        for (Project project : projectList) {
            holdTasks = OM.getTasks_ProjectID(project.projectID);
            for(Task task : holdTasks) taskList.add(task);
        }
        workLogList = OM.getWorkLogs_EmployeeID(currentUser.employeeID);
        messageList = OM.getMessages_EmployeeID(currentUser.employeeID);
    }
}
