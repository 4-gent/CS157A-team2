<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employee Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/profile.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/global.css">
</head>
<body class="employee-body">
    <!-- Navigation Bar -->
    <div class="nav">
        <a href="${pageContext.request.contextPath}/pages/inventory.jsp"><button class="nav-button">Inventory</button></a>
        <a href="${pageContext.request.contextPath}/pages/orders.jsp"><button class="nav-button">Orders</button></a>
        <a href="${pageContext.request.contextPath}/pages/customers.jsp"><button class="nav-button">Customers</button></a>
        <a href="${pageContext.request.contextPath}/index.jsp"><button class="nav-button">Log Out</button></a>
    </div>

    <!-- Employee Header Section -->
    <div class="employee-header">
        <div>
            <label>Employee User Name: ${user_info.username}</label>
        </div>
        <!-- Form for Updating Information -->
        <form action="${pageContext.request.contextPath}/User_Info" method="POST">
            <!-- Password Update -->
            <label for="newPassword">Update Password:</label>
            <input type="password" name="newPassword" id="newPassword" required><br>
            <!-- Delete Account (Confirm Deletion) -->
            <label for="deleteAccount">Delete Account:</label>
            <button type="submit" name="deleteAccount" value="true">Delete</button><br>
            <!-- Submit Button for Updates -->
            <button type="submit" name="updateProfile" value="true">Update Profile</button>
        </form>
    </div>

    <!-- User Profile Section -->
    <div class="profile-header">
        <div class="user-element">
            <!-- Display User Information -->
            <label>Email: ${user_info.email}</label><br>
        </div>
    </div>
</body>
</html>