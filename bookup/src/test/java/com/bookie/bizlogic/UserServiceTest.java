package com.bookie.bizlogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bookie.auth.UserContext;
import com.bookie.auth.AuthorizationProxy;

public class UserServiceTest {

    // Use the interface type here
    private UserServiceInterface userService;

    @BeforeEach
    public void setUp() {
        // Create a proxied instance of UserService
        userService = AuthorizationProxy.createProxy(new UserService());
    }

    @Test
    public void testUpdateFavoriteAuthor_AccessDenied() {
        try {
            // Register two users
            userService.register("user1", "pass", "user1@example.com", "1111111111", false, 0, 0);
            userService.register("user2", "pass", "user2@example.com", "2222222222", false, 0, 0);

            // Set context for a different user
            UserContext.setUserId("user2");

            // Attempt to update favorite author for another user
            Exception exception = assertThrows(SecurityException.class, () -> {
                userService.updateFavoriteAuthor("user1", 1);
            });

            // Verify that access was denied
            assertEquals("Access denied: User mismatch", exception.getMessage());
        } catch (SQLException e) {
            fail("SQLException during test: " + e.getMessage());
        } finally {
            UserContext.clear(); // Clear the context after the test
        }
    }

    @Test
    public void testUpdateFavoriteAuthor_Success() {
        try {
            // Register a user
            userService.register("user1", "pass", "user1@example.com", "1111111111", false, 0, 0);

            // Set context for the same user
            UserContext.setUserId("user1");

            // Update favorite author for the logged-in user
            boolean result = userService.updateFavoriteAuthor("user1", 2);
            assertEquals(true, result);
        } catch (SQLException e) {
            fail("SQLException during test: " + e.getMessage());
        } finally {
            UserContext.clear(); // Clear the context after the test
        }
    }
}