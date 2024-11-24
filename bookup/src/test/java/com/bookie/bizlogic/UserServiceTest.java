package com.bookie.bizlogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bookie.auth.UserContext;
import com.bookie.bizlogic.interfaces.AuthorServiceInterface;
import com.bookie.bizlogic.interfaces.GenreServiceInterface;
import com.bookie.bizlogic.interfaces.UserServiceInterface;
import com.bookie.dao.CartDAO;
import com.bookie.dao.UserDAO;
import com.bookie.models.Author;
import com.bookie.models.Cart;
import com.bookie.models.Genre;
import com.bookie.models.User;

public class UserServiceTest {

    private UserServiceInterface userService;
    private AuthorServiceInterface authorService;
    private GenreServiceInterface genreService;
    private UserDAO userDAO;

    @BeforeEach
    public void setUp() {
        // Initialize DAOs for database cleanup
        userDAO = new UserDAO();
        
        cleanDB();

        userService = UserService.getServiceInstance();
        authorService = AuthorService.getServiceInstance();
        genreService = GenreService.getServiceInstance();
    }

    @AfterEach
    public void cleanUp() {
    	cleanDB();
    }
    
    private void cleanDB() {
    	try (Statement stmt = userDAO.getConnection().createStatement()) {
    		stmt.execute("DELETE FROM Users");
            stmt.execute("DELETE FROM Authors");
            stmt.execute("DELETE FROM Genres");
            UserContext.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    

    @Test
    public void testRegister_Success() throws Exception {
        try {
            User newUser = userService.register("johnDoe", "password123", "john@example.com", "1234567890", false, 0, 0);
            assertNotNull(newUser, "User should be registered successfully");
            assertEquals("johnDoe", newUser.getUsername());
        } catch (SQLException e) {
            fail("SQLException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testRegister_UserAlreadyExists() {
        try {
            // Register the first user
            userService.register("janeDoe", "password123", "jane@example.com", "0987654321", false, 0, 0);

            // Attempt to register the same user again and expect an exception
            assertThrows(Exception.class, () -> {
                userService.register("janeDoe", "password123", "jane@example.com", "0987654321", false, 0, 0);
            });

        } catch (Exception e) {
            fail("Unexpected exception during test: " + e.getMessage());
        }
    }

    @Test
    public void testLogin_Success() throws Exception {
        try {
            userService.register("alice", "securePass", "alice@example.com", "5555555555", false, 0, 0);
            assertTrue(userService.login("alice", "securePass"), "User should be able to login");
        } catch (SQLException e) {
            fail("SQLException during test: " + e.getMessage());
        }
    }

    @Test
    public void testResetPassword_Success() throws Exception {
        try {
            userService.register("bob", "initialPass", "bob@example.com", "2223334444", false, 0, 0);
            UserContext.setUserId("bob");

            assertTrue(userService.resetPassword("bob", "initialPass", "newPass"), "Password should be reset successfully");

            // Verify login with new password
            assertTrue(userService.login("bob", "newPass"));
        } catch (SQLException e) {
            fail("SQLException during test: " + e.getMessage());
        }
    }

    @Test
    public void testResetPassword_Failure_WrongOldPassword() throws Exception {
        try {
            userService.register("charlie", "charliePass", "charlie@example.com", "7778889990", false, 0, 0);
            UserContext.setUserId("charlie");

            assertFalse(userService.resetPassword("charlie", "wrongPass", "newPass"), "Password reset should fail with incorrect old password");
        } catch (SQLException e) {
            fail("SQLException during test: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateFavoriteAuthor_Success() throws Exception {
        try {
            // Step 1: Register a new admin user
            userService.register("user1", "pass", "user1@example.com", "1111111111", true, 0, 0);

            // Step 2: Set the current user context
            UserContext.setUserId("user1");
            
            // Step 3: Create a new author
            Author newAuthor = new Author("J.K. Rowling");
            Author addedAuthor = authorService.addAuthor(newAuthor);
            assertNotNull(addedAuthor, "Author should be added successfully");
            
            int authorId = addedAuthor.getAuthorID();
            assertTrue(authorId > 0, "Author ID should be greater than zero");

            
            // Step 4: Update favorite author for the user
            assertTrue(userService.updateFavoriteAuthor("user1", authorId), "Favorite author should be updated successfully");

            // Step 5: Verify that the favorite author was updated
            User updatedUser = userDAO.getById("user1");
            assertEquals(authorId, updatedUser.getFavoriteAuthorID(), "Favorite author ID should match the updated value");
        } catch (SQLException e) {
            fail("SQLException during test: " + e.getMessage());
        }
    }
    
    @Test
    public void testUpdateFavoriteAuthor_AccessDenied() throws Exception {
        try {
            // Step 1: Register two users
            userService.register("user1", "pass", "user1@example.com", "1111111111", true, 0, 0);
            userService.register("user2", "pass", "user2@example.com", "2222222222", false, 0, 0);

            // Step 2: Set the current user context
            UserContext.setUserId("user1");
            
            // Step 2: Create a new author
            Author newAuthor = new Author("George Orwell");
            Author addedAuthor = authorService.addAuthor(newAuthor);
            assertNotNull(addedAuthor, "Author should be added successfully");
            
            int authorId = addedAuthor.getAuthorID();
            assertTrue(authorId > 0, "Author ID should be greater than zero");

            // Step 3: Set the context to user2
            UserContext.setUserId("user2");

            // Step 4: Try to update favorite author for user1 while logged in as user2
            Exception exception = assertThrows(SecurityException.class, () -> {
                userService.updateFavoriteAuthor("user1", authorId);
            });

            assertEquals("Access denied: User mismatch", exception.getMessage());
        } catch (SQLException e) {
            fail("SQLException during test: " + e.getMessage());
        }
    }
    
    /**
     * Test updating favorite genre with correct user authorization
     * @throws Exception 
     */
    @Test
    public void testUpdateFavoriteGenre_Success() throws Exception {
        try {
            // Step 1: Register a new admin user
            userService.register("user1", "pass", "user1@example.com", "1111111111", true, 0, 0);
            userService.register("user2", "pass", "user2@example.com", "2222222222", false, 0, 0);
            
            // Step 2: Set the current user context
            UserContext.setUserId("user1");

            // Step 2: Create a new genre
            Genre newGenre = new Genre("Science Fiction");
            Genre addedGenre = genreService.addGenre(newGenre);
            assertNotNull(addedGenre, "Genre should be added successfully");

            int genreId = addedGenre.getGenreID();
            assertTrue(genreId > 0, "Genre ID should be greater than zero");

            // Step 3: Set the current user context
            UserContext.setUserId("user2");

            // Step 4: Update favorite genre for the user
            assertTrue(userService.updateFavoriteGenre("user2", genreId), "Favorite genre should be updated successfully");

            // Step 5: Verify that the favorite genre was updated
            User updatedUser = userDAO.getById("user2");
            assertEquals(genreId, updatedUser.getFavoriteGenreID(), "Favorite genre ID should match the updated value");
        } catch (SQLException e) {
            fail("SQLException during test: " + e.getMessage());
        }
    }
    
    /**
     * Test updating favorite genre with unauthorized access (different user)
     * @throws Exception 
     */
    @Test
    public void testUpdateFavoriteGenre_AccessDenied() throws Exception {
        try {
            // Step 1: Register two users
            userService.register("user1", "pass", "user1@example.com", "1111111111", true, 0, 0);
            userService.register("user2", "pass", "user2@example.com", "2222222222", false, 0, 0);
            
            // Step 3: Set the context to user2
            UserContext.setUserId("user1");
            
            // Step 2: Create a new genre
            Genre newGenre = new Genre("Fantasy");
            Genre addedGenre = genreService.addGenre(newGenre);
            assertNotNull(addedGenre, "Genre should be added successfully");

            int genreId = addedGenre.getGenreID();
            assertTrue(genreId > 0, "Genre ID should be greater than zero");

            // Step 3: Set the context to user2
            UserContext.setUserId("user2");

            // Step 4: Try to update favorite genre for user1 while logged in as user2
            Exception exception = assertThrows(SecurityException.class, () -> {
                userService.updateFavoriteGenre("user1", genreId);
            });

            assertEquals("Access denied: User mismatch", exception.getMessage());
        } catch (SQLException e) {
            fail("SQLException during test: " + e.getMessage());
        }
    }
    

    @Test
    public void testDeleteUser_AccessDenied() throws Exception {
        try {
            userService.register("normalUser", "pass", "normal@example.com", "7777777777", false, 0, 0);
            UserContext.setUserId("normalUser");

            SecurityException exception = assertThrows(SecurityException.class, () -> {
                userService.deleteUser("normalUser");
            });

            assertEquals("Access denied: Admin privileges required", exception.getMessage());
        } catch (SQLException e) {
            fail("SQLException during test: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteUser_Success() throws Exception {
        try {
            userService.register("adminUser", "adminPass", "admin@example.com", "8888888888", true, 0, 0);
            UserContext.setUserId("adminUser");

            assertTrue(userService.deleteUser("adminUser"), "Admin user should be able to delete their own account");
        } catch (SQLException e) {
            fail("SQLException during test: " + e.getMessage());
        }
    }
    
    @Test
    public void testRegister_CartCreatedForNormalUser() {
        try {
            // Step 1: Register a normal user
            User newUser = userService.register("normalUser", "password", "normal@example.com", "1234567890", false, 0, 0);

            // Step 2: Verify the user is registered successfully
            assertNotNull(newUser, "User should be registered successfully");
            assertEquals("normalUser", newUser.getUsername());

            // Step 3: Verify a cart is created for the user
            CartDAO cartDAO = new CartDAO();
            Cart cart = cartDAO.getCartByUsername("normalUser");
            assertNotNull(cart, "Cart should be created for normal user");
            assertEquals("normalUser", cart.getUsername(), "Cart username should match the registered user");
            assertEquals(0, cart.getCartItems().size(), "Cart should be empty initially");
        } catch (Exception e) {
            fail("Unexpected exception during test: " + e.getMessage());
        }
    }

    @Test
    public void testRegister_NoCartForAdminUser() {
        try {
            // Step 1: Register an admin user
            User adminUser = userService.register("adminUser", "password", "admin@example.com", "9876543210", true, 0, 0);

            // Step 2: Verify the admin user is registered successfully
            assertNotNull(adminUser, "Admin user should be registered successfully");
            assertEquals("adminUser", adminUser.getUsername());

            // Step 3: Verify no cart is created for the admin user
            CartDAO cartDAO = new CartDAO();
            Cart cart = cartDAO.getCartByUsername("adminUser");
            assertNull(cart, "Cart should not be created for admin user");
        } catch (Exception e) {
            fail("Unexpected exception during test: " + e.getMessage());
        }
    }
}