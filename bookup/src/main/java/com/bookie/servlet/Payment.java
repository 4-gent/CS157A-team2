package com.bookie.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookie.auth.UserContext;
import com.bookie.bizlogic.AddressService;
import com.bookie.bizlogic.PaymentInfoService;
import com.bookie.bizlogic.interfaces.AddressServiceInterface;
import com.bookie.bizlogic.interfaces.PaymentInfoServiceInterface;
import com.bookie.models.Address;
import com.bookie.models.PaymentInfo;

@WebServlet("/Payment")
public class Payment extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private PaymentInfoServiceInterface paymentInfoService;
    private AddressServiceInterface addressService;

    public Payment() {
        super();
        paymentInfoService = PaymentInfoService.getServiceInstance();
        addressService = AddressService.getServiceInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");
        if (username == null) {
            response.sendRedirect("/bookup/pages/login.jsp");
            return;
        }

        try {
            List<PaymentInfo> userPayment = paymentInfoService.getAllPaymentDetailsForUser(username);
            if (userPayment != null) {
                request.setAttribute("paymentDetails", userPayment);
                request.getRequestDispatcher("/pages/payment.jsp").forward(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Payment Information not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An error occurred while fetching payment information");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");
        if (username == null) {
            response.sendRedirect("/bookup/pages/login.jsp");
            return;
        }

        // Ensure UserContext is set
        UserContext.setUserId(username);

        String action = request.getParameter("action");
        try {
            if ("add".equals(action)) {
                addPaymentDetails(request, response, username);
            } else if ("update".equals(action)) {
                updatePaymentDetails(request, response, username);
            } else if ("delete".equals(action)) {
                deletePaymentDetails(request, response, username);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid action");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An error occurred while processing payment information");
        }
    }

    private void addPaymentDetails(HttpServletRequest request, HttpServletResponse response, String username) throws SQLException, IOException {
        String cardNumber = request.getParameter("newCardNumber");
        String expiryDate = request.getParameter("newCardExpiration");
        String cardHolderName = request.getParameter("newCardHolderName");
        String cvv = request.getParameter("newCardCVV");
        String street = request.getParameter("newBillingStreet");
        String city = request.getParameter("newBillingCity");
        String state = request.getParameter("newBillingState");
        String zip = request.getParameter("newBillingZip");
        String country = request.getParameter("newBillingCountry");

        Address address = new Address(0, street, city, state, zip, country);
        Address savedAddress = addressService.addAddress(address);

        PaymentInfo paymentInfo = new PaymentInfo(0, username, cardNumber, expiryDate, cardHolderName, cvv, savedAddress, false);
        PaymentInfo savedPaymentInfo = paymentInfoService.addPaymentDetailsForUser(paymentInfo);

        if (savedPaymentInfo != null) {
            response.sendRedirect("/bookup/pages/profile.jsp");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Failed to add payment details");
        }
    }

    private void updatePaymentDetails(HttpServletRequest request, HttpServletResponse response, String username) throws SQLException, IOException {
        int paymentId = Integer.parseInt(request.getParameter("paymentId"));
        String cardNumber = request.getParameter("newCardNumber");
        String expiryDate = request.getParameter("newCardExpiration");
        String cardHolderName = request.getParameter("newCardHolderName");
        String cvv = request.getParameter("newCardCVV");
        String street = request.getParameter("newBillingStreet");
        String city = request.getParameter("newBillingCity");
        String state = request.getParameter("newBillingState");
        String zip = request.getParameter("newBillingZip");
        String country = request.getParameter("newBillingCountry");

        PaymentInfo existingPaymentInfo = paymentInfoService.getPaymentInfoById(paymentId);
        if (existingPaymentInfo == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Payment Information not found");
            return;
        }

        // Ensure UserContext is set for the payment info user
        UserContext.setUserId(existingPaymentInfo.getUsername());

        int addressID = existingPaymentInfo.getBillingAddress().getAddressID();
        Address address = new Address(addressID, street, city, state, zip, country);
        addressService.updateAddress(address);

        PaymentInfo paymentInfo = new PaymentInfo(paymentId, username, cardNumber, expiryDate, cardHolderName, cvv, address, false);
        boolean isUpdated = paymentInfoService.updatePaymentDetailsForUser(paymentInfo);

        if (isUpdated) {
            response.sendRedirect("/bookup/pages/profile.jsp");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Failed to update payment details");
        }
    }

    private void deletePaymentDetails(HttpServletRequest request, HttpServletResponse response, String username) throws SQLException, IOException {
        int paymentId = Integer.parseInt(request.getParameter("paymentId"));
        boolean isDeleted = paymentInfoService.deletePaymentDetailsForUser(username, paymentId);

        if (isDeleted) {
            response.sendRedirect("/bookup/pages/profile.jsp");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Failed to delete payment details");
        }
    }
}