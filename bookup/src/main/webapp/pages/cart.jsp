<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Shopping Cart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/global.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
        }
        h1, h2 {
            color: #4CAF50;
        }
        .cart-container {
            width: 90%;
            margin: 20px auto;
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
            color: #333;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        tr:hover {
            background-color: #f1f1f1;
        }
        .cart-summary, .payment-details {
            margin: 20px 0;
            padding: 20px;
            border: 1px solid #ddd;
            background-color: #fff;
            border-radius: 5px;
        }
        .checkout-button, .remove-button, .empty-cart-button {
            padding: 10px 15px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
            border-radius: 5px;
        }
        .checkout-button:hover, .remove-button:hover, .empty-cart-button:hover {
            background-color: #45a049;
        }
        .none-found {
            text-align: center;
            font-size: 1.2em;
            color: #666;
        }
    </style>
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

    <div class="cart-container">
        <h1>Shopping Cart</h1>

        <!-- Display Cart Items -->
        <c:if test="${not empty cart.cartItems}">
            <table>
                <thead>
                    <tr>
                        <th>Title</th>
                        <th>Quantity</th>
                        <th>Price</th>
                        <th>Total</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${cart.cartItems}">
                        <tr>
                            <td>${item.inventoryItem.book.title}</td>
                            <td>${item.quantity}</td>
                            <td>$${item.inventoryItem.price}</td>
                            <td>$${item.quantity * item.inventoryItem.price}</td>
                            <td>
                                <!-- Remove Item -->
                                <form action="${pageContext.request.contextPath}/Cart" method="POST" style="display:inline;">
                                    <input type="hidden" name="itemId" value="${item.inventoryItem.inventoryItemID}">
                                    <input type="hidden" name="action" value="remove">
                                    <button type="submit" class="remove-button">Remove</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>

        <!-- Empty Cart Message -->
        <c:if test="${empty cart.cartItems}">
            <p class="none-found">Your cart is empty. Add some books!</p>
        </c:if>

        <!-- Cart Summary -->
        <c:if test="${not empty cart.cartItems}">
            <div class="cart-summary">
                <h2>Cart Summary</h2>
                <p><strong>Total Items:</strong> ${fn:length(cart.cartItems)}</p>
                <p><strong>Total Cost:</strong> $${cart.total}</p>
                
                <!-- Empty Cart -->
                <form action="${pageContext.request.contextPath}/Cart" method="POST">
                    <input type="hidden" name="action" value="empty">
                    <button type="submit" class="empty-cart-button">Empty Cart</button>
                </form>
            </div>
        </c:if>

        <!-- Payment Details Section -->
        <c:if test="${not empty cart.cartItems}">
            <div class="payment-details">
                <h2>Payment Details</h2>
                <form action="${pageContext.request.contextPath}/Cart" method="POST">
                    <input type="hidden" name="action" value="checkout">
                    <label for="addressID">Address ID:</label>
                    <input type="number" id="addressID" name="addressID" required>
                    <label for="paymentDetailsID">Payment Details ID:</label>
                    <input type="number" id="paymentDetailsID" name="paymentDetailsID" required>
                    <button type="submit" class="checkout-button">Checkout</button>
                </form>
            </div>
        </c:if>
    </div>
</body>
</html>