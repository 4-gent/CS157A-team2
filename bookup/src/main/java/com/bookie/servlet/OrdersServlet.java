
package com.bookie.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookie.auth.UserContext;
import com.bookie.bizlogic.OrderService;
import com.bookie.bizlogic.interfaces.OrderServiceInterface;
import com.bookie.models.Order;

@WebServlet("/Orders")
public class OrdersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private OrderServiceInterface orderService;

    public OrdersServlet() {
        super();
        orderService = OrderService.getServiceInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String currentUsername = (String) request.getSession().getAttribute("username");
        if (currentUsername == null) {
            response.sendRedirect("/bookup/pages/login.jsp");
            return;
        }

        // Ensure UserContext is set
        UserContext.setUserId(currentUsername);

        try {
            List<Order> orders;
            String targetUsername = request.getParameter("username");

            if (targetUsername != null) {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("updateStatus".equals(action)) {
            handleUpdateOrderStatus(request, response);
        } else {
            doGet(request, response);
        }
    }

    private void handleUpdateOrderStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String currentUsername = (String) request.getSession().getAttribute("username");
        if (currentUsername == null) {
            response.sendRedirect("/bookup/pages/login.jsp");
            return;
        }

        // Ensure UserContext is set
        UserContext.setUserId(currentUsername);

        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String newStatus = request.getParameter("status");

            boolean isUpdated = orderService.updateOrderStatus(orderId, newStatus);
            if (isUpdated) {
                response.sendRedirect(request.getContextPath() + "/Orders");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update order status");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while updating the order status");
        }
    }
}