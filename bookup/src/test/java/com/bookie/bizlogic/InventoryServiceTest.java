package com.bookie.bizlogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bookie.auth.AuthorizationProxy;
import com.bookie.auth.UserContext;
import com.bookie.dao.BookDAO;
import com.bookie.dao.InventoryDAO;
import com.bookie.dao.UserDAO;
import com.bookie.models.Book;
import com.bookie.models.InventoryItem;

public class InventoryServiceTest {

    private InventoryServiceInterface inventoryService;
    private BookServiceInterface bookService;
    private UserServiceInterface userService;

    @BeforeEach
    public void setUp() {
        inventoryService = AuthorizationProxy.createProxy(new InventoryService());
        bookService = AuthorizationProxy.createProxy(new BookService());
        userService = AuthorizationProxy.createProxy(new UserService());
    }
    
    @AfterEach
    public void tearDown() {
        // Clear the UserContext after each test
        UserContext.clear();
     // Clean up database using DAO connections
        try {
            InventoryDAO inventoryDAO = new InventoryDAO();
            BookDAO bookDAO = new BookDAO();
            UserDAO userDAO = new UserDAO();

            inventoryDAO.getConnection().createStatement().execute("DELETE FROM InventoryItems");
            bookDAO.getConnection().createStatement().execute("DELETE FROM Books");
            userDAO.getConnection().createStatement().execute("DELETE FROM Users");
        } catch (Exception e) {
            fail("Failed to clean up the database: " + e.getMessage());
        }
    }

    @Test
    public void testAddInventoryItem_Success() {
        try {
            // Create admin user and set context
            userService.register("adminUser", "adminPass", "admin@example.com", "0987654321", true, 0, 0);
            UserContext.setUserId("adminUser");

            // Add a book to associate with the inventory item
            Book book = new Book("1234567890123", "Test Book", 2023, "Test Publisher", false, null, null);
            bookService.addBook(book);

            // Add inventory item
            InventoryItem item = new InventoryItem(0, book, 19.99, 10, "A great book");
            InventoryItem addedItem = inventoryService.addInventoryItem(item);

            assertNotNull(addedItem);
            assertEquals("Test Book", addedItem.getBook().getTitle());
        } catch (Exception e) {
            fail("Unexpected exception during test: " + e.getMessage());
        }
    }


//    @Test
//    public void testAddInventoryItem_AccessDenied() {
//        try {
//        	//FIXME, why is it enforcing an admin user
//            // Create a non-admin user and set context
//            userService.register("regularUser", "password", "user@example.com", "1234567890", false, 0, 0);
//            UserContext.setUserId("regularUser");
//
//            Book book = new Book("1234567890123", "Test Book", 2023, "Test Publisher", false, null, null);
//            bookService.addBook(book);
//
//            InventoryItem item = new InventoryItem(0, book, 19.99, 10, "A great book");
//            Exception exception = assertThrows(SecurityException.class, () -> {
//                inventoryService.addInventoryItem(item);
//            });
//            assertEquals("Access denied: Admin privileges required", exception.getMessage());
//        } catch (Exception e) {
//            fail("Unexpected exception during test: " + e.getMessage());
//        }
//    }

