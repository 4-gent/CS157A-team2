<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register with Book Up</title>
    <link rel="stylesheet" href="../styles/global.css">
    <link rel="stylesheet" href="../styles/register.css">
</head>
<body class="register-body">
    <div class="nav">
        <a href="/bookup/index.jsp"><button class="nav-button">Home</button></a>
        <a href="/bookup/pages/login.jsp"><button class="nav-button">Login</button></a>
    </div>
    <div class="register-header">
        <h1>Register with Book Up</h1>
    </div>
    <div class="register-container">
        <form class="register-form" action="/bookup/Register" method="post">
        <h2>User Information</h2>
            <input class="register-input" type="text" name="username" placeholder="Username" required />
            <input class="register-input" type="password" name="password" placeholder="Password" required />
            <input class="register-input" type="text" name="email" placeholder="Email" required />
            <input class="register-input" type="text" name="phone" placeholder="Phone Number" required />

            <!-- Address Information -->
            <h2>Address Information</h2>
            <input class="register-input" type="text" name="street" placeholder="Street" required />
            <input class="register-input" type="text" name="city" placeholder="City" required />
            <input class="register-input" type="text" name="state" placeholder="State" required />
            <input class="register-input" type="text" name="zip" placeholder="ZIP Code" required />
            <input class="register-input" type="text" name="country" placeholder="Country" required />

            <!-- Payment Information -->
            <h2>Payment Information</h2>
            <input class="register-input" type="text" name="cardNumber" placeholder="Card Number" required />
            <input class="register-input" type="text" name="expiryDate" placeholder="Expiry Date (YYYY-MM-DD)" required />
            <input class="register-input" type="text" name="cardHolderName" placeholder="Card Holder Name" required />
            <input class="register-input" type="text" name="cvv" placeholder="CVV" required />

           <input 
    class="register-input" 
    type="submit" 
    value="Register" 
    style="background-color: rgb(76, 175, 80); color: white; border: none; border-radius: 5px; padding: 8px 12px; font-size: 1rem; font-weight: bold; text-transform: uppercase; cursor: pointer;" 
    onmouseover="this.style.backgroundColor='rgb(58, 135, 61)'" 
    onmouseout="this.style.backgroundColor='rgb(76, 175, 80)'"
/>
        </form>
    </div>
    <br>
    <br>
</body>
</html>