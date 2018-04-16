# Odin Project

### Introduction
Odin is a project management software designed for use by small technical businesses to help organize their work towards large goals. It's leadership based structure allows for different responsibilities to be held by different workers. Managers organize the Project Leads who manage the employees. Managers create, delete, and define projects. Project leads give tasks to employees to work towards the project. Employees log their work through starting, stopping, and describing.

### Product Reviews
Basecamp is a business management tool used to keep company projects organized and employees informed. It utilizes message boards and project pages to keep work organized and communicative. The software can be used for scheduling, sharing workloads, chatting, file sharing, and work logging. All work is divided into different teams and projects. Remember The Milk is a much more personal task management software that is meant for families to keep track of everyone's responsibilities. It includes the ability to add tasks through single lines that define everything from the name, to what it is, to a due date, and a priority size. It reminds the user of their tasks through a computer using IM and email, or a phone through texting and push apps. Each task can be broken up into smaller subtasks and lists of tasks can be shared with others using the app. Producteev is another group project manager with useful tools for keeping a project rolling. Deadlines, reminders, priorities, tags, labels, and following all help keep the project participants knowledgeable of the work their peers are doing. In addition, team members can assign each other tasks to give everyone an idea of what they should be doing in the project. Asana separates itself from other project management tools by being one of the most visual ones we could find.

### Project Overview
From our research on various project management tools we will be using a couple of ideas. First, our present Task Management software has no way of recognizing when a task is finished, so we will be adding support for finished tasks as well as due dates for the tasks. In addition, projects without priorities lack structure and guidance, so we will be adding priorities to our tasks. As a project is composed of many tasks, a progression check for how close a project is to completion will be added. We will be also adding support for logging in under different users to keep track of everyone's participation separately as well as keeping the responsibilities in line.Finally, we will be adding a GUI to make the product more suitable for general use by non computer specialists.

### Project Architecture
The core ideas of our prior task management software will remain the same. The tool will be used to manage the progression of tasks. Our first deviation is that we will be using JavaFX for a GUI and Jfoenix for material design. We will be storing our project data using an SQL Database. We will be also implementing a login system and as such we will keep user login information. Where we previously had only one set of data to store, this new product will have many. Each project will have its own data, so only tasks related to that project will be held together. In addition, we will have another data set to hold our login information.

### Requirements
ID|Priority|Description
:-:|:-:|---|
REQ1	|5|The system will store all task and project data to a SQL database for future access .
REQ2	|2|The system will allow access to only the authorized users through a user login page.
REQ3	|4|The system will allow authorized users for the creation and modification of projects as a container for tasks.
REQ4	|1|The system will allow administrative users to create and manage temporary logins for new users.
REQ5	|3|The system shall allow authorized users to create, define due date, and assign tasks/projects to users.
REQ6	|2|The system shall allow users to redefine tasks (description and size)
REQ7	|1|The system shall allow users to change their login information.
REQ8	|4|The system will allow users to start and stop work on a task and log subsuquent information associated with the action.
REQ9	|2|The system will keep track of user created projects and assoicated tasks for generating user statistics and reports.
REQ10	|3|The system will provide statistics on available task and project data. Such as, minimum time, maximum time, average time, durations etc.
REQ11	|1|The system shall display messages set by authorized users ("Your performance is subpar and you are risking termination, fyi") on login.
REQ12	|2|The system will limit project and task view to users specfic to that project.
REQ13	|1|The system will notify authorized users (project lead) when a task is marked as complete by regular user (employee) on login.
REQ14	|3|The system will allow authorized users to conclude items such as projects and tasks to prevent access to sub-users

