package com.bookie.bizlogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bookie.auth.AuthorizationProxy;
import com.bookie.auth.UserContext;
import com.bookie.dao.OrderDAO;
import com.bookie.dao.UserDAO;
import com.bookie.models.Order;
import com.bookie.models.User;

public class OrderServiceTest {

    private OrderServiceInterface orderService;
    private UserDAO userDAO;
    private OrderDAO orderDAO;

    @BeforeEach
    public void setUp() {
        // Initialize DAOs
        userDAO = new UserDAO();
        orderDAO = new OrderDAO();

        // Create a proxied instance of OrderService
        orderService = AuthorizationProxy.createProxy(new OrderService());
    }

    @AfterEach
    public void cleanUp() {
        // Clean up Orders and Users after each test to ensure test isolation
        try (Statement stmt = orderDAO.getConnection().createStatement()) {
            stmt.execute("DELETE FROM Orders");
            stmt.execute("DELETE FROM Users");
            UserContext.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    /**
//     * Test retrieving an order by ID with proper authorization
//     */
//    @Test
//    public void testGetOrderByID_Success() {
//        try {
//            // Step 1: Register a user and set context
//            userDAO.add(new User("user1", "pass", "user1@example.com", "1234567890", false, 0, 0));
//            UserContext.setUserId("user1");
//
//            // Step 2: Insert an order for the user
//            orderDAO.getConnection().createStatement().execute(
//                "INSERT INTO Orders (orderID, username, total, addressID, orderStatus, orderDate) " +
//                "VALUES (1, 'user1', 100.0, 1, 'Pending', NOW())"
//            );
//
//            // Step 3: Retrieve the order
//            Order fetchedOrder = orderService.getOrderByID(1, "user1");
//            assertNotNull(fetchedOrder, "Order should not be null");
//            assertEquals(1, fetchedOrder.getOrderID(), "Order ID should match");
//
//        } catch (Exception e) {
//            fail("Unexpected exception during test: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Test retrieving an order by ID with unauthorized access
//     */
//    @Test
//    public void testGetOrderByID_AccessDenied() {
//        try {
//            // Step 1: Register two users
//            userDAO.add(new User("user1", "pass", "user1@example.com", "1234567890", false, 0, 0));
//            userDAO.add(new User("user2", "pass", "user2@example.com", "0987654321", false, 0, 0));
//
//            // Step 2: Insert an order for user1
//            orderDAO.getConnection().createStatement().execute(
//                "INSERT INTO Orders (orderID, username, total, addressID, orderStatus, orderDate) " +
//                "VALUES (1, 'user1', 100.0, 1, 'Pending', NOW())"
//            );
//
//            // Step 3: Set context to user2 and attempt to access user1's order
//            UserContext.setUserId("user2");
//            Exception exception = assertThrows(SecurityException.class, () -> {
//                orderService.getOrderByID(1, "user1");
//            });
//            assertEquals("Access denied: User mismatch", exception.getMessage());
//
//        } catch (SQLException e) {
//            fail("SQLException during test: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Test retrieving all orders for a user
//     */
//    @Test
//    public void testGetAllOrdersForUser_Success() {
//        try {
//            // Step 1: Register a user and set context
//            userDAO.add(new User("user1", "pass", "user1@example.com", "1234567890", false, 0, 0));
//            UserContext.setUserId("user1");
//
//            // Step 2: Insert multiple orders for the user
//            orderDAO.getConnection().createStatement().execute(
//                "INSERT INTO Orders (orderID, username, total, addressID, orderStatus, orderDate) " +
//                "VALUES (1, 'user1', 100.0, 1, 'Pending', NOW())," +
//                "(2, 'user1', 50.0, 1, 'Shipped', NOW())"
//            );
//
//            // Step 3: Retrieve all orders for the user
//            List<Order> orders = orderService.getAllOrdersForUser("user1");
//            assertNotNull(orders, "Orders list should not be null");
//            assertEquals(2, orders.size(), "There should be two orders");
//
//        } catch (Exception e) {
//            fail("Unexpected exception during test: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Test updating order status
//     */
//    @Test
//    public void testUpdateOrderStatus_Success() {
//        try {
//            // Step 1: Register an admin user and set context
//            userDAO.add(new User("adminUser", "adminPass", "admin@example.com", "0987654321", true, 0, 0));
//            UserContext.setUserId("adminUser");
//
//            // Step 2: Insert an order
//            orderDAO.getConnection().createStatement().execute(
//                "INSERT INTO Orders (orderID, username, total, addressID, orderStatus, orderDate) " +
//                "VALUES (1, 'adminUser', 100.0, 1, 'Pending', NOW())"
//            );
//
//            // Step 3: Update order status
//            boolean isUpdated = orderService.updateOrderStatus(1, "Shipped");
//            assertTrue(isUpdated, "Order status should be updated");
//
//        } catch (SQLException e) {
//            fail("SQLException during test: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Test canceling an order
//     */
//    @Test
//    public void testCancelOrder_Success() {
//        try {
//            // Step 1: Register a user and set context
//            userDAO.add(new User("user1", "pass", "user1@example.com", "1234567890", false, 0, 0));
//            UserContext.setUserId("user1");
//
//            // Step 2: Insert an order
//            orderDAO.getConnection().createStatement().execute(
//                "INSERT INTO Orders (orderID, username, total, addressID, orderStatus, orderDate) " +
//                "VALUES (1, 'user1', 100.0, 1, 'Pending', NOW())"
//            );
//
//            // Step 3: Cancel the order
//            boolean isCancelled = orderService.cancelOrder(1, "user1");
//            assertTrue(isCancelled, "Order should be canceled");
//
//        } catch (Exception e) {
//            fail("Unexpected exception during test: " + e.getMessage());
//        }
//    }
}