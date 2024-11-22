package com.bookie.bizlogic;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import com.bookie.auth.UserContext;
import com.bookie.bizlogic.interfaces.AuthorServiceInterface;
import com.bookie.auth.AuthorizationProxy;
import com.bookie.dao.UserDAO;
import com.bookie.models.Author;
import com.bookie.models.User;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class AuthorServiceTest {

    private AuthorServiceInterface authorService;
    private UserDAO userDAO;

    @BeforeEach
    public void setUp() {
        // Initialize DAOs
        userDAO = new UserDAO();

        // Create a proxied instance of AuthorService
        authorService = AuthorizationProxy.createProxy(new AuthorService());
    }

    @AfterEach
    public void cleanUp() {
        // Clean up Users and Authors after each test to ensure test isolation
        try (Statement stmt = userDAO.getConnection().createStatement()) {
            stmt.execute("DELETE FROM Users");
            stmt.execute("DELETE FROM Authors");
            UserContext.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test adding an author with admin privileges
     */
    @Test
    public void testAddAuthor_Success() {
        try {
            // Step 1: Register an admin user
            userDAO.add(new User("adminUser", "adminPass", "admin@example.com", "1234567890", true, 0, 0));
            
            // Step 2: Set the context to admin user
            UserContext.setUserId("adminUser");

            // Step 3: Add an author as an admin
            Author newAuthor = new Author("J.K. Rowling");
            Author addedAuthor = authorService.addAuthor(newAuthor);

            assertNotNull(addedAuthor, "Author should be added successfully");
            assertEquals("J.K. Rowling", addedAuthor.getName(), "Author name should match");

        } catch (SQLException e) {
            fail("SQLException during test: " + e.getMessage());
        }
    }

    /**
     * Test adding an author with a non-admin user (Access Denied)
     */
    @Test
    public void testAddAuthor_AccessDenied() {
        try {
            // Step 1: Register a non-admin user
            userDAO.add(new User("nonAdminUser", "pass", "user@example.com", "0987654321", false, 0, 0));

            // Step 2: Set the context to non-admin user
            UserContext.setUserId("nonAdminUser");

            // Step 3: Attempt to add an author as a non-admin user
            Exception exception = assertThrows(SecurityException.class, () -> {
                authorService.addAuthor(new Author("New Author"));
            });
            assertEquals("Access denied: Admin privileges required", exception.getMessage());

        } catch (SQLException e) {
            fail("SQLException during test: " + e.getMessage());
        }
    }

    /**
     * Test retrieving all authors
     */
    @Test
    public void testGetAuthors() {
        try {
            // Step 1: Register an admin user and set context
            userDAO.add(new User("adminUser", "adminPass", "admin@example.com", "1234567890", true, 0, 0));
            UserContext.setUserId("adminUser");

            // Step 2: Add some authors
            authorService.addAuthor(new Author("George Orwell"));
            authorService.addAuthor(new Author("Jane Austen"));

            // Step 3: Retrieve the list of authors
            List<Author> authors = authorService.getAuthors();
            assertNotNull(authors, "Authors list should not be null");
            assertTrue(authors.size() >= 2, "There should be at least 2 authors");

        } catch (SQLException e) {
            fail("SQLException during test: " + e.getMessage());
        }
    }
}