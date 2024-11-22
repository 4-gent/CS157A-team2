package com.bookie.bizlogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bookie.auth.AuthorizationProxy;
import com.bookie.auth.UserContext;
import com.bookie.bizlogic.interfaces.BookServiceInterface;
import com.bookie.bizlogic.interfaces.CartServiceInterface;
import com.bookie.bizlogic.interfaces.InventoryServiceInterface;
import com.bookie.bizlogic.interfaces.OrderServiceInterface;
import com.bookie.bizlogic.interfaces.UserServiceInterface;
import com.bookie.dao.UserDAO;
import com.bookie.models.Address;
import com.bookie.models.Book;
import com.bookie.models.CartItem;
import com.bookie.models.InventoryItem;
import com.bookie.models.Order;
import com.bookie.models.PaymentInfo;
import com.bookie.models.User;

public class OrderServiceTest {

    private OrderServiceInterface orderService;
    private UserServiceInterface userService;
    private CartServiceInterface cartService;
    private BookServiceInterface bookService;
    private InventoryServiceInterface inventoryService;
    private UserDAO userDAO; 

    @BeforeEach
    public void setUp() {
        userService = AuthorizationProxy.createProxy(new UserService());
        cartService = AuthorizationProxy.createProxy(new CartService());
        bookService = AuthorizationProxy.createProxy(new BookService());
        inventoryService = AuthorizationProxy.createProxy(new InventoryService());
        orderService = AuthorizationProxy.createProxy(new OrderService());
        userDAO = new UserDAO();
    }

    @AfterEach
    public void cleanUp() {
        try (Statement stmt = userDAO.getConnection().createStatement()) {
            stmt.execute("DELETE FROM Contains");
            stmt.execute("DELETE FROM Orders");
            stmt.execute("DELETE FROM Includes");
            stmt.execute("DELETE FROM Cart");
            stmt.execute("DELETE FROM InventoryItems");
            stmt.execute("DELETE FROM Books");
            stmt.execute("DELETE FROM PaymentDetails");
            stmt.execute("DELETE FROM Addresses");
            stmt.execute("DELETE FROM Users");
            UserContext.clear();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to clean up the database: " + e.getMessage());
        }
    }

    @Test
    public void testGetOrderByID_Success() {
        try {
            // Step 1: Register a user
            User user = userService.register("user1", "password", "user1@example.com", "1234567890", false, 0, 0);
            assertNotNull(user, "User should be registered successfully");
            UserContext.setUserId("user1");

            // Step 2: Add address
            Address address = new Address(0, "123 Main St", "Cityville", "Stateville", "12345", "Country");
            Address savedAddress = new AddressService().addAddress(address);
            assertNotNull(savedAddress, "Address should be saved successfully");

            // Step 3: Add payment details
            PaymentInfo paymentInfo = new PaymentInfo(0, "user1", "4111111111111111", "12/25", "John Doe", "123", savedAddress, false);
            PaymentInfo savedPaymentInfo = new PaymentInfoService().addPaymentDetailsForUser(paymentInfo);
            assertNotNull(savedPaymentInfo, "Payment info should be saved successfully");

         // Step 1: Register a admin user
            User adminUser = userService.register("adminUser", "password", "admin@example.com", "1234567890", true, 0, 0);
            assertNotNull(adminUser, "User should be registered successfully");
            UserContext.setUserId("adminUser");
            
            // Step 4: Add a book and inventory item
            Book book = new Book("1234567890123", "Test Book", 2023, "Test Publisher", false, null);
            Book savedBook = bookService.addBook(book);
            assertNotNull(savedBook, "Book should be saved successfully");

            InventoryItem item = new InventoryItem(0, savedBook, 100.0, 10, "Test Inventory Item");
            InventoryItem savedItem = inventoryService.addInventoryItem(item);
            assertNotNull(savedItem, "Inventory item should be saved successfully");

            //switch user context to user1
            UserContext.setUserId("user1");
            // Step 5: Add item to cart and checkout
            cartService.addItemsToCart("user1", List.of(new CartItem(savedItem,1)));
            Order createdOrder = cartService.checkout("user1", savedAddress.getAddressID(), savedPaymentInfo.getPaymentID());
            assertNotNull(createdOrder, "Order should be created successfully");

            // Step 6: Retrieve the order
            Order fetchedOrder = orderService.getOrderByID(createdOrder.getOrderID(), "user1");
            assertNotNull(fetchedOrder, "Fetched order should not be null");
            assertEquals(createdOrder.getOrderID(), fetchedOrder.getOrderID(), "Order ID should match");
        } catch (Exception e) {
            fail("Unexpected exception during test: " + e.getMessage());
        }
    }

