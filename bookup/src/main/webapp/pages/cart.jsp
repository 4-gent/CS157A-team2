<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Shopping Cart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/global.css">
</head>
<body>
    <div class="cart-container">
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
    
        <h1>Shopping Cart</h1>

        <!-- Display Cart Items -->
        <div class="cart-items">
            <c:if test="${not empty cart.cartItems}">
                <c:forEach var="item" items="${cart.cartItems}">
                    <div class="cart-item">
                        <p><strong>Title:</strong> ${item.inventoryItem.book.title}</p>
                        <p><strong>Quantity:</strong> ${item.quantity}</p>
                        <p><strong>Price:</strong> $${item.inventoryItem.price}</p>
                        <p><strong>Total:</strong> $${item.quantity * item.inventoryItem.price}</p>
                        
                        <!-- Remove Item -->
                        <form action="${pageContext.request.contextPath}/Cart" method="POST">
                            <input type="hidden" name="itemId" value="${item.inventoryItem.inventoryItemID}">
                            <input type="hidden" name="action" value="remove">
                            <button type="submit" class="remove-button">Remove</button>
                        </form>
                    </div>
                </c:forEach>
            </c:if>

            <!-- Empty Cart Message -->
            <c:if test="${empty cart.cartItems}">
                <p class="none-found">Your cart is empty. Add some books!</p>
            </c:if>
        </div>

        <!-- Cart Summary -->
        <c:if test="${not empty cart.cartItems}">
            <div class="cart-summary">
                <h2>Cart Summary</h2>
                <p><strong>Total Items:</strong> ${fn:length(cart.cartItems)}</p>
                <p><strong>Total Cost:</strong> $${cart.totalCost}</p>
                
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