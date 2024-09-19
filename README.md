# CS157A Sec02 Group 2 README

## Book Store Database Application

This document outlines the structure of the database application and explains how to run it.

### Directory Structure

Below is the directory structure for the project, which will be placed into the `webapps` folder in the Tomcat 9 directory:

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

To simplify database queries, a single JavaBeans file will be used to hold methods for SQL CRUD operations. This allows for easy reuse in any `.jsp` file, abstracting the code for calling methods when making queries from the frontend.
