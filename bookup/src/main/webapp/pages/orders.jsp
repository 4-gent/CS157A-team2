<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Orders</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/orders.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/global.css">
        <style>
        body {
            font-family: Arial, sans-serif;
        }
        h1, h2 {
            color: #4CAF50;
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
            font-weight: bold;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        tr:hover {
            background-color: #f1f1f1;
        }
        order-details{
        	margin: 20px;
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
	                <a href="${pageContext.request.contextPath}/pages/cart.jsp"><button class="nav-button">Your Cart</button></a>
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

				<!-- Admin-Only Update Status Form -->
				<c:if test="${sessionScope.isAdmin}">
				    <h2>Update Order Status</h2>
				    <form action="${pageContext.request.contextPath}/Orders" method="post">
				        <input type="hidden" name="action" value="updateStatus">
				        <input type="hidden" name="orderId" value="${order.orderID}">
				        <label for="status">Status:</label>
				        <select name="status" id="status" class="select-status" required>
				            <option value="Pending" ${order.orderStatus == 'Pending' ? 'selected' : ''}>Pending</option>
				            <option value="Shipped" ${order.orderStatus == 'Shipped' ? 'selected' : ''}>Shipped</option>
				            <option value="Delivered" ${order.orderStatus == 'Delivered' ? 'selected' : ''}>Delivered</option>
				            <option value="Cancelled" ${order.orderStatus == 'Cancelled' ? 'selected' : ''}>Cancelled</option>
				        </select>
				        <button type="submit" class="update-status-button">Update Status</button>
				    </form>
				</c:if>
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
			    </tr>
			    <c:forEach var="order" items="${orders}">
			        <tr>
			            <td>${order.orderID}</td>
			            <td>${order.username}</td>
			            <td>
			                <c:choose>
			                    <c:when test="${sessionScope.isAdmin}">
			                        <!-- Admin can update the status -->
								    <form action="${pageContext.request.contextPath}/Orders" method="post">
								        <input type="hidden" name="action" value="updateStatus">
								        <input type="hidden" name="orderId" value="${order.orderID}">
								        <select name="status" id="status" class="select-status" required>
								            <option value="Pending" ${order.orderStatus == 'Pending' ? 'selected' : ''}>Pending</option>
								            <option value="Shipped" ${order.orderStatus == 'Shipped' ? 'selected' : ''}>Shipped</option>
								            <option value="Delivered" ${order.orderStatus == 'Delivered' ? 'selected' : ''}>Delivered</option>
								            <option value="Cancelled" ${order.orderStatus == 'Cancelled' ? 'selected' : ''}>Cancelled</option>
								        </select>
								        <button type="submit" class="update-status-button">Update Status</button>
								    </form>
			                    </c:when>
			                    <c:otherwise>
			                        <!-- Non-admins can only view the status -->
			                        ${order.orderStatus}
			                    </c:otherwise>
			                </c:choose>
			            </td>
			            <td>$${order.total}</td>
			        </tr>
			    </c:forEach>
			</table>
        </c:if>
    </div>
</body>
</html>