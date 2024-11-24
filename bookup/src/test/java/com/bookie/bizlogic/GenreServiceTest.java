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

import com.bookie.auth.UserContext;
import com.bookie.bizlogic.interfaces.GenreServiceInterface;
import com.bookie.dao.UserDAO;
import com.bookie.models.Genre;
import com.bookie.models.User;

public class GenreServiceTest {

    private GenreServiceInterface genreService;
    private UserDAO userDAO;

    @BeforeEach
    public void setUp() {
        // Initialize DAOs
        userDAO = new UserDAO();

        // Create a proxied instance of GenreService
        genreService = GenreService.getServiceInstance();
    }

    @AfterEach
    public void cleanUp() {
        // Clean up Users and Genres after each test to ensure test isolation
        try (Statement stmt = userDAO.getConnection().createStatement()) {
            stmt.execute("DELETE FROM Users");
            stmt.execute("DELETE FROM Genres");
            UserContext.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test adding a genre with admin privileges
     */
    @Test
    public void testAddGenre_Success() {
        try {
            // Step 1: Register an admin user
            userDAO.add(new User("adminUser", "adminPass", "admin@example.com", "1234567890", true, 0, 0));

            // Step 2: Set the context to admin user
            UserContext.setUserId("adminUser");

            // Step 3: Add a genre as an admin
            Genre newGenre = new Genre("Science Fiction");
            Genre addedGenre = genreService.addGenre(newGenre);

            assertNotNull(addedGenre, "Genre should be added successfully");
            assertEquals("Science Fiction", addedGenre.getName(), "Genre name should match");

        } catch (SQLException e) {
            fail("SQLException during test: " + e.getMessage());
        }
    }

    /**
     * Test adding a genre with a non-admin user (Access Denied)
     */
    @Test
    public void testAddGenre_AccessDenied() {
        try {
            // Step 1: Register a non-admin user
            userDAO.add(new User("nonAdminUser", "userPass", "user@example.com", "0987654321", false, 0, 0));

            // Step 2: Set the context to non-admin user
            UserContext.setUserId("nonAdminUser");

            // Step 3: Attempt to add a genre as a non-admin user
            Exception exception = assertThrows(SecurityException.class, () -> {
                genreService.addGenre(new Genre("Mystery"));
            });
            assertEquals("Access denied: Admin privileges required", exception.getMessage());

        } catch (SQLException e) {
            fail("SQLException during test: " + e.getMessage());
        }
    }

    /**
     * Test retrieving all genres
     */
    @Test
    public void testGetGenres() {
        try {
            // Step 1: Register an admin user and set context
            userDAO.add(new User("adminUser", "adminPass", "admin@example.com", "1234567890", true, 0, 0));
            UserContext.setUserId("adminUser");

            // Step 2: Add some genres
            genreService.addGenre(new Genre("Fantasy"));
            genreService.addGenre(new Genre("Non-fiction"));

            // Step 3: Retrieve the list of genres
            List<Genre> genres = genreService.getGenres();
            assertNotNull(genres, "Genres list should not be null");
            assertTrue(genres.size() >= 2, "There should be at least 2 genres");

        } catch (SQLException e) {
            fail("SQLException during test: " + e.getMessage());
        }
    }
}