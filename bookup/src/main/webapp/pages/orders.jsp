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
            margin: 0;
            background-color: #f9f9f9;
        }
        h1 {
            color: #4CAF50;
            text-align: center;
            margin: 20px 0;
        }
        .nav {
            background-color: #333;
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
            width: 90%;
            margin: 20px auto;
            border-collapse: collapse;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        tr:hover {
            background-color: #f1f1f1;
        }
        .no-orders {
            text-align: center;
            color: #888;
            margin: 20px 0;
        }
    </style>
</head>
<body>
    <!-- Navigation Bar -->
    <div class="nav">
        <a href="${pageContext.request.contextPath}/Books">Books</a>
        <a href="${pageContext.request.contextPath}/Inventory">Inventory</a>
        <a href="${pageContext.request.contextPath}/Orders">Orders</a>
        <a href="${pageContext.request.contextPath}/Customers">Customers</a>
        <a href="${pageContext.request.contextPath}/User_Info">Profile</a>
        <a href="${pageContext.request.contextPath}/index.jsp">Log Out</a>
    </div>

    <!-- Orders Table -->
    <h1>Orders</h1>
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
                        <a href="${pageContext.request.contextPath}/Orders?orderId=${order.orderID}">
						    View Details
						</a>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty orders}">
                <tr>
                    <td colspan="6" class="no-orders">
                        <c:choose>
                            <c:when test="${isAdmin}">
                                No orders found in the system.
                            </c:when>
                            <c:otherwise>
                                You have not placed any orders yet.
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:if>
        </tbody>
    </table>
</body>
</html>