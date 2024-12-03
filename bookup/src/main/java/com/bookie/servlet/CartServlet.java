package com.bookie.servlet;

import com.bookie.bizlogic.CartService;
import com.bookie.bizlogic.InventoryService;
import com.bookie.bizlogic.interfaces.CartServiceInterface;
import com.bookie.bizlogic.interfaces.InventoryServiceInterface;
import com.bookie.models.CartItem;
import com.bookie.models.InventoryItem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/Cart")
public class CartServlet extends HttpServlet {

    private CartServiceInterface cartService;
    private InventoryServiceInterface inventoryService;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = CartService.getInstance();
        inventoryService = InventoryService.getServiceInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            System.out.println("Session or Username is null");
            session = request.getSession(true);
            session.setAttribute("username", "testuser"); // Mock username for debugging
        }

        String username = (String) session.getAttribute("username");
        String action = request.getParameter("action");

        if ("addToCart".equals(action)) {
            handleAddToCart(request, response, username);
        } else if ("remove".equals(action)) {
            handleRemoveFromCart(request, response, username);
        }
    }

    private void handleAddToCart(HttpServletRequest request, HttpServletResponse response, String username)
            throws IOException {
        System.out.println("Entering handleAddToCart");

        // Fetch and validate ID parameter
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            System.out.println("ID parameter is missing or empty");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or invalid ID parameter.");
            return;
        }

      

        // Fetch and validate quantity parameter
        String quantityParam = request.getParameter("quantity");
        if (quantityParam == null || quantityParam.isEmpty()) {
            System.out.println("Quantity parameter is missing or empty");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or invalid quantity parameter.");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityParam);
            System.out.println("Parsed Quantity: " + quantity);
        } catch (NumberFormatException e) {
            System.out.println("Invalid Quantity parameter: " + quantityParam);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Quantity parameter.");
            return;
        }

        try {
            // Fetch InventoryItem using the ID
            System.out.println("Fetching InventoryItem for ID: " + id);
            InventoryItem inventoryItem = inventoryService.getByInventoryItemID(id);
            if (inventoryItem == null) {
                System.out.println("No InventoryItem found for ID: " + id);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Book not found.");
                return;
            }

            // Create CartItem and prepare list for addition
            CartItem item = new CartItem(inventoryItem, quantity);
            List<CartItem> items = new ArrayList<>();
            items.add(item);

            // Add items to the cart
            System.out.println("Adding items to cart for user: " + username);
            cartService.addItemsToCart(username, items);

            // Redirect to cart page
            System.out.println("Redirecting to cart.jsp");
            response.sendRedirect("/bookup/pages/cart.jsp");
        } catch (Exception e) {
            System.out.println("Exception in handleAddToCart: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error adding to cart.");
        }
    }


    private void handleRemoveFromCart(HttpServletRequest request, HttpServletResponse response, String username)
            throws IOException {
        String bookId = request.getParameter("bookId");

        try {
            // Parse bookId and remove it from the cart
            int bookIdInt = Integer.parseInt(bookId);
            List<Integer> itemsToRemove = new ArrayList<>();
            itemsToRemove.add(bookIdInt);

            cartService.removeItemsFromCart(username, itemsToRemove);

            // Redirect to cart page
            response.sendRedirect("/bookup/pages/cart.jsp");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID.");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error removing item from cart.");
        }
    }
}
