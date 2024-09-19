<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Login</title>
	<link rel="stylesheet" href="../styles/global.css">
</head>
<body>
	<div class="nav">
		<a href="/bookup/index.jsp"><button class="nav-button">Home</button></a>
		<a href="/bookup/pages/login.jsp"><button class="nav-button">Login</button></a>
		<a href="/bookup/pages/register.jsp"><button class="nav-button">Register</button></a>
	</div>
	<div class="login-body">
		<form class="login-form" action="/bookup/Login" method="post">
			<input type="text" name="username" placeholder="Username" />
			<input type="password" name="password" placeholder="Password" />
			<input type="submit" value="Login" />
		</form>
	</div>
</body>
</html>