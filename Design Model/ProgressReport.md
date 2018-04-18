## Progress Report
### Implemented Use Cases:
No use cases have been completely implemented. GUI work has been going in parallel,
so even though the back end for the following use cases is in place, the GUI still
needs to implement them. All necessary methods for getting and sending information
to the server have also been implemented, but need Model and GUI support before
the use cases can be considered implemented fully.
* (UC-1) Create Project
* (UC-9) Create Employee
* (UC-10) Edit Employee

### Already Functional
The OdinServer.java is completely done or only needing minor tweaks for the future.
It already has all the necessary methods for getting entries, creating new entries,
and editing already existing data. This includes all the searches that are deemed
necessary for querying the data for specific lists of data.

### Currently Being Tackled
The GUI is taking some work, but is being worked on steadily. The GUI supports
a login, but nothing beyond that for now. OdinModel.java is being worked on as well,
it is intended to make sure that the server only gets valid queries and that
the user does not try to push duplicate data onto the server.

## Plan of Work
The View and Add methods of OdinModel should be done by the end of this week on
April 21st. The initial status page and employee page is planned to be finished
by the 21st as well. Using the completing of these milestones, we should be able
to more accurately estimate future dates.

## Breakdown of Responsibilities
* GUI Work: David Henning
* Edit Methods: Yusuf Amani
* Add Methods: Joel Sanchez
* Get Methods: Ramya Singamsetty & Charles Foulk
* Coordinator/Server: Joel Sanchez
* Testing: Charles Foulk