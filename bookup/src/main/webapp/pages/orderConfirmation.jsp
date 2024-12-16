<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bookie.models.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    // Retrieve the Order object
    Order order = (Order) request.getAttribute("order");
    Address shippingAddress = null;
    List<CartItem> items = null;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    if (order != null) {
        shippingAddress = order.getShippingAddress();
        items = order.getItems();
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Order Confirmation</title>
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
        .table-container {
            width: 100%;
            overflow-x: auto;
            margin: 20px 0;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            min-width: 800px;
            table-layout: auto;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: left;
            font-size: 1em;
        }
        th {
            background-color: #f2f2f2;
            font-weight: bold;
            color: #333;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        tr:hover {
            background-color: #f1f1f1;
        }
        .total {
            font-size: 1.2em;
            font-weight: bold;
            color: #4CAF50;
            text-align: right;
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

    <h1>Order Confirmation</h1>
    <%
        if (order == null) {
    %>
        <h2>Order details not available!</h2>
    <%
        } else {
    %>
    <p>Thank you for your purchase, <strong><%= order.getUsername() %></strong>!</p>

    <!-- Order Details -->
    <h2>Order Details</h2>
    <ul>
        <li><strong>Order ID:</strong> <%= order.getOrderID() %></li>
        <li><strong>Order Date:</strong> <%= dateFormat.format(order.getOrderDate()) %></li>
        <li><strong>Order Status:</strong> <%= order.getOrderStatus() %></li>
        <li><strong>Total Amount:</strong> $<%= String.format("%.2f", order.getTotal()) %></li>
    </ul>

    <!-- Shipping Address -->
    <h2>Shipping Address</h2>
    <p>
        <%= shippingAddress.getStreet() %>, <br>
        <%= shippingAddress.getCity() %>, <%= shippingAddress.getState() %>, <%= shippingAddress.getZip() %><br>
        <%= shippingAddress.getCountry() %>
    </p>

    <!-- Ordered Items -->
    <h2>Ordered Items</h2>
    <div class="table-container">
        <table>
            <thead>
                <tr>
                    <th style="width: 25%;">Book Title</th>
                    <th style="width: 20%;">Author</th>
                    <th style="width: 15%;">Genre</th>
                    <th style="width: 10%;">Quantity</th>
                    <th style="width: 15%;">Unit Price</th>
                    <th style="width: 15%;">Subtotal</th>
                </tr>
            </thead>
            <tbody>
                <%
                    double grandTotal = 0.0;
                    for (CartItem cartItem : items) {
                        InventoryItem inventoryItem = cartItem.getInventoryItem();
                        Book book = inventoryItem.getBook();
                        String author = (book.getAuthor() != null) ? book.getAuthor().getName() : "Unknown";
                        String genre = (book.getGenre() != null) ? book.getGenre().getName() : "Unknown";
                        double unitPrice = inventoryItem.getPrice();
                        int quantity = cartItem.getQuantity();
                        double subtotal = unitPrice * quantity;
                        grandTotal += subtotal;
                %>
                <tr>
                    <td><%= book.getTitle() %></td>
                    <td><%= author %></td>
                    <td><%= genre %></td>
                    <td><%= quantity %></td>
                    <td>$<%= String.format("%.2f", unitPrice) %></td>
                    <td>$<%= String.format("%.2f", subtotal) %></td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </div>

    <!-- Total -->
    <p class="total">Grand Total: $<%= String.format("%.2f", grandTotal) %></p>
    <%
        }
    %>
</body>
</html>