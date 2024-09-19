<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Register</title>
	<link rel="stylesheet" href="../styles/global.css">
	<link rel="stylesheet" href="../styles/register.css">
</head>
<body class="register-body">
	<div class="nav">
		<a href="/bookup/index.jsp"><button class="nav-button">Home</button></a>
		<a href="/bookup/pages/login.jsp"><button class="nav-button">Login</button></a>
		<a href="/bookup/pages/register.jsp"><button class="nav-button">Register</button></a>
	</div>
	<div class="register-header">
		<h1>Register with Book Up</h1>
	</div>
	<div class="register-container">
		<form class="register-form" action="/bookup/Register" method="post">
			<input class="register-input" type="text" name="username" placeholder="Username" />
			<input class="register-input" type="password" name="password" placeholder="Password" />
			<input class="register-input" type="text" name="email" placeholder="Email" />
			<input class="register-input" type="text" name="phone" placeholder="Phone Number" />
			<input class="register-input" type="submit" value="Register" />
		</form>
	</div>
</body>
</html>