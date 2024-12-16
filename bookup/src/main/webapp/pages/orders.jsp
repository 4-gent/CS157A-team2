<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Orders</title>
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
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        tr:hover {
            background-color: #f1f1f1;
        }
        .orders-container {
            margin: 20px auto;
            width: 90%;
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

    <div class="orders-container">
        <h1 class="orders-header">All Orders</h1>
        <table>
            <thead>
                <tr>
                    <th>Order ID</th>
                    <th>Username</th>
                    <th>Status</th>
                    <th>Order Date</th>
                    <th>Total</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="order" items="${orders}">
                    <tr>
                        <td>${order.orderID}</td>
                        <td>${order.username}</td>
                        <td>${order.orderStatus}</td>
                        <td>${order.orderDate}</td>
                        <td>$${order.total}</td>
                        <td>
                            <!-- Link to view order details -->
                            <a href="${pageContext.request.contextPath}/Orders?orderId=${order.orderID}">
                                View Details
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>