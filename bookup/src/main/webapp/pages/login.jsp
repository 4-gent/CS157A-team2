<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" type="text/css" href="../styles/login.css">
    <link rel="stylesheet" type="text/css" href="../styles/global.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;700&family=Roboto:wght@400;700&display=swap" rel="stylesheet">
</head>
<body class="login-body">
    <div class="nav">
        <a href="/bookup/index.jsp"><button class="nav-button">Home</button></a>
        <a href="/bookup/pages/register.jsp"><button class="nav-button">Register</button></a>
    </div>
    <div class="login-header">
        <h1>Bookup</h1>
    </div>
    <div class="login-container">
        <form class="login-form" action="/bookup/Login" method="post">
            <!-- New Log In text added -->
            <div class="form-header">Log In Form</div>
            <input class="login-input" type="text" name="username" placeholder="Username" />
            <input class="login-input" type="password" name="password" placeholder="Password" />
            <input class="login-button" type="submit" value="Login" />
        </form>
    </div>
</body>
</html>
