package Model;

import Server.*;
import com.mysql.jdbc.StringUtils;
import com.sun.corba.se.spi.orbutil.threadpool.Work;

import javax.swing.text.DateFormatter;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class OdinModel implements OdinInterface {
    OdinServer OS;

    public OdinModel() throws IOException, SQLException {
        OS = new OdinServer();
    }

    //Returns the employee's ID number on success.
    //Returns -1 if the username does not exist.
    //Returns -2 if the username exists, but the password is wrong.
    public int getUserID(String userName, String password) {
        Employee emp;
        try {
            emp = OS.getEmployee_Username(userName);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        if (emp == null) return -1;
        if (emp.passwordCheck(password)) return emp.employeeID;
        return -2;
    }

    //Edit methods
    public boolean editEmployee(int employeeID, String name, String position, int groupID, String username, String password, String status) {
        Employee emp;
        try {
            emp = OS.getEmployee_EmployeeID(employeeID);
            if (emp != null) {
                emp = OS.getEmployee_Username(username);
                if (emp == null || emp.employeeID == employeeID) {
                    OS.editEmployee(employeeID, name, position, groupID, username, password, status);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean editProject(int projectID, String name, String dueDate, int groupID, int projectLeadID, String description, String status) {
        Project proj;
        Employee lead;
        try {
            lead = OS.getEmployee_EmployeeID(projectLeadID);
            proj = OS.getProject_ProjectID(projectID);
            if (proj != null && lead != null && lead.position.compareTo("Project Lead") == 0) {
                OS.editProject(projectID, name, dueDate, groupID, projectLeadID, description, status);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean editTask(int taskID, String name, String dueDate, int projectID, String employees, String description, int size, String status) {
        Task task;
        List<Integer> emp = extractEmployeeIDs(employees);
        Employee empHold;
        Project projHold;
        try {
            task = OS.getTask_TaskID(taskID);
            if (task != null) {
                projHold = getProject_ProjectID(projectID);
                if (projHold == null) return false;
                Collections.sort(emp);
                for (Integer integer : emp) {
                    empHold = OS.getEmployee_EmployeeID(integer);
                    if (empHold == null) return false;
                    if (empHold.groupID != projHold.groupID) return false;
                }
                employees = ",";
                for (Integer integer : emp) employees = employees.concat(integer.toString() + ',');
                OS.editTask(taskID, name, dueDate, projectID, employees, description, size, status);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean editWorkLog(int logID, String startTime, String stopTime, String elapsedTime, String description, int taskID, int employeeID) {
        WorkLog log;
        Employee emp;
        Task task;
        try {
            log = OS.getWorkLog_LogID(logID);
            emp = OS.getEmployee_EmployeeID(employeeID);
            task = OS.getTask_TaskID(taskID);
            if (log != null && emp != null && task != null && task.hasEmployee(employeeID)) {
                OS.editWorkLog(logID, taskID, employeeID, elapsedTime, startTime, stopTime, description);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean stopWork(int logID, String description) {
        WorkLog workLog = null;
        String startString, stopString;
        try { workLog = OS.getWorkLog_LogID(logID); }
        catch(Exception e) { e.printStackTrace(); }
        if (workLog != null) {
            startString = workLog.startTime;
            startString = startString.substring(0,19);
            stopString = LocalDateTime.now().toString();
            stopString = stopString.substring(0,19);
            editWorkLog(logID, startString, stopString, calcElapsedTime(startString, stopString), description, workLog.taskID, workLog.employeeID);
            return true;
        }
        return false;
    }

    public String calcElapsedTime(String startString, String stopString)
    {
        DateTimeFormatter formatter;
        LocalDateTime startTime, stopTime;
        String elapsedTime;
        long hours, minutes, seconds;

        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        startString = startString.substring(0,19);
        stopString = stopString.substring(0,19);
        startString = startString.replace('T', ' ');
        stopString = stopString.replace('T', ' ');
        startTime = LocalDateTime.parse(startString, formatter);
        stopTime = LocalDateTime.parse(stopString, formatter);

        hours = startTime.until(stopTime, ChronoUnit.HOURS);
        startTime = startTime.plusHours(hours);
        minutes = startTime.until(stopTime, ChronoUnit.MINUTES);
        startTime = startTime.plusMinutes(minutes);
        seconds = startTime.until(stopTime, ChronoUnit.SECONDS);

        elapsedTime = hours + " hours, " + minutes + " minutes, " + seconds + " seconds.";

        return elapsedTime;
    }

    public boolean setMessage_Read(int MessageID) {
        Message mes;
        try {
            mes = OS.getMessage_MessageID(MessageID);
            if (mes != null) {
                OS.setMessage_Read(MessageID);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean setProject_Open(int ProjectID) {
        Project proj;
        try {
            proj = OS.getProject_ProjectID(ProjectID);
            if (proj != null) {
                OS.setProject_Open(ProjectID);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean setProject_Closed(int ProjectID) {
        Project proj;
        try {
            proj = OS.getProject_ProjectID(ProjectID);
            if (proj != null) {
                OS.setProject_Closed(ProjectID);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean setTask_Open(int TaskID) {
        Task task;
        try {
            task = OS.getTask_TaskID(TaskID);
            if (task != null) {
                OS.setTask_Open(TaskID);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean setTask_Closed(int TaskID) {
        Task task;
        try {
            task = OS.getTask_TaskID(TaskID);
            if (task != null) {
                OS.setTask_Closed(TaskID);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //Add methods
    public boolean addEmployee(String name, String position, int groupID, String username, String password, String status) {
        Employee emp;
        try {
            emp = OS.getEmployee_Username(username);
            if (emp == null) {
                OS.addEmployee(name, position, groupID, username, password, status);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addProject(String name, String dueDate, int groupID, int projectLeadID, String description, String status) {
        Employee lead;
        try {
            lead = OS.getEmployee_EmployeeID(projectLeadID);
            if (lead != null && lead.position.compareTo("Project Lead") == 0) {
                OS.addProject(name, Date.valueOf(dueDate), groupID, projectLeadID, description, status);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addTask(String name, String dueDate, int projectID, String employees, String description, int size, String status) {
        List<Integer> emp = extractEmployeeIDs(employees);
        Employee empHold;
        Project projHold;
        try {
            projHold = getProject_ProjectID(projectID);
            if (projHold == null) return false;
            Collections.sort(emp);
            for (Integer integer : emp) {
                empHold = OS.getEmployee_EmployeeID(integer);
                if (empHold == null) return false;
                if (empHold.groupID != projHold.groupID) return false;
            }
            employees = ",";
            for (Integer integer : emp) employees = employees.concat(integer.toString() + ',');
            OS.addTask(name, dueDate, projectID, employees, description, size, status);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addWorkLog(String startTime, String stopTime, String elapsedTime, String description, int taskID, int employeeID) {
        Employee emp;
        Task task;
        try {
            emp = OS.getEmployee_EmployeeID(employeeID);
            task = OS.getTask_TaskID(taskID);
            if (emp != null && task != null && task.hasEmployee(employeeID)) {
                OS.addWorkLog(taskID, employeeID, elapsedTime, startTime, stopTime, description);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int startWork(int taskID, int employeeID) {
        Employee emp;
        Task task;
        LocalDateTime startDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startTime = startDateTime.format(formatter);
        startTime = startTime.substring(0,19);
        try {
            emp = OS.getEmployee_EmployeeID(employeeID);
            task = OS.getTask_TaskID(taskID);
            if (emp != null && task != null && task.hasEmployee(employeeID)) {
                return OS.startWorkLog(taskID, employeeID, startTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean addMessage(String message, int recipientID, int senderID) {
        Employee emp;
        try {
            emp = OS.getEmployee_EmployeeID(recipientID);
            if (emp != null) {
                OS.addMessage(message, recipientID, senderID);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //deletes
    public boolean deleteEmployee_EmployeeID(int employeeID) {
        Employee employee;
        try {
            employee = OS.getEmployee_EmployeeID(employeeID);
            if (employee != null) {
                OS.deleteEmployee_EmployeeID(employeeID);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteTask_TaskID(int taskID) {
        Task task;
        try {
            task = OS.getTask_TaskID(taskID);
            if (task != null) {
                OS.deleteTask_TaskID(taskID);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteMessage_MessageID(int messageID) {
        Message message;
        try {
            message = OS.getMessage_MessageID(messageID);
            if (message != null) {
                OS.deleteMessage_MessageID(messageID);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteMessages_RecipientID(int recipientID) {
        List<Message> message;
        try {
            message = OS.getMessages_RecipientID(recipientID);
            if (message != null) {
                OS.deleteMessages_RecipientID(recipientID);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteMessages_SenderID(int senderID) {
        List<Message> message;
        try {
            message = OS.getMessages_SenderID(senderID);
            if (message != null) {
                OS.deleteMessages_SenderID(senderID);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //Get Singles
    public Employee getEmployee_EmployeeID(int employeeID) {
        Employee employee;
        try {
            employee = OS.getEmployee_EmployeeID(employeeID);
        } catch (Exception e) {
            return null;
        }
        return employee;
    }

    public Employee getEmployee_Username(String username) {
        Employee employee;
        try {
            employee = OS.getEmployee_Username(username);
        } catch (Exception e) {
            return null;
        }
        return employee;
    }

    public Project getProject_ProjectID(int projectID) {
        Project project;
        try {
            project = OS.getProject_ProjectID(projectID);
        } catch (Exception e) {
            return null;
        }
        return project;
    }

    public Task getTask_TaskID(int taskID) {
        Task task;
        try {
            task = OS.getTask_TaskID(taskID);
        } catch (Exception e) {
            return null;
        }
        return task;
    }

    public WorkLog getWorkLog_LogID(int logID) {
        WorkLog workLog;
        try {
            workLog = OS.getWorkLog_LogID(logID);
        } catch (Exception e) {
            return null;
        }
        return workLog;
    }

    public Message getMessage_MessageID(int messageID) {
        Message message;
        try {
            message = OS.getMessage_MessageID(messageID);
        } catch (Exception e) {
            return null;
        }
        return message;
    }

    //Get Sets
    public List<Employee> getEmployees() {
        List<Employee> employees;
        try {
            employees = OS.getEmployees();
        } catch (Exception e) {
            return null;
        }
        return employees;
    }

    public List<Employee> getEmployees_GroupID(int groupID) {
        List<Employee> employees;
        try {
            employees = OS.getEmployees_GroupID(groupID);
        } catch (Exception e) {
            return null;
        }
        return employees;
    }

    public List<Project> getProjects() {
        List<Project> projects;
        try {
            projects = OS.getProjects();
        } catch (Exception e) {
            return null;
        }
        return projects;
    }

    public List<Project> getProjects_GroupID(int groupID) {
        List<Project> projects;
        try {
            projects = OS.getProject_GroupID(groupID);
        } catch (Exception e) {
            return null;
        }
        return projects;
    }

    public List<Project> getProjects_ProjectLeadID(int projectLeadID) {
        List<Project> projects;
        try {
            projects = OS.getProject_ProjectLeadID(projectLeadID);
        } catch (Exception e) {
            return null;
        }
        return projects;
    }

    public List<Project> getProjects_Status(String status) {
        List<Project> projects;
        try {
            projects = OS.getProjects_Status(status);
        } catch (Exception e) {
            return null;
        }
        return projects;
    }

    public List<Task> getTasks() {
        List<Task> tasks;
        try {
            tasks = OS.getTasks();
        } catch (Exception e) {
            return null;
        }
        return tasks;
    }

    public List<Task> getTasks_ProjectID(int projectID) {
        List<Task> tasks;
        try {
            tasks = OS.getTasks_ProjectID(projectID);
        } catch (Exception e) {
            return null;
        }
        return tasks;
    }

    public List<Task> getTasks_EmployeeID(int employeeID) {
        List<Task> tasks;
        try {
            tasks = OS.getTasks_EmployeeID(employeeID);
        } catch (Exception e) {
            return null;
        }
        return tasks;
    }

    public List<WorkLog> getWorkLogs() {
        List<WorkLog> workLogs;
        try {
            workLogs = OS.getWorkLogs();
        } catch (Exception e) {
            return null;
        }
        return workLogs;
    }

    public List<WorkLog> getWorkLogs_EmployeeID(int employeeID) {
        List<WorkLog> workLogs;
        try {
            workLogs = OS.getWorkLogs_EmployeeID(employeeID);
        } catch (Exception e) {
            return null;
        }
        return workLogs;
    }

    public List<WorkLog> getWorkLogs_TaskID(int taskID) {
        List<WorkLog> workLogs;
        try {
            workLogs = OS.getWorkLogs_TaskID(taskID);
        } catch (Exception e) {
            return null;
        }
        return workLogs;
    }

    public List<Message> getMessages() {
        List<Message> messages;
        try {
            messages = OS.getMessages();
        } catch (Exception e) {
            return null;
        }
        return messages;
    }

    public List<Message> getMessages_RecipientID(int recipientID) {
        List<Message> messages;
        try {
            messages = OS.getMessages_RecipientID(recipientID);
        } catch (Exception e) {
            return null;
        }
        return messages;
    }

    public List<Message> getMessages_SenderID(int senderID) {
        List<Message> messages;
        try {
            messages = OS.getMessages_SenderID(senderID);
        } catch (Exception e) {
            return null;
        }
        return messages;
    }

    //Filters
    public Employee filterEmployees_EmployeeID(List<Employee> list, int employeeID) {
        for (Employee employee : list) {
            if (employee.employeeID == employeeID) return employee;
        }
        return null;
    }

    public Employee filterEmployees_Username(List<Employee> list, String username) {
        for (Employee employee : list) {
            if (employee.username.toLowerCase().compareTo(username.toLowerCase()) == 0) return employee;
        }
        return null;
    }

    public List<Employee> filterEmployees_GroupID(List<Employee> list, int groupID) {
        List<Employee> employees = new ArrayList<>();
        for (Employee employee : list) {
            if (employee.groupID == groupID) employees.add(employee);
        }
        return employees;
    }

    public List<Employee> filterEmployees_Position(List<Employee> list, String position) {
        List<Employee> employees = new ArrayList<>();
        for (Employee employee : list) {
            if (employee.position.compareTo(position) == 0) employees.add(employee);
        }
        return employees;
    }

    public Project filterProjects_ProjectID(List<Project> list, int projectID) {
        for (Project project : list) {
            if (project.projectID == projectID) return project;
        }
        return null;
    }

    public List<Project> filterProjects_DueDate(List<Project> list, String dueDate) {
        List<Project> projects = new ArrayList<>();
        for (Project project : list) {
            if (project.dueDate.toLowerCase().compareTo(dueDate.toLowerCase()) == 0) projects.add(project);
        }
        return projects;
    }

    public List<Project> filterProjects_GroupID(List<Project> list, int groupID) {
        List<Project> projects = new ArrayList<>();
        for (Project project : list) {
            if (project.groupID == groupID) projects.add(project);
        }
        return projects;
    }

    public List<Project> filterProjects_ProjectLeadID(List<Project> list, int projectLeadID) {
        List<Project> projects = new ArrayList<>();
        for (Project project : list) {
            if (project.projectLeadID == projectLeadID) projects.add(project);
        }
        return projects;
    }

    public List<Project> filterProjects_Status(List<Project> list, String status) {
        List<Project> projects = new ArrayList<>();
        for (Project project : list) {
            if (project.status.toLowerCase().compareTo(status.toLowerCase()) == 0) projects.add(project);
        }
        return projects;
    }

    public Task filterTasks_TaskID(List<Task> list, int taskID) {
        for (Task tsk : list) {
            if (tsk.taskID == taskID) return tsk;
        }
        return null;
    }

    public List<Task> filterTasks_DueDate(List<Task> list, String dueDate) {
        List<Task> tasks = new ArrayList<>();
        for (Task task : list) {
            if (task.dueDate.toLowerCase().compareTo(dueDate.toLowerCase()) == 0) tasks.add(task);
        }
        return tasks;
    }

    public List<Task> filterTasks_EmployeeID(List<Task> list, int employeeID) {
        List<Task> tasks = new ArrayList<>();
        for (Task task : list) {
            if (extractEmployeeIDs(task.employees).contains(employeeID)) tasks.add(task);
        }
        return tasks;
    }

    public List<Task> filterTasks_ProjectID(List<Task> list, int projectID) {
        List<Task> tasks = new ArrayList<>();
        for (Task task : list) {
            if (task.projectID == projectID) tasks.add(task);
        }
        return tasks;
    }

    public List<Task> filterTasks_Status(List<Task> list, String status) {
        List<Task> tasks = new ArrayList<>();
        for (Task task : list) {
            if (task.status.toLowerCase().compareTo(status.toLowerCase()) == 0) tasks.add(task);
        }
        return tasks;
    }

    public List<Task> filterTasks_Size(List<Task> list, int size) {
        List<Task> tasks = new ArrayList<>();
        for (Task task : list) {
            if (task.size == size) tasks.add(task);
        }
        return tasks;
    }

    public WorkLog filterWorkLog_LogID(List<WorkLog> list, int logID) {
        for (WorkLog log : list) {
            if (log.logID == logID) return log;
        }
        return null;
    }

    public List<WorkLog> filterWorkLog_TaskID(List<WorkLog> list, int taskID) {
        List<WorkLog> workLogs = new ArrayList<>();
        for (WorkLog workLog : list) {
            if (workLog.taskID == taskID) workLogs.add(workLog);
        }
        return workLogs;
    }

    public List<WorkLog> filterWorkLog_EmployeeID(List<WorkLog> list, int employeeID) {
        List<WorkLog> workLogs = new ArrayList<>();
        for (WorkLog workLog : list) {
            if (workLog.employeeID == employeeID) workLogs.add(workLog);
        }
        return workLogs;
    }

    public List<Message> filterMessages_RecipientID(List<Message> list, int recipientID) {
        List<Message> messages = new ArrayList<>();
        for (Message message : list) {
            if (message.recipientID == recipientID) messages.add(message);
        }
        return messages;
    }

    public List<Message> filterMessages_SenderID(List<Message> list, int senderID) {
        List<Message> messages = new ArrayList<>();
        for (Message message : list) {
            if (message.senderID == senderID) messages.add(message);
        }
        return messages;
    }

    public List<Message> filterMessages_Read(List<Message> list) {
        List<Message> messages = new ArrayList<>();
        for (Message message : list) {
            if (message.status.equals("Read")) messages.add(message);
        }
        return messages;
    }

    public List<Message> filterMessages_Unread(List<Message> list) {
        List<Message> messages = new ArrayList<>();
        for (Message message : list) {
            if (message != null && message.status.equals("Unread")) messages.add(message);
        }
        return messages;
    }

    public List<Integer> extractEmployeeIDs(String employees) {
        List<Integer> list = new ArrayList<>();
        StringTokenizer stk = new StringTokenizer(employees, ",");
        while (stk.hasMoreTokens()) list.add(Integer.valueOf(stk.nextToken()));
        return list;
    }

    public List<Integer> sortEmployeeIDs(String emp) {
        List<Integer> list = extractEmployeeIDs(emp);
        Collections.sort(list);
        return list;
    }

    public String empListToString(String empList) {
        String sortedString = "";
        List<Integer> sortedList = sortEmployeeIDs(empList);
        for (Integer integer : sortedList) {
            sortedString = sortedString.concat(integer.toString().concat(", "));
        }
        if(!sortedString.isEmpty())sortedString = sortedString.substring(0, sortedString.lastIndexOf(", "));
        return sortedString;
    }

    public String durationAsString(Duration dur) {
        long totalSec = dur.getSeconds();

        int years = (int) totalSec / (3600 * 24 * 365);
        int days = (int) totalSec / (3600 * 24) % 365;
        int hours = (int) totalSec / 3600 % 24;
        int minutes = (int) totalSec % 3600 / 60;
        int seconds = (int) totalSec % 60;

        String out = "";

        if (years != 0) {
            out += "Years: " + years + " ";
        }
        if (days != 0) {
            out += "Days: " + days + " ";
        }
        if (hours != 0) {
            out += "Hrs: " + hours + " ";
        }
        if (minutes != 0) {
            out += "Min: " + minutes + " ";
        }
        out += "Sec: " + seconds + " ";


        return out;
    }

    public Duration calcDuration(List<WorkLog> workLogs) {
        LocalDateTime startTime, endTime;
        Duration duration = Duration.ZERO;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss");
        for(WorkLog workLog : workLogs)
        {
            startTime = LocalDateTime.parse(workLog.startTime.substring(0,19), formatter);
            endTime = LocalDateTime.parse(workLog.startTime.substring(0,19), formatter);
            duration = duration.plus(Duration.between(startTime, endTime));
        }
        return duration;
    }

    public List<WorkLog> getWorkLogs(Task task, List<WorkLog> workLogs) {
        return filterWorkLog_TaskID(workLogs, task.taskID);
    }

    public List<WorkLog> getWorkLogs(Project project, List<Task> tasks, List<WorkLog> workLogs) {
        List<WorkLog> finalList = new ArrayList<>();
        for(Task task : tasks)
        {
            if(task.projectID == project.projectID) finalList.addAll(getWorkLogs(task, workLogs));
        }
        return finalList;
    }

    public boolean isValidName(String name) {
        return !(name.matches("(.*)[0-9](.*)") || name.matches("(.*)[!\"#$%&'()*+,./:;<=>?@^_`{|}~-](.*)"));
    }

    public boolean isValidDate(String date) {
        if (date.isEmpty() || LocalDate.parse(date).toString().equals(date)) return true;
        else return false;
    }

    public boolean isValidNum(String number)
    {
        return StringUtils.isStrictlyNumeric(number);
    }

    public boolean isValidString(String string)
    {
        return !(string.matches("(.*)[\'\";()](.*)"));
    }

    public boolean isValidWorkStatus(String status)
    {
        return (status.equals("Open") || status.equals("Closed"));
    }

    public boolean isValidEmpStatus(String status)
    {
        return (status.equals("Active") || status.equals("Inactive"));
    }

    public boolean isValidPos(String status) {
        return (status.equals("Manager") || status.equals("Project Lead") || status.equals("Employee"));
    }

    public boolean isValidEmpList(String empList) {
        if(empList.matches("(.*)[a-zA-z](.*)") || empList.matches("(.*)[!\"#$%&'()*+./:;<=>?@^_`{|}~-](.*)"))
            return false;
        int size = empList.length();
        for(int i = 0; i < size; ++i)
        {
            if(!(Character.compare(empList.charAt(i), ',') == 0 || Character.isDigit(empList.charAt(i))))
                return false;
        }
        return true;
    }

    public boolean isValidDateTime(String time)
    {
        time = time.substring(0,19);
        //time = time.replace(' ', 'T');
        if (time.isEmpty() || LocalDateTime.parse(time).toString().equals(time)) return true;
        else return false;
    }

    public String now()
    {
        DateTimeFormatter formatter;
        LocalDateTime now;
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        now = LocalDateTime.now();
        return now.format(formatter);
    }

    public void closeConnection() {
        try {
            OS.con.close();
            OS.stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isClosed() throws Exception
    {
        return OS.isClosed();
    }
}