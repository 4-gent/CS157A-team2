<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inventory Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/inventory.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/global.css">
    <style>
        .scrollable-table {
            height: 300px;
            overflow-y: auto;
            border: 1px solid #ccc;
            margin: 20px 0;
        }
        .scrollable-table table {
            width: 100%;
            border-collapse: collapse;
        }
        .scrollable-table th, .scrollable-table td {
            border: 1px solid #ccc;
            padding: 8px;
            text-align: left;
        }
        .scrollable-table th {
            background-color: #f4f4f4;
        }
        .none-found {
            color: red;
            font-weight: bold;
            text-align: center;
            margin-top: 20px;
        }
        .clickable-row {
            cursor: pointer;
            transition: background-color 0.2s;
        }
        .clickable-row:hover {
            background-color: #f1f1f1;
        }
    </style>
</head>
<body class="inventory-body">
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

    <div class="inventory-container">
        <h1>Inventory Management</h1>

		<!-- Inventory Items Table -->
        <div class="scrollable-table">
            <table>
                <thead>
                    <tr>
                        <th>Item ID</th>
                        <th>ISBN</th>
                        <th>Title</th>
                        <th>Author</th>
                        <th>Year</th>
                        <th>Publisher</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th>Description</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="details" items="${inventoryDetails}">
                        <tr class="clickable-row">
                            <td>${details.inventoryItem.inventoryItemID}</td>
                            <td>${details.book.ISBN}</td>
                            <td>${details.book.title}</td>
                            <td>${details.author != null ? details.author.name : 'N/A'}</td>
                            <td>${details.book.year}</td>
                            <td>${details.book.publisher}</td>
                            <td>$${details.inventoryItem.price}</td>
                            <td>${details.inventoryItem.qty}</td>
                            <td>${details.inventoryItem.description}</td>
                            <td>
                                <c:if test="${sessionScope.isAdmin}">
                                    <!-- Delete Inventory Item Form -->
                                    <form action="${pageContext.request.contextPath}/Inventory" method="post" style="display:inline;">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="itemId" value="${details.inventoryItem.inventoryItemID}">
                                        <button class="inventory-button" type="submit">Delete</button>
                                    </form>
                                    <!-- Update Inventory Item Form -->
                                    <form action="${pageContext.request.contextPath}/Inventory" method="post" style="display:inline;">
                                        <input type="hidden" name="action" value="update">
                                        <input type="hidden" name="itemId" value="${details.inventoryItem.inventoryItemID}">
                                        <input type="number" step="0.01" name="price" placeholder="Price" value="${details.inventoryItem.price}" required>
                                        <input type="number" name="quantity" placeholder="Quantity" value="${details.inventoryItem.qty}" required>
                                        <input type="text" name="description" placeholder="Description" value="${details.inventoryItem.description}" required>
                                        <button type="submit" class="inventory-button">Update</button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <c:if test="${empty inventoryDetails}">
                <div class="none-found">No inventory items found.</div>
            </c:if>
        </div>

        <!-- Add Inventory Item Form -->
        <c:if test="${sessionScope.isAdmin}">
		    <div class="add-item-form">
		        <h2>Add Inventory Item</h2>
		        <form action="${pageContext.request.contextPath}/Inventory" method="post">
		            <input type="hidden" name="action" value="add">
		            <input type="text" name="isbn" placeholder="ISBN" required>
		            <input type="text" name="title" placeholder="Title" required>
		            <input type="number" name="year" placeholder="Year" required>
		            <input type="text" name="publisher" placeholder="Publisher" required>
		            <input type="number" step="0.01" name="price" placeholder="Price" required>
		            <input type="number" name="quantity" placeholder="Quantity" required>
		            <input type="text" name="description" placeholder="Description" required>
		            <button type="submit">Add Item</button>
		        </form>
		    </div>
		</c:if>

    </div>
</body>
</html>