### Use Cases
ID|Name|Description
---|---|---
UC-1    |Create Project         | Create a project, defining its name, group, and project leader. Optional: Due date, description, and status.
UC-2    |Edit Project           | Change any data of a project excluding the project ID. Can change name, group, lead, due date, status, and description.
UC-3    |Summarize Project      | View all of the information that a project contains, including a list of its tasks and the time spent on it.
UC-4    |Create Task            | Create a task, defining its name, associated project, and employee assigned. Optional: Due date, description, and size.
UC-5    |Edit Task              | Change any data of a task excluding the task ID. Can change name, associated project, employee assigned, due date, description, and size.
UC-6    |Summarize Task         | View all of the information that a task contains, including the time spent on it.
UC-7    |Start a work session   | Log the beginning of a working session.
UC-8    |End a work session     | Log the end of a work session, including a description of the work done. May request closure of task.
UC-9    |Edit a work session    | Change any data of a work log excluding the work log ID. Can change the entry type, the description, the timestamp, the employee assigned, and the task assigned
UC-10   |Create Employee        | Create an employee, defining its name, position, username, and password. Optional: Group.
UC-11   |Edit Employee          | Change any data of an employee excluding the employee ID. Can change name, group, position, username, and password.
UC-12   |Change Login           | Change the login information of the logged in user.

### Accountability Matrix
Requirement |PW |UC1|UC2|UC3|UC4|UC5|UC6|UC7|UC8|UC9|UC10|UC11|UC12
---         |:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:
REQ1|   5|  X   |X  |X  |X  |X  |X  |X  |X  |X  |X  |X  |X
REQ2|   2|  X   |X  |X  |X  |X  |X  |X  |X  |X  |X  |X  |X
REQ3|   4|  X   |X  |X  |   |   |   |   |   |   |   |   |
REQ4|   1|      |   |   |   |   |   |   |   |   |X  |X  |X
REQ5|   3|      |X  |   |   |X  |   |   |   |   |X  |X  |
REQ6|   2|      |X  |   |   |X  |   |   |   |   |   |   |
REQ7|   1|      |   |   |   |   |   |   |   |   |   |X  |X
REQ8|   4|      |   |   |   |   |   |X  |X  |X  |   |   |
REQ9|   2|      |   |X  |   |   |X  |   |   |   |   |   |
REQ10|  3|      |   |X  |   |   |X  |   |   |   |   |   |
REQ11|  1|      |   |   |   |   |   |   |   |   |   |   |
REQ12|  2|  X   |X  |X  |X  |X  |X  |X  |X  |X  |   |   |
REQ13|  1|      |X  |   |   |X  |   |   |   |   |   |   |
REQ14|  3|      |X  |   |   |X  |   |   |   |   |   |   |
Max PW|||    5  |5  |5  |5  |5  |5  |5  |5  |5  |5  |5  |5
Total PW|||  13 |22 |18 |9  |18 |14 |13 |18 |13 |11 |12 |9

### Use Case Blocks
Use Case UC - | Name
---|---
Initiating Actor:|
Actor's Goal:|
Participating Actors:|
Preconditions:|
Postconditions:|
Event Flow for Main:|- - -
Event Flow for Extension:|- - -

Use Case UC - 1| Create a project
---|---
Initiating Actor:|Manager
Actor's Goal:|To create a new project.
Participating Actors:|Odin
Preconditions:|None worth mentioning.
Postconditions:|A new project is added to the database.
Event Flow for Main:|- - -
1   |**Manager** arrives at the terminal and is prompted for a log in.
2   |**Manager** supplies his login information.
3   |(a) **Odin** verifies the login information, and (b) signals the **Manager** of its validity.
4   |**Odin** (a) displays options available to the **Manager**, (b) and prompts the **Manager** to make a selection.
5   |**Manager** selects the "Create a project" option and enters the new data.
6   |**Odin** (a) stores the new data and (b) signals completion.
Event Flow for Extension:|- - -
2a  |**Manager** supplies invalid login information.
1   |**Odin** (a) detects an error and (b) signals the **Manager**.
2   |**Manager** supplies his login information.
3   |Same as in step 3 above.

