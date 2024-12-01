<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Shopping Cart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cart.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/global.css">
</head>
<body class="cart-body">
    <!-- Navigation Bar -->
    <div class="nav">
        <a href="/bookup/Books"><button class="nav-button">Books</button></a>
        <a href="/bookup/index.jsp"><button class="nav-button">Log Out</button></a>
        <a href="/bookup/profile.jsp"><button class="nav-button">Profile</button></a>
    </div>

    <!-- Cart Section -->
    <div class="cart-container">
        <h1>Shopping Cart</h1>

        <!-- Display Cart Items -->
        <div class="cart-items">
            <c:if test="${not empty cartItems}">
                <c:forEach var="item" items="${cartItems}">
                    <div class="cart-item">
                        <p><strong>Title:</strong> ${item.title}</p>
                        <p><strong>Quantity:</strong> ${item.quantity}</p>
                        <p><strong>Price:</strong> $${item.price}</p>
                        <p><strong>Total:</strong> $${item.quantity * item.price}</p>
                        
                        <!-- Remove Item -->
                        <form action="/bookup/Cart" method="POST">
                            <input type="hidden" name="bookId" value="${item.id}">
                            <button type="submit" name="action" value="remove" class="remove-button">Remove</button>
                        </form>
                    </div>
                </c:forEach>
            </c:if>

            <!-- Empty Cart Message -->
            <c:if test="${empty cartItems}">
                <p class="none-found">Your cart is empty. Add some books!</p>
            </c:if>
        </div>

        <!-- Cart Summary -->
        <c:if test="${not empty cartItems}">
            <div class="cart-summary">
                <h2>Cart Summary</h2>
                <p><strong>Total Items:</strong> ${cartItems.size()}</p>
                <p><strong>Total Cost:</strong> $${totalCost}</p>
                
                <!-- Proceed to Checkout -->
                <form action="/bookup/Checkout" method="POST">
                    <button type="submit" class="checkout-button">Proceed to Checkout</button>
                </form>
            </div>
        </c:if>
    </div>
</body>
</html>
