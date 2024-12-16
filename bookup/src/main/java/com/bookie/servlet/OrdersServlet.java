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

            // Extract username from session
            String currentUsername = (String) session.getAttribute("username");
            boolean isAdmin = Boolean.TRUE.equals(session.getAttribute("isAdmin"));

            // Ensure UserContext is set for proper authorization
            UserContext.setUserId(currentUsername);

            // Check for orderId (specific order details)
            String orderIdParam = request.getParameter("orderId");
            if (orderIdParam != null) {
                try {
                    int orderId = Integer.parseInt(orderIdParam);

                    // Fetch specific order
                    Order order = orderService.getOrderByID(orderId, currentUsername);
                    if (order == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found.");
                        return;
                    }

                    // Forward order details to orderConfirmation.jsp
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

            // If no orderId is provided, fetch all orders for the user
            String targetUsername = request.getParameter("username");
            List<Order> orders;
            if (targetUsername != null && isAdmin) {
                orders = orderService.getAllOrdersForUser(targetUsername);
            } else {
                orders = orderService.getAllOrdersForUser(currentUsername);
            }

            // Forward list of orders to orders.jsp
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