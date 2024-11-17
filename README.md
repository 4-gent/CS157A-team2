# CS157A Sec02 Group 2 README

## Book Store Database Application

This document outlines the structure of the database application and explains how to run it.

### Directory Structure

Below is the directory structure for the project where the Tomcat 9.0 server will run directly from Eclipse IDE

```
bookup/                    <-- Root directory of your Maven project
├── pom.xml                <-- Maven configuration file
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── bookie/
│   │   │           ├── auth/               <-- Package for authentication and authorization
│   │   │           │   ├── AccessControlUtil.java
│   │   │           │   ├── IsAdmin.java
│   │   │           │   ├── IsAdminOrSameUser.java
│   │   │           │   ├── SameUser.java
│   │   │           │   └── UserContext.java
│   │   │           ├── bizlogic/           <-- Package for business logic (empty in the screenshot)
│   │   │           ├── dao/                <-- Package for Data Access Objects (DAOs)
│   │   │           ├── models/             <-- Package for models or data entities
│   │   │           └── servlet/            <-- Package for Servlets
│   │   │               ├── GetBooks.java
│   │   │               ├── Login.java
│   │   │               ├── Register.java
│   │   │               └── Search.java
│   │   ├── resources/                      <-- Resource files (e.g., configuration files)
│   │   └── webapp/
│   │       ├── META-INF/                   <-- Metadata files
│   │       ├── WEB-INF/
│   │       │   └── web.xml                <-- Deployment descriptor
│   │       ├── pages/                     <-- JSP pages for the web application
│   │       │   ├── books.jsp
│   │       │   ├── error.jsp
│   │       │   ├── login.jsp
│   │       │   ├── register.jsp
│   │       │   ├── search.jsp
│   │       │   └── success.jsp
│   │       ├── index.jsp                  <-- Landing page for the application
│   │       ├── styles/                    <-- CSS files
│   │       └── web-resources/             <-- Additional web resources (if any)
│   ├── test/
│   │   ├── java/                          <-- Unit tests for your application
│   │   └── resources/                     <-- Test resources (e.g., test configuration)
├── target/                                <-- Compiled classes and build artifacts (generated)
├── build/                                 <-- Build-related files (e.g., compiled WAR files)
└── sql/                                   <-- SQL scripts (if any)
```
# Project Structure

## pom.xml
The Maven configuration file where dependencies, plugins, and project details are defined.

## src/main/java
This is where your Java source code resides.

- **auth/**: Contains classes related to authentication and authorization (e.g., `UserContext`, annotations for access control).
- **bizlogic/**: *(Currently empty)* A placeholder for business logic-related classes.
- **dao/**: Contains Data Access Objects that interact with the database.
- **models/**: Contains classes representing data entities.
- **servlet/**: Contains servlets that handle HTTP requests.

## src/main/resources
This directory is typically used for configuration files (like `.properties` files).

## src/main/webapp
- **META-INF/**: Contains metadata files (usually related to the application server).
- **WEB-INF/**: Contains the `web.xml` deployment descriptor and other server-side configurations.
- **pages/**: Contains JSP files used to render views in your web application.
- **styles/**: Contains CSS files for styling the web pages.
- **index.jsp**: The main entry point for your application.

## src/test/java
Contains unit tests for your application.

## target/
The directory where compiled classes, JAR/WAR files, and other build artifacts are generated.

## build/ and sql/
- **build/**: Directory for build-related files (optional).
- **sql/**: Directory for SQL scripts used to set up the database (optional).

# How to Use the Project Structure

## Building the Project
To compile and package your project, run the following command in the terminal:

```bash
mvn clean package
```

This command will:
- Clean any previous build artifacts.
- Compile the Java source code.
- Generate the `.war` file in the `target/` directory.

---

## Running the Project on Tomcat

### Option 1: Manually Deploy the WAR File
1. Locate the generated `.war` file in the `target/` directory (e.g., `bookup.war`).
2. Copy the `.war` file to the `webapps/` folder of your Tomcat installation.
3. Start the Tomcat server:
   ```bash
   /opt/homebrew/opt/tomcat/bin/catalina.sh start
   ```
4. Access the application in your browser at:
   ```
   http://localhost:8080/bookup
   ```

---

### Option 2: Use the Tomcat Maven Plugin
If you’ve configured the Tomcat Maven plugin in your `pom.xml`, you can deploy directly from the terminal.

To deploy the application:
```bash
mvn tomcat9:deploy
```

To stop the deployed application:
```bash
mvn tomcat9:undeploy
```

---

## Accessing the Application
Once deployed, access your application in a web browser at:
```
http://localhost:8080/bookup
```

---

## Running Tests
To run your unit tests, use the following command:

```bash
mvn test
```

This will:
- Compile the test classes located in `src/test/java`.
- Execute the tests using **JUnit** and **Mockito**.
- Generate a test report in the `target/surefire-reports` directory.

---

## Common Issues and Solutions

### 1. Dependencies Not Found
If you encounter issues with missing dependencies in Eclipse, try the following:

```bash
mvn clean install -U
```

Then refresh your project in Eclipse:
- Right-click on your project > **Maven** > **Update Project**.

---

### 2. Cleaning the Project
To force a clean build:

```bash
mvn clean
mvn package
```

---

## Updating the Database
If you have SQL scripts in the `sql/` directory, you can run them manually in your MySQL database to set up the schema:

```bash
mysql -u root -p < sql/bookup_user.sql
```

---

## Summary
- **Compile the project**: `mvn clean package`
- **Deploy to Tomcat**: Copy the `.war` file or use `mvn tomcat9:deploy`
- **Run tests**: `mvn test`
- **Clean project**: `mvn clean install -U`
