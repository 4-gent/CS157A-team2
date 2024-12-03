package com.bookie.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bookie.auth.UserContext;
import com.bookie.bizlogic.OrderService;
import com.bookie.bizlogic.interfaces.OrderServiceInterface;
import com.bookie.models.Order;

@WebServlet("/Orders")
public class OrdersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrderServiceInterface orderService;

    @Override
    public void init() throws ServletException {
        orderService = OrderService.getServiceInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Retrieve session and validate user login
            HttpSession session = request.getSession(false);
            System.out.println("Session info: " + session);
            System.out.println("Session username: " + session.getAttribute("username"));
            if (session == null || session.getAttribute("username") == null) {
                response.sendRedirect(request.getContextPath() + "/pages/login.jsp?error=Please log in to access orders.");
                return;
            }

            // Extract user details from session
            String currentUsername = (String) session.getAttribute("username");
            boolean isAdmin = Boolean.TRUE.equals(session.getAttribute("isAdmin"));

            // Add logging to check session attributes
            System.out.println("Current Username: " + currentUsername);
            System.out.println("Is Admin: " + isAdmin);

            // Set UserContext
            UserContext.setUserId(currentUsername);
            System.out.println("UserContext set in OrdersServlet for userId: " + UserContext.getUserId());

            // Admin-only search for orders by username
            String targetUsername = request.getParameter("username");
            System.out.println("Target username: " + targetUsername);
            List<Order> orders;
            if (targetUsername != null && isAdmin) {
                // Fetch all orders for the target user
                orders = orderService.getAllOrdersForUser(targetUsername);
                System.out.println("Admin user: " + currentUsername + " fetching orders for: " + targetUsername);
            } else {
                // Fetch orders for the current user (default behavior)
                orders = orderService.getAllOrdersForUser(currentUsername);
                System.out.println("Regular user: " + currentUsername);
            }
            System.out.println("User orders: " + orders);
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("/pages/orders.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing your request.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}