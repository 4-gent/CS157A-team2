<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Books</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/books.css">
</head>
<body class="books-body">
    <div>
        <div class="nav">
            <a href="${pageContext.request.contextPath}/index.jsp"><button class="nav-button">Home</button></a>
        </div>
        <div class="books-header">
            <h1 class="books-header">Books</h1>
        </div>
        
		<!-- Filter Form -->
		<form action="${pageContext.request.contextPath}/Filter" method="post" class="filter-container">
		    <!-- Genre Dropdown -->
		    <label for="genre">Genre:</label>
		    <select name="genre" id="genre" class="filter-dropdown">
		        <option value="">Select Genre</option>
		        <c:forEach var="g" items="${genres}">
		            <option value="${g}">${g}</option>
		        </c:forEach>
		    </select>
		
		    <!-- Availability Dropdown -->
		    <label for="availability">Availability:</label>
		    <select name="availability" id="availability" class="filter-dropdown">
		        <option value="">Select Availability</option>
		        <option value="available">Available</option>
		        <option value="unavailable">Unavailable</option>
		    </select>
		
		    <!-- Publisher Dropdown -->
		    <label for="publisher">Publisher:</label>
		    <select name="publisher" id="publisher" class="filter-dropdown">
		        <option value="">Select Publisher</option>
		        <c:forEach var="p" items="${publishers}">
		            <option value="${p}">${p}</option>
		        </c:forEach>
		    </select>
		
		    <!-- Publication Year Dropdown -->
		    <label for="year">Publication Year:</label>
		    <select name="year" id="year" class="filter-dropdown">
		        <option value="">Select Year</option>
		        <c:forEach var="y" items="${years}">
		            <option value="${y}">${y}</option>
		        </c:forEach>
		    </select>
		
		    <!-- Author Dropdown -->
		    <label for="author">Author:</label>
		    <select name="author" id="author" class="filter-dropdown">
		        <option value="">Select Author</option>
		        <c:forEach var="a" items="${authors}">
		            <option value="${a}">${a}</option>
		        </c:forEach>
		    </select>
		
		    <!-- Submit Button -->
		    <button type="submit" class="filter-button">Filter</button>
		</form>


        <!-- Books List -->
        <div class="books-list">
            <c:choose>
                <c:when test="${not empty search_result}">
                    <c:forEach var="book" items="${search_result}">
                        <div class="book-element" onclick="openModal('${book.title}', '${book.year}', '${book.ISBN}')">
                            <label>Title</label>
                            <p>${book.title}</p>
                            <label>Year</label>
                            <p>${book.year}</p>
                        </div>
                    </c:forEach>
                </c:when>

                <c:otherwise>
                    <p class="none-found">No books found. Try different filters.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- Modal Structure -->
    <div id="bookModal" class="modal">
        <div class="modal-content">
            <span class="close-button" onclick="closeModal()">&times;</span>
            <h2 id="modalTitle">Book Title</h2>
            <p id="modalYear">Published Year</p>
            <p id="modalISBN">ISBN</p>
        </div>
    </div>

    <script>
        // Function to open the modal with book details
        function openModal(title, year, isbn) {
            document.getElementById("modalTitle").textContent = title;
            document.getElementById("modalYear").textContent = "Published in: " + year;
            document.getElementById("modalISBN").textContent = "ISBN: " + isbn;
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
