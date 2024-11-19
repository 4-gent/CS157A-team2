package com.bookie.bizlogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bookie.auth.AuthorizationProxy;
import com.bookie.auth.UserContext;
import com.bookie.dao.CartDAO;
import com.bookie.models.Book;
import com.bookie.models.Cart;
import com.bookie.models.InventoryItem;
import com.bookie.models.Order;

public class CartServiceTest {

    private CartServiceInterface cartService;
    private UserServiceInterface userService;
    private BookServiceInterface bookService;
    private InventoryServiceInterface inventoryService;

    private Connection connection;

    @BeforeEach
    public void setUp() {
        cartService = AuthorizationProxy.createProxy(new CartService());
        userService = AuthorizationProxy.createProxy(new UserService());
        bookService = AuthorizationProxy.createProxy(new BookService());
        inventoryService = AuthorizationProxy.createProxy(new InventoryService());
    }

    @AfterEach
    public void tearDown() {
        UserContext.clear();
     // Get connection from any DAO (CartDAO, BookDAO, etc.)
        try {
            connection = new CartDAO().getConnection();
            connection.createStatement().execute("DELETE FROM Carts");
            connection.createStatement().execute("DELETE FROM InventoryItems");
            connection.createStatement().execute("DELETE FROM Books");
            connection.createStatement().execute("DELETE FROM Users");
        } catch (SQLException e) {
            fail("Failed to clean up the database: " + e.getMessage());
        }
    }

//    @Test
//    public void testGetUserCart_Success() throws SQLException {
//        userService.register("user1", "password", "user1@example.com", "1234567890", false, 0, 0);
//        UserContext.setUserId("user1");
//
//        Cart cart = cartService.getUserCart("user1");
//        assertNotNull(cart, "Cart should not be null");
//        assertEquals("user1", cart.getUsername(), "Cart username should match");
//    }
//
//    @Test
//    public void testGetUserCart_AccessDenied() {
//        try {
//            userService.register("user1", "password", "user1@example.com", "1234567890", false, 0, 0);
//            userService.register("user2", "password", "user2@example.com", "0987654321", false, 0, 0);
//
//            UserContext.setUserId("user2");
//            Exception exception = assertThrows(SecurityException.class, () -> {
//                cartService.getUserCart("user1");
//            });
//            assertEquals("Access denied: Admin or user match required", exception.getMessage());
//        } catch (SQLException e) {
//            fail("SQLException during test: " + e.getMessage());
//        }
//    }
//
//    @Test
//    public void testAddItemsToCart_Success() throws Exception {
//        userService.register("user1", "password", "user1@example.com", "1234567890", false, 0, 0);
//        UserContext.setUserId("user1");
//
//        Book book = new Book("1234567890123", "Test Book", 2023, "Test Publisher", false, null, null);
//        bookService.addBook(book);
//
//        InventoryItem inventoryItem = new InventoryItem(0, book, 19.99, 10, "Test Description");
//        inventoryService.addInventoryItem(inventoryItem);
//
//        List<InventoryItem> items = new ArrayList<>();
//        items.add(inventoryItem);
//
//        Cart updatedCart = cartService.addItemsToCart("user1", items);
//        assertNotNull(updatedCart, "Cart should not be null");
//        assertEquals(1, updatedCart.getInventoryItems().size(), "Cart should contain one item");
//    }
//
//    @Test
//    public void testRemoveItemsFromCart_Success() throws Exception {
//        userService.register("user1", "password", "user1@example.com", "1234567890", false, 0, 0);
//        UserContext.setUserId("user1");
//
//        Book book = new Book("1234567890123", "Test Book", 2023, "Test Publisher", false, null, null);
//        bookService.addBook(book);
//
//        InventoryItem inventoryItem = new InventoryItem(0, book, 19.99, 10, "Test Description");
//        inventoryService.addInventoryItem(inventoryItem);
//
//        List<InventoryItem> items = new ArrayList<>();
//        items.add(inventoryItem);
//
//        cartService.addItemsToCart("user1", items);
//
//        List<Integer> itemIDs = new ArrayList<>();
//        itemIDs.add(inventoryItem.getInventoryItemID());
//
//        Cart updatedCart = cartService.removeItemsFromCart("user1", itemIDs);
//        assertNotNull(updatedCart, "Cart should not be null");
//        assertEquals(0, updatedCart.getInventoryItems().size(), "Cart should have no items");
//    }
//
//    @Test
//    public void testCheckout_Success() throws Exception {
//        userService.register("user1", "password", "user1@example.com", "1234567890", false, 0, 0);
//        UserContext.setUserId("user1");
//
//        Book book = new Book("1234567890123", "Test Book", 2023, "Test Publisher", false, null, null);
//        bookService.addBook(book);
//
//        InventoryItem inventoryItem = new InventoryItem(0, book, 19.99, 10, "Test Description");
//        inventoryService.addInventoryItem(inventoryItem);
//
//        List<InventoryItem> items = new ArrayList<>();
//        items.add(inventoryItem);
//
//        cartService.addItemsToCart("user1", items);
//
//        Order order = cartService.checkout("user1", 1); // Assuming addressID 1 exists
//        assertNotNull(order, "Order should not be null");
//        assertEquals(19.99, order.getTotal(), 0.01, "Order total should match cart total");
//    }
//    
//    @Test
//    public void testAdminAccessingAnotherUsersCart() {
//        try {
//            // Register an admin user and a regular user
//            userService.register("adminUser", "adminPass", "admin@example.com", "0987654321", true, 0, 0);
//            userService.register("regularUser", "password", "regular@example.com", "1234567890", false, 0, 0);
//
//            // Set up the regular user's cart
//            UserContext.setUserId("regularUser");
//            Book book = new Book("1234567890123", "Test Book", 2023, "Test Publisher", false, null, null);
//            bookService.addBook(book);
//
//            InventoryItem inventoryItem = new InventoryItem(0, book, 19.99, 10, "Test Description");
//            inventoryService.addInventoryItem(inventoryItem);
//
//            List<InventoryItem> items = new ArrayList<>();
//            items.add(inventoryItem);
//
//            cartService.addItemsToCart("regularUser", items);
//
//            // Switch to admin user and access the regular user's cart
//            UserContext.setUserId("adminUser");
//            Cart cart = cartService.getUserCart("regularUser");
//
//            assertNotNull(cart, "Cart should not be null");
//            assertEquals("regularUser", cart.getUsername(), "Cart username should match the regular user's username");
//            assertEquals(1, cart.getInventoryItems().size(), "Cart should contain one item");
//        } catch (Exception e) {
//            fail("Unexpected exception during test: " + e.getMessage());
//        }
//    }
}