package com.bookie.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookie.auth.UserContext;
import com.bookie.bizlogic.AddressService;
import com.bookie.bizlogic.PaymentInfoService;
import com.bookie.bizlogic.UserService;
import com.bookie.bizlogic.interfaces.AddressServiceInterface;
import com.bookie.bizlogic.interfaces.PaymentInfoServiceInterface;
import com.bookie.bizlogic.interfaces.UserServiceInterface;
import com.bookie.models.Address;
import com.bookie.models.PaymentInfo;

@WebServlet("/Register")
public class Register extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Register() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        String street = request.getParameter("street");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String zip = request.getParameter("zip");
        String country = request.getParameter("country");

        String cardNumber = request.getParameter("cardNumber");
        String expiryDate = request.getParameter("expiryDate");
        String cardHolderName = request.getParameter("cardHolderName");
        String cvv = request.getParameter("cvv");

        UserServiceInterface userService = UserService.getServiceInstance();
        AddressServiceInterface addressService = AddressService.getServiceInstance();
        PaymentInfoServiceInterface paymentInfoService = PaymentInfoService.getServiceInstance();

        try {
            // Register the user
            userService.register(username, password, email, phone, false, 0, 0);

            // Ensure UserContext is set
            UserContext.setUserId(username);

            // Create and save the address
            Address address = new Address(0, street, city, state, zip, country);
            Address savedAddress = addressService.addAddress(address);

            // Create and save the payment information
            PaymentInfo paymentInfo = new PaymentInfo(0, username, cardNumber, expiryDate, cardHolderName, cvv, savedAddress, false);
            paymentInfoService.addPaymentDetailsForUser(paymentInfo);

            // Redirect to login page after successful registration
            response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
        } catch (SQLException e) {
            // Log the error and redirect to an error page
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/pages/error.jsp?message=Database error occurred");
        } catch (Exception e) {
            // Handle any other unexpected errors
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/pages/error.jsp?message=Unexpected error occurred");
        }
    }
}