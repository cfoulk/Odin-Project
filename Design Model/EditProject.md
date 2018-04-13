```mermaid
sequenceDiagram
User ->> GUI: User sends login info.
GUI -->> Model: GUI sends login info to Model.
Model -->> Server: Model requests employee info for username.
Server -->> Model: Sends employee info if username exists.
Model -->> Model: Checks if password is valid for username.
Model -->> GUI: User is valid.
GUI ->> User: GUI displays status window.
User ->> GUI: Select a project to edit.
GUI ->> User: GUI opens a project edit window.
User ->> GUI: User inputs project info and clicks save.
GUI -->> Model: GUI sends info to Model.
Model -->> Server: Model asks if project lead with employeeID exists.
Server -->> Model: Server confirms project lead exists.
Model -->> Server: Model sends project info to server.
GUI -->> Server: GUI requests update.
Server -->> GUI: Server sends update.
GUI -->> GUI: GUI updates.
GUI ->> User: Displays confirmation and new info.
```
