<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/profile.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/global.css">
</head>
<body class="profile-body">
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

    <!-- User Profile Section -->
    <div class="profile-header">
        <div class="user-element">
            <h2>User Profile</h2>
            <p><strong>Username:</strong> ${sessionScope.username}</p>
            <p><strong>Email:</strong> ${sessionScope.email}</p>
            <p><strong>Phone:</strong> ${sessionScope.phone}</p>
            <c:choose>
                <c:when test="${sessionScope.isAdmin}">
                    <p><strong>Role:</strong> Administrator</p>
                </c:when>
                <c:otherwise>
                    <p><strong>Role:</strong> Regular User</p>
                </c:otherwise>
            </c:choose>
            <p><strong>Favorite Author:</strong> ${sessionScope.favoriteAuthor}</p>
            <p><strong>Favorite Genre:</strong> ${sessionScope.favoriteGenre}</p>

			<br />

            <!-- Form for Updating Information -->
            <form action="${pageContext.request.contextPath}/User_Info" method="POST">
                <label for="newPassword">Update Password:</label>
                <input type="password" name="newPassword" id="newPassword"><br>
                <label for="favoriteAuthor">Update Favorite Author:</label>
                <input type="text" name="favoriteAuthor" id="favoriteAuthor" value=""><br>
                <label for="favoriteGenre">Update Favorite Genre:</label>
                <input type="text" name="favoriteGenre" id="favoriteGenre" value=""><br>
                <label for="deleteAccount">Delete Account:</label>
                <button type="submit" name="deleteAccount" value="true" onclick="return confirm('Are you sure you want to delete your account?');">Delete</button><br>
                <button type="submit" name="updateProfile" value="true">Update Profile</button>
            </form>

            <!-- Payment Information Section -->
            <div class="payment-info-container">
                <h2>Payment Information</h2>
                <c:if test="${not empty sessionScope.paymentDetails}">
                     <c:forEach var="paymentInfo" items="${sessionScope.paymentDetails}">
						<!-- Form for updating payment information -->
                         <form action="${pageContext.request.contextPath}/Payment" method="POST">
                             <input type="hidden" name="action" value="update">
                             <input type="hidden" name="paymentId" value="${paymentInfo.paymentID}">
                             <label for="newCardHolderName">Update Card Holder Name:</label>
                             <input type="text" name="newCardHolderName" id="newCardHolderName" value="${paymentInfo.cardHolderName}"><br>
                             <label for="newCardNumber">Update Card Number:</label>
                             <input type="text" name="newCardNumber" id="newCardNumber" value="${paymentInfo.cardNumber}"><br>
                             <label for="newCardExpiration">Update Card Expiration Date:</label>
                             <input type="text" name="newCardExpiration" id="newCardExpiration" value="${paymentInfo.monthYear}"><br>
                             <label for="newCardCVV">Update Card CVV:</label>
                             <input type="text" name="newCardCVV" id="newCardCVV" value="${paymentInfo.cvv}"><br>
                             <label for="newBillingStreet">Update Billing Street:</label>
                             <input type="text" name="newBillingStreet" id="newBillingStreet" value="${paymentInfo.billingAddress.street}"><br>
                             <label for="newBillingCity">Update Billing City:</label>
                             <input type="text" name="newBillingCity" id="newBillingCity" value="${paymentInfo.billingAddress.city}"><br>
                             <label for="newBillingState">Update Billing State:</label>
                             <input type="text" name="newBillingState" id="newBillingState" value="${paymentInfo.billingAddress.state}"><br>
                             <label for="newBillingZip">Update Billing ZIP:</label>
                             <input type="text" name="newBillingZip" id="newBillingZip" value="${paymentInfo.billingAddress.zip}"><br>
                             <label for="newBillingCountry">Update Billing Country:</label>
                             <input type="text" name="newBillingCountry" id="newBillingCountry" value="${paymentInfo.billingAddress.country}"><br>
                             <button type="submit" name="updatePayment" value="true">Update Payment Information</button>
                         </form>
                         <!-- Form for deleting payment information -->
                         <form action="${pageContext.request.contextPath}/Payment" method="POST">
                             <input type="hidden" name="action" value="delete">
                             <input type="hidden" name="paymentId" value="${paymentInfo.paymentID}">
                             <button type="submit" name="deletePayment" value="true" onclick="return confirm('Are you sure you want to delete this payment method?');">Delete Payment Information</button>
                         </form>
                     </c:forEach>
                </c:if>
                <c:if test="${empty sessionScope.paymentDetails}">
                    <p>No payment information found.</p>
                </c:if>
            </div>
        </div>
    </div>
    <br>
    <br>
</body>
</html>
