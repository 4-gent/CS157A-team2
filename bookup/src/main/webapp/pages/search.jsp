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
        <form action="${pageContext.request.contextPath}/Search" method="post" class="search-container">
            <input placeholder="Search for..." class="search-input" type="text" name="search-input" required/>
            <button type="submit" class="search-button">Search</button>
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
                <p>No books found. Try a different search term.</p>
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
