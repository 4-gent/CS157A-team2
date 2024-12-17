<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Search Books</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/global.css">
    <link href="https://fonts.googleapis.com/css2?family=Sora:wght@600&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f9f9f9;
        }
        .nav {
            background-color: #333;
            overflow: hidden;
            padding: 10px 0;
            text-align: center;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }
        .nav .brand {
            font-family: 'Sora', sans-serif;
            font-size: 1.8rem;
            color: #4CAF50;
            padding-left: 20px;
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
        .search-container {
            margin: 30px auto;
            text-align: center;
        }
        .search-input {
            width: 50%;
            padding: 14px;
            font-size: 1rem;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        .search-button {
            padding: 14px 20px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
            border-radius: 5px;
            margin-left: 10px;
        }
        .search-button:hover {
            background-color: #45a049;
        }
        .books-list {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            justify-content: center;
            margin: 30px auto;
        }
        .book-element {
            background-color: white;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            padding: 20px;
            width: 250px;
            text-align: left;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            cursor: pointer;
        }
        .book-element:hover {
            transform: translateY(-5px);
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
        }
        .book-element label {
            font-weight: bold;
            color: #4CAF50;
        }
        .book-element p {
            margin: 5px 0;
            color: #555;
        }
        .none-found {
            text-align: center;
            color: #777;
            font-size: 1.2em;
        }
        .modal {
            display: none;
            position: fixed;
            z-index: 1;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0, 0, 0, 0.5);
        }
        .modal-content {
            background-color: #fff;
            margin: 10% auto;
            padding: 20px;
            border: 1px solid #888;
            width: 50%;
            border-radius: 10px;
        }
        .close-button {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
        }
        .close-button:hover,
        .close-button:focus {
            color: black;
            text-decoration: none;
            cursor: pointer;
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
            <a href="${pageContext.request.contextPath}/Cart">Cart</a>
            <a href="${pageContext.request.contextPath}/User_Info">Profile</a>
            <a href="${pageContext.request.contextPath}/index.jsp">Log Out</a>
        </div>
    </div>

    <!-- Search Header -->
    <div class="books-header">
        <h1>Search Books</h1>
    </div>

    <!-- Search Form -->
    <form action="${pageContext.request.contextPath}/Search" method="post" class="search-container">
        <input placeholder="Search by ISBN, Author, or Publisher" class="search-input" type="text" name="search-input" required/>
        <button type="submit" class="search-button">Search</button>
    </form>

    <!-- Books List -->
    <div class="books-list">
        <c:choose>
            <c:when test="${not empty search_result}">
                <c:forEach var="book" items="${search_result}">
                    <div class="book-element" onclick="openModal('${book.title}', '${book.author.name}', '${book.publisher}', '${book.genre.name}', '${book.year}', '${book.ISBN}')">
                        <label>Title:</label>
                        <p>${book.title}</p>
                        <label>Author:</label>
                        <p>${book.author.name}</p>
                        <label>Publisher:</label>
                        <p>${book.publisher}</p>
                        <label>Genre:</label>
                        <p>${book.genre.name}</p>
                        <label>Year:</label>
                        <p>${book.year}</p>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p class="none-found">No books found. Try a different search term.</p>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Modal Structure -->
    <div id="bookModal" class="modal">
        <div class="modal-content">
            <span class="close-button" onclick="closeModal()">&times;</span>
            <h2 id="modalTitle">Book Title</h2>
            <p id="modalAuthor">Author</p>
            <p id="modalPublisher">Publisher</p>
            <p id="modalGenre">Genre</p>
            <p id="modalYear">Year</p>
            <p id="modalISBN">ISBN</p>
        </div>
    </div>

    <script>
        // Function to open the modal with book details
        function openModal(title, author, publisher, genre, year, isbn) {
            document.getElementById("modalTitle").textContent = title;
            document.getElementById("modalAuthor").textContent = "Author: " + author;
            document.getElementById("modalPublisher").textContent = "Publisher: " + publisher;
            document.getElementById("modalGenre").textContent = "Genre: " + genre;
            document.getElementById("modalYear").textContent = "Year: " + year;
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