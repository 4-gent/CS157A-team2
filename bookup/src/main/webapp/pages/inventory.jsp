<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inventory Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/global.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 0;
        }

        /* Navigation Bar */
        .nav {
            background-color: #333;
            padding: 10px 0;
            text-align: center;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .nav .brand {
            font-size: 1.8rem;
            color: #4CAF50;
            padding-left: 20px;
            font-family: 'Sora', sans-serif;
        }
        .nav a {
            color: white;
            text-decoration: none;
            padding: 10px 20px;
        }
        .nav a:hover {
            background-color: #575757;
        }

        .inventory-container {
            width: 95%;
            margin: 20px auto;
        }
        h1 {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .add-button {
            padding: 10px 15px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
            border-radius: 5px;
            text-align: center;
        }
        .add-button:hover {
            background-color: #45a049;
        }

        /* Table Styles */
		.scrollable-table {
		    overflow-y: auto; /* Enable vertical scrolling */
		    max-height: 650px; /* Double the table height (originally 400px) */
		    border: 1px solid #ddd;
		    background-color: #fff;
		}
		
		.scrollable-table table {
		    width: 100%;
		    border-collapse: collapse;
		}
		
		.scrollable-table th {
		    position: sticky; /* Keeps headers fixed during scroll */
		    top: 0;
		    background-color: #f4f4f4;
		    z-index: 10; /* Ensures headers appear above table rows */
		}
		
		th, td {
		    border: 1px solid #ddd;
		    padding: 12px;
		    text-align: center;
		}
		
		tr:hover {
		    background-color: #f1f1f1;
		}
        .inventory-button {
            width: 80px; /* Fixed width for consistency */
            padding: 6px 12px;
            margin: 2px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-align: center;
            display: inline-block;
        }
        .delete-button {
            background-color: #e74c3c;
            color: white;
        }
        .view-button {
            background-color: #3498db;
            color: white;
        }

        /* Modal Styles */
        .modal {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
        }
        .modal-content {
            background-color: #fff;
            margin: 10% auto;
            padding: 20px;
            width: 50%;
            border-radius: 10px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
        }
        .modal-content h2 {
            margin-top: 0;
        }
        .close-button {
            color: #aaa;
            float: right;
            font-size: 24px;
            cursor: pointer;
        }
        .close-button:hover {
            color: black;
        }
        .modal-form input {
            width: 75%;
            padding: 8px;
            margin: 10px auto;
            display: block;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .modal-form .modal-btn-container {
            text-align: center; /* Center align the button */
            margin-top: 10px;
        }
        .modal-form button {
            width: 50%; /* Button centered and shorter */
            padding: 10px 0;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .modal-form button:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
    <!-- Navigation Bar -->
    <div class="nav">
        <div class="brand">Bookie</div>
        <div>
            <a href="${pageContext.request.contextPath}/Books">Books</a>
            <a href="${pageContext.request.contextPath}/Inventory">Inventory</a>
            <a href="${pageContext.request.contextPath}/Orders">Orders</a>
            <a href="${pageContext.request.contextPath}/Customers">Customers</a>
            <a href="${pageContext.request.contextPath}/User_Info">Profile</a>
            <a href="${pageContext.request.contextPath}/index.jsp">Log Out</a>
        </div>
    </div>

    <div class="inventory-container">
        <h1>
            Inventory Management
            <button class="add-button" onclick="openModal()">Add Inventory</button>
        </h1>

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
                        <tr>
                            <td>${details.inventoryItem.inventoryItemID}</td>
                            <td>${details.book.ISBN}</td>
                            <td>${details.book.title}</td>
                            <td>${details.author.name}</td>
                            <td>${details.book.year}</td>
                            <td>${details.book.publisher}</td>
                            <td>$${details.inventoryItem.price}</td>
                            <td>${details.inventoryItem.qty}</td>
                            <td>${details.inventoryItem.description}</td>
                            <td>
                                <button class="inventory-button view-button" onclick="openModal(
                                    '${details.inventoryItem.inventoryItemID}', '${details.book.ISBN}', '${details.book.title}',
                                    '${details.author.name}', '${details.book.year}', '${details.book.publisher}',
                                    '${details.inventoryItem.price}', '${details.inventoryItem.qty}', '${details.inventoryItem.description}')">
                                    View
                                </button>
                                <form action="${pageContext.request.contextPath}/Inventory" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="itemId" value="${details.inventoryItem.inventoryItemID}">
                                    <button class="inventory-button delete-button" type="submit">Delete</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Modal for View/Add Inventory -->
    <div id="inventoryModal" class="modal">
        <div class="modal-content">
            <span class="close-button" onclick="closeModal()">&times;</span>
            <h2 id="modalTitle">Add / Update Inventory</h2>
            <form class="modal-form" action="${pageContext.request.contextPath}/Inventory" method="post">
                <input type="hidden" name="action" id="modalAction" value="add">
                <input type="hidden" name="itemId" id="itemId">
                <input type="text" name="isbn" id="isbn" placeholder="ISBN" required>
                <input type="text" name="title" id="title" placeholder="Title" required>
                <input type="text" name="author" id="author" placeholder="Author" required>
                <input type="number" name="year" id="year" placeholder="Year" required>
                <input type="text" name="publisher" id="publisher" placeholder="Publisher" required>
                <input type="number" step="0.01" name="price" id="price" placeholder="Price" required>
                <input type="number" name="quantity" id="quantity" placeholder="Quantity" required>
                <input type="text" name="description" id="description" placeholder="Description" required>
                <div class="modal-btn-container">
                    <button type="submit">Save</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function openModal(itemId = '', isbn = '', title = '', author = '', year = '', publisher = '', price = '', quantity = '', description = '') {
            document.getElementById("itemId").value = itemId;
            document.getElementById("isbn").value = isbn;
            document.getElementById("title").value = title;
            document.getElementById("author").value = author;
            document.getElementById("year").value = year;
            document.getElementById("publisher").value = publisher;
            document.getElementById("price").value = price;
            document.getElementById("quantity").value = quantity;
            document.getElementById("description").value = description;
            document.getElementById("modalAction").value = itemId ? 'update' : 'add';
            document.getElementById("modalTitle").textContent = itemId ? 'Update Inventory' : 'Add Inventory';
            document.getElementById("inventoryModal").style.display = "block";
        }

        function closeModal() {
            document.getElementById("inventoryModal").style.display = "none";
        }
    </script>
</body>
</html>