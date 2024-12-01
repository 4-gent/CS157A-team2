<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/profile.css">
    <link rel="stylesheet" href="../styles/global.css">
</head>
<body class="profile-body">
    <!-- Navigation Bar -->
    <div class="nav">
        <a href="/bookup/Books"><button class="nav-button">Books</button></a>
        <a href="/bookup/index.jsp"><button class="nav-button">Log Out</button></a>
         <a href="/bookup/pages/cart.jsp"><button class="nav-button">Checkout</button></a>
    </div>

    <!-- User Profile Section -->
    <div class="profile-header">
        <div class="user-element">
            <!-- Display User Information -->
            <label>Username: ${user_info.username}</label><br>
            <label>Email: ${user_info.email}</label><br>

            <!-- Form for Updating Information -->
            <form action="/bookup/User_Info" method="POST">
                <!-- Password Update -->
                <label for="newPassword">Update Password:</label>
                <input type="password" name="newPassword" id="newPassword" required><br>

                <!-- Favorite Author Update -->
                <label for="favoriteAuthor">Update Favorite Author:</label>
                <input type="text" name="favoriteAuthor" id="favoriteAuthor" value="${user_info.favoriteAuthorID}" required><br>

                <!-- Favorite Genre Update -->
                <label for="favoriteGenre">Update Favorite Genre:</label>
                <input type="text" name="favoriteGenre" id="favoriteGenre" value="${user_info.favoriteGenreID}" required><br>

                <!-- Delete Account (Confirm Deletion) -->
                <label for="deleteAccount">Delete Account:</label>
                <button type="submit" name="deleteAccount" value="true">Delete</button><br>

                <!-- Submit Button for Updates -->
                <button type="submit" name="updateProfile" value="true">Update Profile</button>
            </form>
        </div>
    </div>
</body>
</html>
