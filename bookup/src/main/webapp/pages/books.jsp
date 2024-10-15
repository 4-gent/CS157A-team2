<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.sql.*"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Books</title>
    <link rel="stylesheet" href="../styles/global.css">
</head>
<body class="books-body">
    <div>
        <div>
            <a href="/bookup/index.jsp"><button class="nav-button">Home</button></a>
            <a href="/bookup/pages/login.jsp"><button class="nav-button">Login</button></a>
            <a href="/bookup/pages/register.jsp"><button class="nav-button">Register</button></a>
        </div>
        <div class="books-header">
            <h1>Books</h1>
        </div>
        <div class="books-list">
            <table border="1" cellpadding="10" cellspacing="0">
                <tr>
                    <th>Name</th>
                    <th>Author</th>
                    <th>Genre</th>
                    <th>Price</th>
                </tr>
                <%
                String db = "bookup";
                String user = "root";
                String password = "Marlon1209@";
                try {
                    Connection con;
                    Class.forName("com.mysql.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookup?autoReconnect=true&useSSL=false", user, password);
                    
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM books");

                    while (rs.next()) {
                        out.println("<tr>");
                        out.println("<td>" + rs.getString("name") + "</td>");
                        out.println("<td>" + rs.getString("author") + "</td>");
                        /* out.println("<td>" + rs.getString("ISBN") + "</td>"); */
                        out.println("<td>" + rs.getString("genre") + "</td>");
                        out.println("<td>" + rs.getFloat("price") + "</td>");
                        out.println("</tr>");
                    }
                    rs.close();
                    stmt.close();
                    con.close();
                } catch(SQLException e) {
                    out.println("<tr><td colspan='5'>SQLException caught: " + e.getMessage() + "</td></tr>");
                }
                %>
            </table>
        </div>
    </div>
</body>
</html>
