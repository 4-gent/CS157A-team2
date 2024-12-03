<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Orders</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/orders.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/global.css">
</head>
<body>
    <!-- Navigation Bar -->
    <c:choose>
        <c:when test="${sessionScope.isAdmin}">
            <div class="nav">
                <a href="${pageContext.request.contextPath}/Books"><button class="nav-button">Books</button></a>
                <a href="${pageContext.request.contextPath}/Inventory"><button class="nav-button">Inventory</button></a>
                <a href="${pageContext.request.contextPath}/Orders"><button class="nav-button">Orders</button></a>
                <a href="${pageContext.request.contextPath}/Customers"><button class="nav-button">Customers</button></a>
                <a href="${pageContext.request.contextPath}/User_Info"><button class="nav-button">Profile</button></a>
                <a href="${pageContext.request.contextPath}/index.jsp"><button class="nav-button">Log Out</button></a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="nav">
                <a href="${pageContext.request.contextPath}/Books"><button class="nav-button">Books</button></a>
                <a href="${pageContext.request.contextPath}/Orders"><button class="nav-button">Your Orders</button></a>
                <a href="${pageContext.request.contextPath}/Cart"><button class="nav-button">Your Cart</button></a>
                <a href="${pageContext.request.contextPath}/User_Info"><button class="nav-button">Profile</button></a>
                <a href="${pageContext.request.contextPath}/index.jsp"><button class="nav-button">Log Out</button></a>
            </div>
        </c:otherwise>
    </c:choose>

    <div class="orders-container">
        <!-- Admin-Only Search Form -->
        <c:if test="${sessionScope.isAdmin}">
            <div class="search-bar">
                <h2>Search Orders by Username</h2>
                <form action="${pageContext.request.contextPath}/Orders" method="post">
                    <input type="text" name="username" placeholder="Enter username" value="${param.username}" required>
                    <button type="submit">Search</button>
                </form>
            </div>
        </c:if>

        <!-- Check if a specific order is being displayed -->
        <c:if test="${not empty order}">
            <h1>Order Details</h1>
            <div class="order-details">
                <p><strong>Order ID:</strong> ${order.orderID}</p>
                <p><strong>Status:</strong> ${order.orderStatus}</p>
                <p><strong>Total:</strong> $${order.total}</p>
                <h2>Items:</h2>
                <ul>
                    <c:forEach var="item" items="${order.items}">
                        <li>${item.book.title} - ${item.quantity} x $${item.price}</li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <!-- Display all orders -->
        <c:if test="${empty order}">
            <h1 class="orders-header">All Orders</h1>
            <table border="1">
                <tr>
                    <th>Order ID</th>
                    <th>Username</th>
                    <th>Status</th>
                    <th>Total</th>
                    <th>Actions</th>
                </tr>
                <c:forEach var="order" items="${orders}">
                    <tr>
                        <td>${order.orderID}</td>
                        <td>${order.username}</td>
                        <td>${order.orderStatus}</td>
                        <td>$${order.total}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/Orders?orderId=${order.orderID}">View Details</a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
    </div>
</body>
</html>