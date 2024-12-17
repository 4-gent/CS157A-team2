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
            // Validate session and user login
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("username") == null) {
                response.sendRedirect(request.getContextPath() + "/pages/login.jsp?error=Please log in to access orders.");
                return;
            }

            // Extract username and role
            String currentUsername = (String) session.getAttribute("username");
            boolean isAdmin = Boolean.TRUE.equals(session.getAttribute("isAdmin"));

            // Ensure UserContext is set for proper authorization
            UserContext.setUserId(currentUsername);

            // Check if an orderId parameter is present
            String orderIdParam = request.getParameter("orderId");
            if (orderIdParam != null) {
                try {
                    int orderId = Integer.parseInt(orderIdParam);
                    // Fetch the specific order
                    Order order = orderService.getOrderByID(orderId, currentUsername);

                    if (order == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found.");
                        return;
                    }

                    // Forward to order confirmation page with order details
                    request.setAttribute("order", order);
                    request.getRequestDispatcher("/pages/orderConfirmation.jsp").forward(request, response);
                    return;
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order ID format.");
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to view this order.");
                    return;
                }
            }

            // Default behavior: Fetch all orders
            List<Order> orders;

            if (isAdmin) {
                // Admin fetches all orders
                orders = orderService.getAllOrders();
            } else {
                // Normal user fetches their own orders
                orders = orderService.getAllOrdersForUser(currentUsername);
            }

            // Forward the list of orders to the JSP
            request.setAttribute("orders", orders);
            request.setAttribute("isAdmin", isAdmin); // Pass the admin flag for JSP UI logic
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