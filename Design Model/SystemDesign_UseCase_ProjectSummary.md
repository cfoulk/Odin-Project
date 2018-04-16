```mermaid
sequenceDiagram 
User ->> GUI: User sends login info 
GUI -->> Model: GUI sends login info to Model
 Model -->> Server: Model requests employee info for username 
 Server -->> Model: Sends employee info if username exists 
 Model -->> Model: Checks if password is valid for username 
 Model -->> GUI: User is valid 
 GUI ->> User: GUI displays status window 
User ->> GUI: selects a project
GUI --> Model: sends selected project info to the Model
Model-->>Server: requests project info
Server-->> Model: sends project info to the Model
Model -->> GUI : display the project summary 
 ```
