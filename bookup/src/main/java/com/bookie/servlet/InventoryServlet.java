package com.bookie.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bookie.auth.UserContext;
import com.bookie.bizlogic.AuthorService;
import com.bookie.bizlogic.BookService;
import com.bookie.bizlogic.InventoryService;
import com.bookie.bizlogic.interfaces.AuthorServiceInterface;
import com.bookie.bizlogic.interfaces.BookServiceInterface;
import com.bookie.bizlogic.interfaces.InventoryServiceInterface;
import com.bookie.models.Author;
import com.bookie.models.Book;
import com.bookie.models.InventoryItem;

@WebServlet("/Inventory")
public class InventoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private InventoryServiceInterface inventoryService;
    private BookServiceInterface bookService;
    private AuthorServiceInterface authorService;

    @Override
    public void init() throws ServletException {
        inventoryService = InventoryService.getServiceInstance();
        bookService = BookService.getServiceInstance();
        authorService = AuthorService.getServiceInstance();
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
                response.sendRedirect(request.getContextPath() + "/pages/login.jsp?error=Please log in to access inventory.");
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
            System.out.println("UserContext set in InventoryServlet for userId: " + UserContext.getUserId());

            // Fetch all inventory items
            List<InventoryItem> inventoryItems = inventoryService.getAllInventoryItems();
            System.out.println("Inventory items: " + inventoryItems);

            // Fetch all authors
            List<Author> authors = authorService.getAuthors();

            // Create a map to hold the combined information
            List<Map<String, Object>> inventoryDetails = new ArrayList<>();

            // Fetch corresponding book and author details for each inventory item
            for (InventoryItem item : inventoryItems) {
                Book book = bookService.getBookByISBN(item.getBook().getISBN());
                Author author = null;
                if (book != null && book.getAuthor() != null) {
                    for (Author a : authors) {
                        if (a.getAuthorID() == book.getAuthor().getAuthorID()) {
                            author = a;
                            break;
                        }
                    }
                }
                Map<String, Object> details = new HashMap<>();
                details.put("inventoryItem", item);
                details.put("book", book);
                details.put("author", author);
                inventoryDetails.add(details);
            }

            request.setAttribute("inventoryDetails", inventoryDetails);
            request.getRequestDispatcher("/pages/inventory.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing your request.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Retrieve session and validate user login
            HttpSession session = request.getSession(false);
            System.out.println("Session info: " + session);
            System.out.println("Session username: " + session.getAttribute("username"));
            if (session == null || session.getAttribute("username") == null) {
                response.sendRedirect(request.getContextPath() + "/pages/login.jsp?error=Please log in to access inventory.");
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
            System.out.println("UserContext set in InventoryServlet for userId: " + UserContext.getUserId());

            // Determine action
            String action = request.getParameter("action");
            if (action == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action parameter is missing.");
                return;
            }

            switch (action) {
                case "add":
                    handleAdd(request, response);
                    break;
                case "delete":
                    handleDelete(request, response);
                    break;
                case "update":
                    handleUpdate(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing your request.");
        }
    }

    private void handleAdd(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Ensure the user is an admin
        if (!Boolean.TRUE.equals(request.getSession().getAttribute("isAdmin"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin privileges required.");
            return;
        }

        // Extract and validate parameters
        String isbn = request.getParameter("isbn");
        String title = request.getParameter("title");
        String yearStr = request.getParameter("year");
        String publisher = request.getParameter("publisher");
        String priceStr = request.getParameter("price");
        String quantityStr = request.getParameter("quantity");
        String description = request.getParameter("description");

        if (isbn == null || title == null || yearStr == null || publisher == null || priceStr == null || quantityStr == null || description == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "All parameters are required.");
            return;
        }

        if (isbn.length() > 13) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ISBN length exceeds the limit.");
            return;
        }

        int year;
        double price;
        int quantity;

        try {
            year = Integer.parseInt(yearStr);
            price = Double.parseDouble(priceStr);
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format.");
            return;
        }

        // Add book and inventory item
        Book book = new Book(isbn, title, year, publisher, false, null);
        Book savedBook = bookService.addBook(book);
        InventoryItem item = new InventoryItem(0, savedBook, price, quantity, description);
        inventoryService.addInventoryItem(item);

        response.sendRedirect(request.getContextPath() + "/Inventory");
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Ensure the user is an admin
        if (!Boolean.TRUE.equals(request.getSession().getAttribute("isAdmin"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin privileges required.");
            return;
        }

        // Extract parameters
        String itemIdStr = request.getParameter("itemId");
        if (itemIdStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Item ID is required.");
            return;
        }

        int itemId;
        try {
            itemId = Integer.parseInt(itemIdStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid item ID format.");
            return;
        }

        // Fetch the inventory item to get the associated book
        InventoryItem item = inventoryService.getByInventoryItemID(itemId);
        if (item == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Inventory item not found.");
            return;
        }

        // Delete the inventory item
        boolean isDeleted = inventoryService.removeInventoryItem(itemId);
        if (!isDeleted) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete inventory item.");
            return;
        }

        // Delete the associated book
        Book book = item.getBook();
        if (book != null) {
            boolean isBookDeleted = bookService.deleteBook(book.getISBN());
            if (!isBookDeleted) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete associated book.");
                return;
            }
        }

        response.sendRedirect(request.getContextPath() + "/Inventory");
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Ensure the user is an admin
        if (!Boolean.TRUE.equals(request.getSession().getAttribute("isAdmin"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin privileges required.");
            return;
        }

        // Extract and validate parameters
        String itemIdStr = request.getParameter("itemId");
        String priceStr = request.getParameter("price");
        String quantityStr = request.getParameter("quantity");
        String description = request.getParameter("description");

        if (itemIdStr == null || priceStr == null || quantityStr == null || description == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "All parameters are required.");
            return;
        }

        int itemId;
        double price;
        int quantity;

        try {
            itemId = Integer.parseInt(itemIdStr);
            price = Double.parseDouble(priceStr);
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format.");
            return;
        }

        // Update inventory item
        InventoryItem item = inventoryService.getByInventoryItemID(itemId);
        item.setPrice(price);
        item.setQty(quantity);
        item.setDescription(description);
        inventoryService.updateInventoryItem(item);

        response.sendRedirect(request.getContextPath() + "/Inventory");
    }
}