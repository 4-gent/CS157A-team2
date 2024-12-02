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
        		//request.setAttribute(username, response)
        	} else {
        		
        	}
        } catch (Exception e) {
        	
        }
    	
    	
    	//want to look at payment details
    	
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
    	
    	//hasn't made payment details yet so needs to fill them in
    	
    	//update
    	
    	//delete
    }
   
}

