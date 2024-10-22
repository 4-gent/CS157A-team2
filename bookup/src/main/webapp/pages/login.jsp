<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Login</title>
	<link rel="stylesheet" href="../styles/login.css">
	<link rel="stylesheet" href="../styles/global.css">
</head>
<body class="login-body">
	<div class="nav">
		<a href="/bookup/index.jsp"><button class="nav-button">Home</button></a>
		<a href="/bookup/pages/register.jsp"><button class="nav-button">Register</button></a>
	</div>
	<div class="login-header">
		<h1>Login with Book Up</h1>
	</div>
	<div class="login-container">
		<form class="login-form" action="/bookup/Login" method="post">
			<input class="login-input" type="text" name="username" placeholder="Username" />
			<input class="login-input" type="password" name="password" placeholder="Password" />
			<input class="login-input" type="submit" value="Login" />
		</form>
	</div>
</body>
</html>