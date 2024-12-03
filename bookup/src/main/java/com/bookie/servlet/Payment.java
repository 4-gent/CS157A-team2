package com.bookie.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bookie.dao.UserDAO;
import com.bookie.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookie.bizlogic.BookService;
import com.bookie.bizlogic.PaymentInfoService;
import com.bookie.bizlogic.interfaces.BookServiceInterface;
import com.bookie.bizlogic.interfaces.PaymentInfoServiceInterface;
import com.bookie.models.Address;
import com.bookie.models.Book;
import com.bookie.models.PaymentInfo;

@WebServlet("/Payment")
public class Payment extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private PaymentInfoService paymentInfoService;
    
    public Payment() {
        super();
        
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	// Retrieve the username from the session, need for service methods
    	String username = (String) request.getSession().getAttribute("username");
    	
    	// If username is null, redirect to login page
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
    	
    	
    	//want to look at payment details
    	
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
    	// Retrieve the username from the session, need for service methods
    	String username = (String) request.getSession().getAttribute("username");
    	
    	// If username is null, redirect to login page
        if (username == null) {
            response.sendRedirect("/bookup/pages/login.jsp");
            return;
        }
        
        
        try {
        	
        	List<PaymentInfo> userPayment = paymentInfoService.getAllPaymentDetailsForUser(username);
        	String updatePayment = request.getParameter("updatePayment");
        	if (updatePayment != null) {
        		
        		PaymentInfo paymentDetails;
        		
        		
        		int addressID = Integer.parseInt(request.getParameter("addressID")); 
        		
        		String street = request.getParameter("street");
        		if (street != null && !street.isEmpty()) {
        			//don't think I need this
        			//might need to check if its null
        		}
        		
        		String city = request.getParameter("city");
        		
        		String state = request.getParameter("state");
        		
        		String zip = request.getParameter("zip");
        		
        		String country = request.getParameter("country");
        		
        		Address billingAddress = new Address((int) addressID, street, city, state, zip, country);
        		
        		int paymentID = Integer.parseInt(request.getParameter("paymentID"));
        		
        		String cardNumber = request.getParameter("cardNumber");
        		
        		String monthYear = request.getParameter("monthYear");
        		
        		String cardHolderName = request.getParameter("cardHolderName");
        		
        		String cvv = request.getParameter("cvv");
        		
        		boolean isDeleted = false; //not sure
        		
        		PaymentInfo paymentInfo = new PaymentInfo(paymentID, username, cardNumber, monthYear, cardHolderName, cvv, billingAddress, isDeleted);
        		
        		boolean updates = paymentInfoService.updatePaymentDetailsForUser(paymentInfo);
        		
        		
        		//handle payment deletion
        		String paymentDeleted = request.getParameter("isDeleted");
        		if ("true".equals(paymentDeleted)) {
        			paymentInfoService.deletePaymentDetailsForUser(username, paymentID);
        			response.sendRedirect("/bookup/payment.jsp");
                    return;
        		}
        	}
        	else { //add details
        		PaymentInfo paymentDetails;
        		
        		
        		int addressID = Integer.parseInt(request.getParameter("addressID")); 
        		
        		String street = request.getParameter("street");
        		if (street != null && !street.isEmpty()) {
        			//don't think I need this
        			//might need to check if its null
        		}
        		
        		String city = request.getParameter("city");
        		
        		String state = request.getParameter("state");
        		
        		String zip = request.getParameter("zip");
        		
        		String country = request.getParameter("country");
        		
        		Address billingAddress = new Address((int) addressID, street, city, state, zip, country);
        		
        		int paymentID = Integer.parseInt(request.getParameter("paymentID"));
        		
        		String cardNumber = request.getParameter("cardNumber");
        		
        		String monthYear = request.getParameter("monthYear");
        		
        		String cardHolderName = request.getParameter("cardHolderName");
        		
        		String cvv = request.getParameter("cvv");
        		
        		boolean isDeleted = false; //not sure
        		
        		PaymentInfo paymentInfo = new PaymentInfo(paymentID, username, cardNumber, monthYear, cardHolderName, cvv, billingAddress, isDeleted);
        		
        		paymentInfo = paymentInfoService.addPaymentDetailsForUser(paymentInfo);
        	}
        	
        	
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An error occurred while processing the request");
        }
    	
    	//Add new payment detail for user? (if dont have them already or adding more)
    	
    	
    	//update
    	
    	//delete
    }
   
}

