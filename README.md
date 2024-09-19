# CS157A Sec02 Group 2 README

## Book Store Database Application

This document outlines the structure of the database application and explains how to run it.

### Directory Structure

Below is the directory structure for the project where the Tomcat 9.0 server will run directly from Eclipse IDE

```bash
CS157A-team2
├── Java Resources
│   └── src/main/java
│       └── defaultpackage
│           ├── Connector.java    # Class to handle database connections
│           ├── Login.java        # Class to handle user login queries
│           ├── Register.java     # Class to handle new user registration
│           └── User.java         # Class to create User objects
├── src/main/webapp               # Web client files (.jsp)
│   ├── styles                    # CSS and other styling files/images
│   ├── pages                     # Web pages
│   └── index.jsp                 # Landing page for the application
```

### Important Notes

Java classes should be stored in src/main/java to be reused in other methods, Connector acts as the JDBC connection to the backend database.

Actual client-side files will be in src/main/webapp, put .jsp files into pages and css and images will be stored in styles

The db name is bookup which can be changed if need be and the first table so far is user to store all user information in plaintext
