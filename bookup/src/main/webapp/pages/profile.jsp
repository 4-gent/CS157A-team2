<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bookie - Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/global.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f9f9f9;
        }
        h1, h2 {
            color: #4CAF50;
        }
        .nav {
            background-color: #333;
            overflow: hidden;
            padding: 10px 0;
            text-align: center;
        }
        .nav a {
            color: white;
            text-decoration: none;
            padding: 10px 20px;
            display: inline-block;
        }
        .nav a:hover {
            background-color: #575757;
        }
        .profile-container {
            margin: 20px auto;
            width: 90%;
            background-color: #fff;
            border: 1px solid #ddd;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
            font-weight: bold;
        }
        .profile-section {
            margin-bottom: 20px;
        }
        label {
            font-weight: bold;
            display: block;
            margin: 10px 0 5px;
        }
        input[type="text"], input[type="password"], button {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ddd;
            border-radius: 5px;
            box-sizing: border-box;
        }
        button {
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
        }
        button:hover {
            background-color: #45a049;
        }
        .delete-button {
            background-color: #d9534f;
        }
        .delete-button:hover {
            background-color: #c9302c;
        }
        .form-actions {
            display: flex;
            justify-content: center;
            gap: 10px;
        }
        .profile-header {
            text-align: center;
            margin-bottom: 2rem;
        }
        .no-data {
            text-align: center;
            font-style: italic;
            color: #777;
        }
    </style>
</head>
<body>
    <!-- Navigation Bar -->
    <c:choose>
        <c:when test="${sessionScope.isAdmin}">
            <div class="nav">
                <a href="${pageContext.request.contextPath}/Books">Books</a>
                <a href="${pageContext.request.contextPath}/Inventory">Inventory</a>
                <a href="${pageContext.request.contextPath}/Orders">Orders</a>
                <a href="${pageContext.request.contextPath}/Customers">Customers</a>
                <a href="${pageContext.request.contextPath}/User_Info">Profile</a>
                <a href="${pageContext.request.contextPath}/index.jsp">Log Out</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="nav">
                <a href="${pageContext.request.contextPath}/Books">Books</a>
                <a href="${pageContext.request.contextPath}/Orders">Your Orders</a>
                <a href="${pageContext.request.contextPath}/Cart">Your Cart</a>
                <a href="${pageContext.request.contextPath}/User_Info">Profile</a>
                <a href="${pageContext.request.contextPath}/index.jsp">Log Out</a>
            </div>
        </c:otherwise>
    </c:choose>

    <!-- Profile Container -->
    <div class="profile-container">
        <div class="profile-header">
            <h1>User Profile</h1>
        </div>

        <!-- User Information -->
        <div class="profile-section">
            <h2>Personal Information</h2>
            <p><strong>Username:</strong> ${sessionScope.username}</p>
            <p><strong>Email:</strong> ${sessionScope.email}</p>
            <p><strong>Phone:</strong> ${sessionScope.phone}</p>
            <p><strong>Role:</strong> ${sessionScope.isAdmin ? 'Administrator' : 'Regular User'}</p>
            <p><strong>Favorite Author:</strong> ${sessionScope.favoriteAuthor}</p>
            <p><strong>Favorite Genre:</strong> ${sessionScope.favoriteGenre}</p>
        </div>

        <!-- Update Profile Form -->
        <div class="profile-section">
            <h2>Update Profile</h2>
            <form action="${pageContext.request.contextPath}/User_Info" method="POST">
                <label for="newPassword">Update Password:</label>
                <input type="password" name="newPassword" id="newPassword">

                <label for="favoriteAuthor">Favorite Author:</label>
                <input type="text" name="favoriteAuthor" id="favoriteAuthor">

                <label for="favoriteGenre">Favorite Genre:</label>
                <input type="text" name="favoriteGenre" id="favoriteGenre">

                <div class="form-actions">
                    <button type="submit" name="updateProfile" value="true">Update Profile</button>
                    <button type="submit" name="deleteAccount" value="true" class="delete-button" onclick="return confirm('Are you sure you want to delete your account?');">Delete Account</button>
                </div>
            </form>
        </div>

        <!-- Payment Information Section -->
        <div class="profile-section">
            <h2>Payment Information</h2>
            <c:if test="${not empty sessionScope.paymentDetails}">
                <table>
                    <thead>
                        <tr>
                            <th>Card Holder</th>
                            <th>Card Number</th>
                            <th>Expiration</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="paymentInfo" items="${sessionScope.paymentDetails}">
                            <tr>
                                <td>${paymentInfo.cardHolderName}</td>
                                <td>${paymentInfo.cardNumber}</td>
                                <td>${paymentInfo.monthYear}</td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/Payment" method="POST" style="display:inline;">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="paymentId" value="${paymentInfo.paymentID}">
                                        <button type="submit" class="delete-button">Delete</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
            <c:if test="${empty sessionScope.paymentDetails}">
                <p class="no-data">No payment information found.</p>
            </c:if>
        </div>
    </div>
</body>
</html>