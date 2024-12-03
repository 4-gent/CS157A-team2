<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <title>Payment Information</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cart.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/global.css">
</head>
<body class="payment body">
	<!-- Navigation Bar -->
	<div class="nav">
		<a href="/bookup/Books"><button class="nav-button">Books</button></a>
		<a href="/bookup/index.jsp"><button class="nav-button">Log Out</button></a>
		<a href="/bookup/profile.jsp"><button class="nav-button">Profile</button></a>
		
	</div>
	
	<!-- Payment Section -->
	<div class="payment-container">
		<h1>Payment Information</h1>
	
		<!-- Display Payment Information -->
		<div class="payment-infos">
			<c:if test="${not empty paymentInfos}">
				<c:forEach var="info" items="${paymentInfos}">
					<div class="payment-info">
						<p><strong>Payment ID:</strong> ${info.paymentDetails.paymentID}</p>
						<p><strong>User Name:</strong> ${info.username}</p>
						<p><strong>Card Number:</strong> ${info.cardNumber}</p>
						<p><strong>Expiration:</strong> ${info.exp}</p>
						<p><strong>Card Holder Name:</strong> ${info.cardHolderName}</p>
						<p><strong>CVV:</strong> ${info.cvv}</p>
						<h2><strong>Billing Address</strong></h2>
						<p><strong>Address ID:</strong> ${info.addressID}</p>
						<p><strong>Street:</strong> ${info.street}</p>
						<p><strong>City:</strong> ${info.city}</p>
						<p><strong>State:</strong> ${info.state}</p>
						<p><strong>ZIP Code:</strong> ${info.zip}</p>
						<p><strong>Country:</strong> ${info.country}</p>
					</div>
				</c:forEach>
			</c:if>
			
			<!-- No Payment Info Message -->
			<c:if test="${empty paymentInfos}">
				<p class="none-found">Your payment detail are empty. Add them now!</p>
			</c:if>
			
			<button class ="addPayment">Add Payment</button>
			
		</div>
		
		<!-- Add Button -->
		<form> </form>
		
		<!-- Delete Button --> 
		<form> </form>
		
		<!-- Edit Button -->
		<form> </form>
		
	</div>
</body>
</html>