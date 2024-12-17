<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Customers</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/global.css">    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/customers.css">
</head>
<body class="customers-body">
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

    <!-- Search Form -->
    <!--<div class="search-container">
        <form action="${pageContext.request.contextPath}/Customers" method="post">
            <input type="text" name="username" placeholder="Enter username" required>
            <button type="submit">Search</button>
        </form>
    </div>-->

    <!-- Customer Details Section -->
    <div class="customer-details-container">
        <h1>Customer Details</h1>
        <c:if test="${not empty userDetails}">
            <table border="1">
                <tr>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Favorite Author</th>
                    <th>Favorite Genre</th>
                </tr>
                <tr>
                    <td>${userDetails.username}</td>
                    <td>${userDetails.email}</td>
                    <td>${userDetails.phone}</td>
                    <td><c:out value="${favoriteAuthor != null ? favoriteAuthor.name : 'N/A'}"/></td>
                    <td><c:out value="${favoriteGenre != null ? favoriteGenre.name : 'N/A'}"/></td>
                </tr>
            </table>
        </c:if>
        <c:if test="${not empty nonAdminUsers}">
            <table border="1">
                <tr>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Favorite Author</th>
                    <th>Favorite Genre</th>
                </tr>
                <c:forEach var="user" items="${nonAdminUsers}">
                    <tr>
                        <td>${user.username}</td>
                        <td>${user.email}</td>
                        <td>${user.phone}</td>
                        <td>
                            <c:choose>
                                <c:when test="${user.favoriteAuthorID != 0}">
                                    <c:out value="${authorMap[user.favoriteAuthorID] != null ? authorMap[user.favoriteAuthorID].name : 'N/A'}"/>
                                </c:when>
                                <c:otherwise>
                                    N/A
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${user.favoriteGenreID != 0}">
                                    <c:out value="${genreMap[user.favoriteGenreID] != null ? genreMap[user.favoriteGenreID].name : 'N/A'}"/>
                                </c:when>
                                <c:otherwise>
                                    N/A
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
        <c:if test="${empty nonAdminUsers && empty userDetails}">
            <p>No customer details found.</p>
        </c:if>
        <c:if test="${not empty param.error}">
            <p style="color: red;">${param.error}</p>
        </c:if>
    </div>
</body>
</html>