    @Test
    public void testGetOrderByID_AccessDenied() {
        try {
            User admin = userService.register("admin", "password", "user1@example.com", "1234567890", true, 0, 0);
            UserContext.setUserId("admin");
            
            Book book = new Book("1234567890123", "Test Book", 2023, "Test Publisher", false, null);
            Book savedBook = bookService.addBook(book);

            InventoryItem item = new InventoryItem(0, savedBook, 100.0, 10, "Test Inventory Item");
            InventoryItem savedItem = inventoryService.addInventoryItem(item);

            // Step 1: Register two users
            User user1 = userService.register("user1", "password", "user1@example.com", "1234567890", false, 0, 0);
            User user2 = userService.register("user2", "password", "user2@example.com", "0987654321", false, 0, 0);
            assertNotNull(user1);
            assertNotNull(user2);

            UserContext.setUserId("user1");

            // Step 2: Add address, payment details, and book for user1
            Address address = new Address(0, "123 Main St", "Cityville", "Stateville", "12345", "Country");
            Address savedAddress = new AddressService().addAddress(address);

            PaymentInfo paymentInfo = new PaymentInfo(0, "user1", "4111111111111111", "12/25", "John Doe", "123", savedAddress, false);
            PaymentInfo savedPaymentInfo = new PaymentInfoService().addPaymentDetailsForUser(paymentInfo);

            cartService.addItemsToCart("user1", List.of(new CartItem(savedItem,1)));
            Order createdOrder = cartService.checkout("user1", savedAddress.getAddressID(), savedPaymentInfo.getPaymentID());
            assertNotNull(createdOrder);

            // Step 3: Set context to user2 and try accessing user1's order
            UserContext.setUserId("user2");
            Exception exception = assertThrows(SecurityException.class, () -> {
                orderService.getOrderByID(createdOrder.getOrderID(), "user1");
            });
            assertEquals("Access denied: Admin or user match required", exception.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception during test: " + e.getMessage());
        }
    }

    @Test
    public void testGetAllOrdersForUser_Success() {
        try {
            User admin = userService.register("admin", "password", "user1@example.com", "1234567890", true, 0, 0);
            assertNotNull(admin, "User should be registered successfully");
            UserContext.setUserId("admin");
            
            Book book = new Book("1234567890123", "Test Book", 2023, "Test Publisher", false, null);
            Book savedBook = bookService.addBook(book);

            InventoryItem item = new InventoryItem(0, savedBook, 100.0, 10, "Test Inventory Item");
            InventoryItem savedItem = inventoryService.addInventoryItem(item);

            User user = userService.register("user1", "password", "user1@example.com", "1234567890", false, 0, 0);
            assertNotNull(user, "User should be registered successfully");
            UserContext.setUserId("user1");

            // Step 2: Add address and payment details
            Address address = new Address(0, "123 Main St", "Cityville", "Stateville", "12345", "Country");
            Address savedAddress = new AddressService().addAddress(address);

            PaymentInfo paymentInfo = new PaymentInfo(0, "user1", "4111111111111111", "12/25", "John Doe", "123", savedAddress, false);
            PaymentInfo savedPaymentInfo = new PaymentInfoService().addPaymentDetailsForUser(paymentInfo);


            cartService.addItemsToCart("user1", List.of(new CartItem(savedItem,1)));
            cartService.checkout("user1", savedAddress.getAddressID(), savedPaymentInfo.getPaymentID());

            cartService.addItemsToCart("user1", List.of(new CartItem(savedItem,1)));
            cartService.checkout("user1", savedAddress.getAddressID(), savedPaymentInfo.getPaymentID());

            // Step 4: Fetch all orders
            List<Order> orders = orderService.getAllOrdersForUser("user1");
            assertNotNull(orders, "Orders list should not be null");
            assertEquals(2, orders.size(), "There should be two orders");
        } catch (Exception e) {
            fail("Unexpected exception during test: " + e.getMessage());
        }
    }
}