Use Case UC - 2| Editing a project
---|---
Initiating Actor:|Manager or Project Lead
Actor's Goal:|Change properties of the project, including its name, group number, description, status, and due date.
Participating Actors:|Odin
Preconditions:|There is a project to edit.
Postconditions:|Changes are made to the project in accordance to what the actor inputs.
Event Flow for Main:|- - -
1   |**Actor** arrives at the terminal and is prompted for a log in.
2   |**Actor** supplies his login information.
3   |(a) **Odin** verifies the login information, and (b) signals the **Actor** of its validity.
4   |**Odin** (a) displays options available to the **Actor**, (b) and prompts the **Actor** to make a selection.
5   |**Actor** selects the "Edit a project" option and enters the project's name.
6   |**Odin** (a) verifies the project exists, (b) displays options available to the **Actor**, and (c) prompts the **Actor** to make a selection.
7   |**Actor** selects one of the options, and provides new information for the selection.
8   |**Odin** (a) stores the new information in the database, and (b) signals completion.
Event Flow for Extension:|- - -
2a|**Actor** supplies invalid login information.
1   |**Odin** (a) detects an error and (b) signals the **Actor**.
2   |**Actor** supplies his login information.
3   |Same as in step 3 above.
5a  |
1   |**Odin** (a) finds no project with the given name and (b) signals the **Actor**.
2   |**Actor** enters the project's name.
3   |Same as in Step 6 above.

Use Case UC - 3| Summarize a project
---|---
Initiating Actor:|Manager, Project Lead, or Employee
Actor's Goal:|See a summary of a project.
Participating Actors:|Odin
Preconditions:|There is a project to summarize.
Postconditions:|The Actor sees a summary of the project.
Event Flow for Main:|- - -
1   |**Actor** arrives at the terminal and is prompted for a log in.
2   |**Actor** supplies his login information.
3   |(a) **Odin** verifies the login information, and (b) signals the **Actor** of its validity.
4   |**Odin** (a) displays options available to the **Actor**, (b) and prompts the Actor to make a selection.
5   |**Actor** selects the "See time spent on a project" option and enters the project's name.
6   |**Odin** (a) verifies the project exists, (b) displays a summary of the project.
Event Flow for Extension:|- - -
2a  |**Actor** supplies invalid login information.
1   |**Odin** (a) detects an error and (b) signals the **Actor**.
2   |**Actor** supplies his login information.
3   |Same as in step 3 above.
5a  |
1   |**Odin** (a) finds no project with the given name and (b) signals the **Actor**.
2   |**Actor** enters the project's name.
3   |Same as in Step 6 above.

Use Case UC - 4| Create a task
---|---
Initiating Actor:|Project Lead
Actor's Goal:|To create a new task.
Participating Actors:|Odin
Preconditions:|There is a project for the task to be associated with.
Postconditions:|A new task is added to the database.
Event Flow for Main:|- - -
1   |**Project Lead** arrives at the terminal and is prompted for a log in.
2   |**Project Lead** supplies his login information.
3   |(a) **Odin** verifies the login information, and (b) signals the **Project Lead** of its validity.
4   |**Odin** (a) displays options available to the **Project Lead**, (b) and prompts the **Project Lead** to make a selection.
5   |**Project Lead** selects the "Create a task" option.
6   |Odin prompts the user for a project name to link the task to.
7   |**Project Lead** enters a project name.
8   |**Odin** (a) verifies the project exists, (b) and prompts the **Project Lead** to enter data for the task.
9   |**Odin** (a) stores the new data and (b) signals completion.
Event Flow for Extension:|- - -
2a  |**Project Lead** supplies invalid login information.
1   |**Odin** (a) detects an error and (b) signals the **Project Lead**.
2   |**Project Lead** supplies his login information.
3   |Same as in step 3 above.
7a  |
1   |**Odin** (a) finds no project with the given name and (b) signals the **Project Lead**.
2   |**Project Lead** enters the project's name.
3   |Same as in Step 8 above.
