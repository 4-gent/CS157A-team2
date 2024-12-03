<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Customer Details</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/profile.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/global.css">
</head>
<body>
    <!-- Navigation Bar -->
    <div class="nav">
        <a href="${pageContext.request.contextPath}/Books"><button class="nav-button">Books</button></a>
        <a href="${pageContext.request.contextPath}/pages/inventory.jsp"><button class="nav-button">Inventory</button></a>
        <a href="${pageContext.request.contextPath}/pages/orders.jsp"><button class="nav-button">Orders</button></a>
        <a href="${pageContext.request.contextPath}/pages/customers.jsp"><button class="nav-button">Customers</button></a>
        <a href="${pageContext.request.contextPath}/index.jsp"><button class="nav-button">Log Out</button></a>
    </div>
    <!-- Search Form -->
    <div class="search-bar">
        <h2>Search Customer by Username</h2>
        <form action="${pageContext.request.contextPath}/ViewCustomer" method="post">
            <input type="text" name="username" placeholder="Enter username" required>
            <button type="submit">Search</button>
        </form>
    </div>

    <!-- Customer Details Section -->
    <div class="customer-details-container">
        <h1>Customer Details</h1>
        <c:if test="${not empty userDetails}">
            <table border="1">
                <tr>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Favorite Author</th>
                    <th>Favorite Genre</th>
                </tr>
                <tr>
                    <td>${userDetails.username}</td>
                    <td>${userDetails.email}</td>
                    <td>${userDetails.phone}</td>
                    <td><c:out value="${favoriteAuthor != null ? favoriteAuthor.getName() : 'N/A'}"/></td>
                    <td><c:out value="${favoriteGenre != null ? favoriteGenre.getName() : 'N/A'}"/></td>
                </tr>
            </table>
        </c:if>
        <c:if test="${empty userDetails}">
            <p>No customer details found.</p>
        </c:if>
        <c:if test="${not empty param.error}">
            <p style="color: red;">${param.error}</p>
        </c:if>
    </div>
</body>
</html>