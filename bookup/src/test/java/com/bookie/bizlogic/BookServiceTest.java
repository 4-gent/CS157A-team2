package com.bookie.bizlogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import com.bookie.dao.UserDAO;
import com.bookie.models.Book;

public class BookServiceTest {

    private BookServiceInterface bookService;
    private UserServiceInterface userService;

    private BookDAO bookDAO;
    private UserDAO userDAO;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a proxied instance of BookService
        bookService = AuthorizationProxy.createProxy(new BookService());
        userService = AuthorizationProxy.createProxy(new UserService());
    }

    @AfterEach
    public void tearDown() {
        UserContext.clear();
     // Initialize DAOs to access the database connection
        bookDAO = new BookDAO();
        userDAO = new UserDAO();

        // Clean up Users and Books
        try {
			userDAO.getConnection().createStatement().execute("DELETE FROM Users");
			bookDAO.getConnection().createStatement().execute("DELETE FROM Books");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Test
    public void testAddBook_AccessDenied() {
        try {
            userService.register("user1", "password", "user1@example.com", "1234567890", false, 0, 0);
            UserContext.setUserId("user1");

            Exception exception = assertThrows(SecurityException.class, () -> {
                bookService.addBook(new Book("1234567890123", "Test Book", 2023, "Test Publisher", false, null, null));
            });
            assertEquals("Access denied: Admin privileges required", exception.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception during test: " + e.getMessage());
        }
    }

    @Test
    public void testAddBook_Success() {
        try {
            userService.register("adminUser", "adminPass", "admin@example.com", "0987654321", true, 0, 0);
            UserContext.setUserId("adminUser");

            Book book = new Book("1234567890123", "Test Book", 2023, "Test Publisher", false, null, null);
            Book addedBook = bookService.addBook(book);
            assertNotNull(addedBook, "Book should be added successfully");
            assertEquals("Test Book", addedBook.getTitle());
        } catch (Exception e) {
            fail("Unexpected exception during test: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateBook_Success() {
        try {
            userService.register("adminUser", "adminPass", "admin@example.com", "0987654321", true, 0, 0);
            UserContext.setUserId("adminUser");

            Book book = new Book("1234567890123", "Test Book", 2023, "Test Publisher", false, null, null);
            bookService.addBook(book);

            book.setTitle("Updated Title");
            assertTrue(bookService.updateBook(book), "Book should be updated successfully");

            Book updatedBook = bookService.getBookByISBN("1234567890123");
            assertEquals("Updated Title", updatedBook.getTitle());
        } catch (Exception e) {
            fail("Unexpected exception during test: " + e.getMessage());
        }
    }

    @Test
    public void testBrowseBooks() {
        try {
        	//FIXME fix the framework not to have a user for methods with no authorization check
        	userService.register("adminUser", "adminPass", "admin@example.com", "0987654321", true, 0, 0);
            UserContext.setUserId("adminUser");

            Book book1 = new Book("1234567890123", "Test Book 1", 2023, "Test Publisher 1", false, null, null);
            Book book2 = new Book("1234567890124", "Test Book 2", 2023, "Test Publisher 2", false, null, null);
            bookService.addBook(book1);
            bookService.addBook(book2);

            List<Book> books = bookService.browseBooks();
            assertEquals(2, books.size(), "There should be two books in the system");
        } catch (Exception e) {
            fail("Unexpected exception during test: " + e.getMessage());
        }
    }
}