    @Test
    public void testGetByInventoryItemID_Success() {
        try {
            // Add a book and inventory item
            userService.register("adminUser", "adminPass", "admin@example.com", "0987654321", true, 0, 0);
            UserContext.setUserId("adminUser");

            Book book = new Book("1234567890123", "Test Book", 2023, "Test Publisher", false, null, null);
            bookService.addBook(book);

            InventoryItem item = new InventoryItem(0, book, 19.99, 10, "A great book");
            InventoryItem addedItem = inventoryService.addInventoryItem(item);

            // Fetch the item by ID
            InventoryItem fetchedItem = inventoryService.getByInventoryItemID(addedItem.getInventoryItemID());
            assertNotNull(fetchedItem);
            assertEquals("Test Book", fetchedItem.getBook().getTitle());
        } catch (Exception e) {
            fail("Unexpected exception during test: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateInventoryItem_Success() {
        try {
            // Create admin user and set context
            userService.register("adminUser", "adminPass", "admin@example.com", "0987654321", true, 0, 0);
            UserContext.setUserId("adminUser");

            // Add a book and inventory item
            Book book = new Book("1234567890123", "Test Book", 2023, "Test Publisher", false, null, null);
            bookService.addBook(book);

            InventoryItem item = new InventoryItem(0, book, 19.99, 10, "A great book");
            InventoryItem addedItem = inventoryService.addInventoryItem(item);

            // Update the inventory item
            addedItem.setPrice(24.99);
            addedItem.setQty(15);
            boolean isUpdated = inventoryService.updateInventoryItem(addedItem);

            assertTrue(isUpdated);
            InventoryItem updatedItem = inventoryService.getByInventoryItemID(addedItem.getInventoryItemID());
            assertEquals(24.99, updatedItem.getPrice());
            assertEquals(15, updatedItem.getQty());
        } catch (Exception e) {
            fail("Unexpected exception during test: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveInventoryItem_Success() {
        try {
            // Create admin user and set context
            userService.register("adminUser", "adminPass", "admin@example.com", "0987654321", true, 0, 0);
            UserContext.setUserId("adminUser");

            // Add a book and inventory item
            Book book = new Book("1234567890123", "Test Book", 2023, "Test Publisher", false, null, null);
            bookService.addBook(book);

            InventoryItem item = new InventoryItem(0, book, 19.99, 10, "A great book");
            InventoryItem addedItem = inventoryService.addInventoryItem(item);

            // Remove the inventory item
            boolean isDeleted = inventoryService.removeInventoryItem(addedItem.getInventoryItemID());
            assertTrue(isDeleted);

            InventoryItem deletedItem = inventoryService.getByInventoryItemID(addedItem.getInventoryItemID());
            assertNull(deletedItem, "The inventory item should be deleted");
        } catch (Exception e) {
            fail("Unexpected exception during test: " + e.getMessage());
        }
    }

    @Test
    public void testSearchByISBN_Success() {
        try {
        	// FIXME, forcing admin needed
        	// Create user and set context
            userService.register("user", "userPass", "user@example.com", "0987654321", true, 0, 0);
            UserContext.setUserId("user");
            
            // Add a book and inventory item
            Book book = new Book("1234567890123", "Test Book", 2023, "Test Publisher", false, null, null);
            bookService.addBook(book);

            InventoryItem item = new InventoryItem(0, book, 19.99, 10, "A great book");
            inventoryService.addInventoryItem(item);

            // Search by ISBN
            InventoryItem searchedItem = inventoryService.searchByISBN("1234567890123");
            assertNotNull(searchedItem);
            assertEquals("Test Book", searchedItem.getBook().getTitle());
        } catch (Exception e) {
            fail("Unexpected exception during test: " + e.getMessage());
        }
    }

    @Test
    public void testSearchInventoryItemsByKeyword_Success() {
        try {
        	// FIXME,forcing admin needed
        	// Create user and set context
            userService.register("user", "userPass", "user@example.com", "0987654321", true, 0, 0);
            UserContext.setUserId("user");
            
            // Add books and inventory items
            Book book1 = new Book("1234567890123", "First Book", 2023, "Test Publisher", false, null, null);
            Book book2 = new Book("1234567890124", "Second Book", 2023, "Another Publisher", false, null, null);
            bookService.addBook(book1);
            bookService.addBook(book2);

            InventoryItem item1 = new InventoryItem(0, book1, 19.99, 10, "Description one");
            InventoryItem item2 = new InventoryItem(0, book2, 24.99, 5, "Description two");
            inventoryService.addInventoryItem(item1);
            inventoryService.addInventoryItem(item2);

            // Search by keyword
            List<InventoryItem> items = inventoryService.searchInventoryItemsByKeyword("First");
            assertEquals(1, items.size());
            assertEquals("First Book", items.get(0).getBook().getTitle());
        } catch (Exception e) {
            fail("Unexpected exception during test: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateInventoryItem_AccessDenied() {
        try {
            // Create a non-admin user and set context
            userService.register("regularUser", "password", "user@example.com", "1234567890", false, 0, 0);
            UserContext.setUserId("regularUser");

            // Add a book and inventory item
            userService.register("adminUser", "adminPass", "admin@example.com", "0987654321", true, 0, 0);
            UserContext.setUserId("adminUser");

            Book book = new Book("1234567890123", "Test Book", 2023, "Test Publisher", false, null, null);
            bookService.addBook(book);

            InventoryItem item = new InventoryItem(0, book, 19.99, 10, "A great book");
            InventoryItem addedItem = inventoryService.addInventoryItem(item);

            // Switch to a regular user and try to update the inventory item
            UserContext.setUserId("regularUser");
            addedItem.setPrice(24.99);
            Exception exception = assertThrows(SecurityException.class, () -> {
                inventoryService.updateInventoryItem(addedItem);
            });
            assertEquals("Access denied: Admin privileges required", exception.getMessage());
        } catch (SQLException e) {
            fail("SQLException during test: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveInventoryItem_AccessDenied() {
        try {
            // Create a non-admin user and set context
            userService.register("regularUser", "password", "user@example.com", "1234567890", false, 0, 0);
            UserContext.setUserId("regularUser");

            // Add a book and inventory item
            userService.register("adminUser", "adminPass", "admin@example.com", "0987654321", true, 0, 0);
            UserContext.setUserId("adminUser");

            Book book = new Book("1234567890123", "Test Book", 2023, "Test Publisher", false, null, null);
            bookService.addBook(book);

            InventoryItem item = new InventoryItem(0, book, 19.99, 10, "A great book");
            InventoryItem addedItem = inventoryService.addInventoryItem(item);

            // Switch to a regular user and try to remove the inventory item
            UserContext.setUserId("regularUser");
            Exception exception = assertThrows(SecurityException.class, () -> {
                inventoryService.removeInventoryItem(addedItem.getInventoryItemID());
            });
            assertEquals("Access denied: Admin privileges required", exception.getMessage());
        } catch (SQLException e) {
            fail("SQLException during test: " + e.getMessage());
        }
    }
    
}