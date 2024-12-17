<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Books</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/global.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/books.css" type="text/css">
</head>
<body class="books-body">
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
    <div class="books-header">
        <h1>Books</h1>
    </div>

    <!-- Search Form -->
    <form action="${pageContext.request.contextPath}/Books" method="post" class="search-container">
        <input placeholder="Search for..." class="search-input" type="text" name="search-input" value="${param['search-input']}"/>
        <button type="submit" class="search-button">Search</button>
    </form>

    <!-- Filter Form -->
    <form action="${pageContext.request.contextPath}/Books" method="post" class="filter-container">
        <select name="genre" id="genre" class="filter-dropdown">
            <option value="">Select Genre</option>
            <c:forEach var="g" items="${genres}">
                <option value="${g}" ${g == param.genre ? 'selected' : ''}>${g.name}</option>
            </c:forEach>
        </select>

        <select name="year" id="year" class="filter-dropdown">
            <option value="">Select Year</option>
            <c:forEach var="y" items="${years}">
                <option value="${y}" ${y == param.year ? 'selected' : ''}>${y}</option>
            </c:forEach>
        </select>

        <select name="author" id="author" class="filter-dropdown">
            <option value="">Select Author</option>
            <c:forEach var="a" items="${authors}">
                <option value="${a}" ${a == param.author ? 'selected' : ''}>${a.name}</option>
            </c:forEach>
        </select>

        <button type="submit" class="filter-button">Apply Filters</button>
    </form>
	
    <!-- Books List -->
    <div class="books-list">
        <c:choose>
            <c:when test="${not empty search_result}">
                <c:forEach var="book" items="${search_result}">
                    <div class="book-element" onclick="openModal('${book.title}', '${book.year}', '${book.ISBN}')">
                        <label>Title:</label>
                        <p>${book.title}</p>
                        <label>Year:</label>
                        <p>${book.year}</p>
                    </div>
                </c:forEach>
            </c:when>

            <c:when test="${not empty filter_result}">
                <c:forEach var="book" items="${filter_result}">
                    <div class="book-element" onclick="openModal('${book.title}', '${book.year}', '${book.ISBN}')">
                        <label>Title:</label>
                        <p>${book.title}</p>
                        <label>Year:</label>
                        <p>${book.year}</p>
                    </div>
                </c:forEach>
            </c:when>

            <c:otherwise>
                <!-- Show all books if no results from filters/search -->
                <c:forEach var="book" items="${bookList}">
                    <div class="book-element" onclick="openModal('${book.title}', '${book.year}', '${book.ISBN}')">
                        <label>Title:</label>
                        <p>${book.title}</p>
                        <label>Year:</label>
                        <p>${book.year}</p>
                        <!-- Add to Cart Button -->
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>

        <!-- "No books found" message -->
        <c:if test="${empty search_result && empty filter_result && empty bookList}">
            <p class="none-found">No books found. Try different filters or search terms.</p>
        </c:if>
    </div>

    <!-- Modal Structure -->
    <div id="bookModal" class="modal">
        <div class="modal-content">
            <span class="close-button" onclick="closeModal()">&times;</span>
            <h2 id="modalTitle">Book Title</h2>
            <p id="modalYear">Published Year</p>
            <p id="modalISBN">ISBN</p>
            <!-- Add to Cart Form -->
            <form action="${pageContext.request.contextPath}/Cart" method="POST">
                <input type="hidden" name="action" value="add">
                <input type="hidden" name="isbn" id="modalISBNInput">
                <label for="quantity">Quantity:</label>
                <input type="number" id="quantity" name="quantity" value="1" min="1" required>
                <button type="submit" class="add-to-cart">Add to Cart</button>
            </form>
            
        </div>
    </div>

    <script>
        // Function to open the modal with book details
        function openModal(title, year, isbn) {
            document.getElementById("modalTitle").textContent = title;
            document.getElementById("modalYear").textContent = "Published in: " + year;
            document.getElementById("modalISBN").textContent = "ISBN: " + isbn;
            document.getElementById("modalISBNInput").value = isbn; // Populate the hidden input field

            document.getElementById("bookModal").style.display = "block";
        }

        // Function to close the modal
        function closeModal() {
            document.getElementById("bookModal").style.display = "none";
        }

        // Close the modal if the user clicks anywhere outside the modal content
        window.onclick = function(event) {
            var modal = document.getElementById("bookModal");
            if (event.target == modal) {
                closeModal();
            }
        }
        
    </script>
</body>
</html>