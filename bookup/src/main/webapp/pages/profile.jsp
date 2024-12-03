<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/profile.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/global.css">
</head>
<body class="profile-body">
    <!-- Navigation Bar -->
    <c:choose>
        <c:when test="${sessionScope.isAdmin}">
            <div class="nav">
				<a href="${pageContext.request.contextPath}/Books"><button class="nav-button">Books</button></a>
                <a href="${pageContext.request.contextPath}/Inventory"><button class="nav-button">Inventory</button></a>
                <a href="${pageContext.request.contextPath}/pages/orders.jsp"><button class="nav-button">Orders</button></a>
                <a href="${pageContext.request.contextPath}/pages/customers.jsp"><button class="nav-button">Customers</button></a>
                <a href="${pageContext.request.contextPath}/index.jsp"><button class="nav-button">Log Out</button></a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="nav">
                <a href="${pageContext.request.contextPath}/Books"><button class="nav-button">Books</button></a>
                <a href="${pageContext.request.contextPath}/Orders"><button class="nav-button">Your Orders</button></a>
                <a href="${pageContext.request.contextPath}/index.jsp"><button class="nav-button">Log Out</button></a>
            </div>
        </c:otherwise>
    </c:choose>

    <!-- User Profile Section -->
    <div class="profile-header">
        <div class="user-element">
            <h2>User Profile</h2>
            <p><strong>Username:</strong> ${sessionScope.username}</p>
            <p><strong>Email:</strong> ${sessionScope.email}</p>
            <p><strong>Phone:</strong> ${sessionScope.phone}</p>
            <c:choose>
                <c:when test="${sessionScope.isAdmin}">
                    <p><strong>Role:</strong> Administrator</p>
                </c:when>
                <c:otherwise>
                    <p><strong>Role:</strong> Regular User</p>
                </c:otherwise>
            </c:choose>
            <p><strong>Favorite Author:</strong> ${sessionScope.favoriteAuthor}</p>
            <p><strong>Favorite Genre:</strong> ${sessionScope.favoriteGenre}</p>

            <!-- Form for Updating Information -->
            <form action="${pageContext.request.contextPath}/User_Info" method="POST">
                <label for="newPassword">Update Password:</label>
                <input type="password" name="newPassword" id="newPassword" required><br>
                <label for="favoriteAuthor">Update Favorite Author:</label>
                <input type="text" name="favoriteAuthor" id="favoriteAuthor" value="" required><br>
                <label for="favoriteGenre">Update Favorite Genre:</label>
                <input type="text" name="favoriteGenre" id="favoriteGenre" value="" required><br>
                <label for="deleteAccount">Delete Account:</label>
                <button type="submit" name="deleteAccount" value="true" onclick="return confirm('Are you sure you want to delete your account?');">Delete</button><br>
                <button type="submit" name="updateProfile" value="true">Update Profile</button>
            </form>
        </div>
    </div>
</body>
</html>
