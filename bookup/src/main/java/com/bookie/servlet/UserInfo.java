package com.bookie.servlet;

import com.bookie.dao.UserDAO;
import com.bookie.bizlogic.PaymentInfoService;
import com.bookie.bizlogic.interfaces.PaymentInfoServiceInterface;
import com.bookie.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;

import com.bookie.models.Author;
import com.bookie.models.Book;
import com.bookie.models.InventoryItem;
import com.bookie.models.PaymentInfo;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookie.auth.UserContext;
import com.bookie.bizlogic.CartService;
import com.bookie.bizlogic.interfaces.CartServiceInterface;

@WebServlet("/User_Info")
public class UserInfo extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO(); // Initialize UserDAO
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the username from the session
        String username = (String) request.getSession().getAttribute("username");

        // If username is null, redirect to login page
        if (username == null) {
            response.sendRedirect("/bookup/pages/login.jsp");
            return;
        }

        try {
            User user = userDAO.getUserByUsername(username);

            if (user != null) {
                request.setAttribute("user_info", user); // Pass user to JSP
                request.getRequestDispatcher("/pages/profile.jsp").forward(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("User not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An error occurred while fetching user information");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the username from the session
        String username = (String) request.getSession().getAttribute("username");

        // Ensure UserContext is set
        UserContext.setUserId(username);
        
        PaymentInfoServiceInterface paymentInfoService = PaymentInfoService.getServiceInstance();
        CartServiceInterface cartService = CartService.getServiceInstance();
        // If username is null, redirect to login page
        if (username == null) {
            response.sendRedirect("/bookup/pages/login.jsp");
            return;
        }

        try {
            User user = userDAO.getUserByUsername(username);

            if (user != null) {
                // Handle profile updates
                String updateProfile = request.getParameter("updateProfile");
                if (updateProfile != null) {
                    boolean updated = false;

                    String newPassword = request.getParameter("newPassword");
                    if (newPassword != null && !newPassword.isEmpty()) {
                        user.setPassword(newPassword);
                        updated = true;
                    }

                    String favoriteAuthor = request.getParameter("favoriteAuthor");
                    if (favoriteAuthor != null && !favoriteAuthor.isEmpty()) {
                        user.setFavoriteAuthorID(Integer.parseInt(favoriteAuthor));
                        updated = true;
                    }

                    String favoriteGenre = request.getParameter("favoriteGenre");
                    if (favoriteGenre != null && !favoriteGenre.isEmpty()) {
                        user.setFavoriteGenreID(Integer.parseInt(favoriteGenre));
                        updated = true;
                    }

                    if (updated) {
                        userDAO.update(user);
                        response.sendRedirect(request.getContextPath() + "/pages/profile.jsp?username=" + username);
                        return;
                    }
                }

                // Handle account deletion
                String deleteAccount = request.getParameter("deleteAccount");
                if ("true".equals(deleteAccount)) {
                    // Delete associated records in the cart table
                    cartService.deleteByUsername(username);

                    // Retrieve and delete payment information
                    List<PaymentInfo> payInfo = paymentInfoService.getAllPaymentDetailsForUser(username);
                    for (PaymentInfo payment : payInfo) {
                        paymentInfoService.deletePaymentDetailsForUser(username, payment.getPaymentID());
                    }

                    // Delete the user
                    userDAO.delete(username);

                    response.sendRedirect("/bookup/index.jsp");
                    return;
                }
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("User not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An error occurred while processing the request");
        }
    }
}
