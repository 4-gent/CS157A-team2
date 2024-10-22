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
│
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── com.bookie.bizlogic  # all the high level functions like registerUser(), login() etc
│   │   │   ├── com.bookie.dao       # low level data access and db interaction classes
│   │   │   ├── com.bookie.models    # java classes that are equivalent for each Entities in db, User, Book etc.
│   ├── test
│   │   ├── java
│   │   │   ├── com.bookie.bizlogic
│   │   │   ├── com.bookie.dao
│   │   │   ├── com.bookie.models

├── src/main/webapp               # Web client files (.jsp)
│   ├── styles                    # CSS and other styling files/images
│   ├── pages                     # Web pages
│   └── index.jsp                 # Landing page for the application
```

### Important Notes

[ Depricated - See Notes Updates Section below]

Java classes should be stored in src/main/java to be reused in other methods, Connector acts as the JDBC connection to the backend database.

Actual client-side files will be in src/main/webapp, put .jsp files into pages and css and images will be stored in styles

The db name is bookup which can be changed if need be and the first table so far is user to store all user information in plaintext

### Updates

The package com.bookie.bizlogic  has all the high level business logic classes like UserService ( all user level interactions and methods - functions like registerUser(), login() etc), BookService with all book level interactions (Like browseBooks() etc)

The package com.bookie.dao   has all the low level data access and db interaction classes including the dbConnection

The package com.bookie.models  has all the java classes that are equivalent for each Entities in db, User, Book etc. At the higher levels of abstraction these classes are used at the business logic layer and UI layer.

Please the tests folder on how to use these high level methods

