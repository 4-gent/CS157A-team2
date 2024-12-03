package com.bookie.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookie.auth.UserContext;
import com.bookie.bizlogic.InventoryService;
import com.bookie.bizlogic.CartService;
import com.bookie.bizlogic.interfaces.CartServiceInterface;
import com.bookie.bizlogic.interfaces.InventoryServiceInterface;
import com.bookie.errors.NotEnoughQuantityException;
import com.bookie.models.Cart;
import com.bookie.models.CartItem;
import com.bookie.models.InventoryItem;
import com.bookie.models.Order;

@WebServlet("/Cart")
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private CartServiceInterface cartService;
    private InventoryServiceInterface inventoryService;

    public CartServlet() {
        super();
        cartService = CartService.getServiceInstance();
        inventoryService = InventoryService.getServiceInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");
        if (username == null) {
            response.sendRedirect("/bookup/pages/login.jsp");
            return;
        }

        // Ensure UserContext is set
        UserContext.setUserId(username);

        try {
            Cart cart = cartService.getUserCart(username);
            request.setAttribute("cart", cart);
            request.getRequestDispatcher("/pages/cart.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/pages/error.jsp?message=Failed to retrieve cart");
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
                handleAddToCart(request, response, username);
            } else if ("remove".equals(action)) {
                handleRemoveFromCart(request, response, username);
            } else if ("checkout".equals(action)) {
                handleCheckout(request, response, username);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid action");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/pages/error.jsp?message=Failed to process cart action");
        }
    }

    private void handleAddToCart(HttpServletRequest request, HttpServletResponse response, String username) throws Exception {
        String isbn = request.getParameter("isbn");
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        // Retrieve the inventory item from the database based on ISBN
        InventoryItem inventoryItem = inventoryService.searchByISBN(isbn);
        if (inventoryItem == null) {
            response.sendRedirect(request.getContextPath() + "/pages/error.jsp?message=Inventory item not found");
            return;
        }

        List<CartItem> items = new ArrayList<>();
        items.add(new CartItem(inventoryItem, quantity));

        try {
            Cart updatedCart = cartService.addItemsToCart(username, items);
            request.setAttribute("cart", updatedCart);
            response.sendRedirect(request.getContextPath() + "/Cart");
        } catch (NotEnoughQuantityException e) {
            response.sendRedirect(request.getContextPath() + "/pages/error.jsp?message=Not enough quantity available");
        }
    }

    private void handleRemoveFromCart(HttpServletRequest request, HttpServletResponse response, String username) throws Exception {
        int itemId = Integer.parseInt(request.getParameter("itemId"));

        List<Integer> itemIDs = new ArrayList<>();
        itemIDs.add(itemId);

        Cart updatedCart = cartService.removeItemsFromCart(username, itemIDs);
        request.setAttribute("cart", updatedCart);
        response.sendRedirect(request.getContextPath() + "/Cart");
    }

    private void handleCheckout(HttpServletRequest request, HttpServletResponse response, String username) throws Exception {
        int addressID = Integer.parseInt(request.getParameter("addressID"));
        int paymentDetailsID = Integer.parseInt(request.getParameter("paymentDetailsID"));

        Order order = cartService.checkout(username, addressID, paymentDetailsID);
        request.setAttribute("order", order);
        response.sendRedirect(request.getContextPath() + "/pages/orderConfirmation.jsp");
    }
}