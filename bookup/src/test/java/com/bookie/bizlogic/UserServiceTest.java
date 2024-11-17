package com.bookie.bizlogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bookie.auth.UserContext;
import com.bookie.auth.UserServiceProxy;

public class UserServiceTest {

    private UserServiceInterface userService;

    @BeforeEach
    public void setUp() {
        UserService realService = new UserService();
        userService = UserServiceProxy.create(realService);
    }

    @Test
    public void testUpdateFavoriteAuthor_AccessDenied() {
        try {
            userService.register("user1", "pass", "user1@example.com", "1111111111", false, 0, 0);
            userService.register("user2", "pass", "user2@example.com", "2222222222", false, 0, 0);

            UserContext.setUserId("user2");
            
            Exception exception = assertThrows(SecurityException.class, () -> {
                userService.updateFavoriteAuthor("user1", 1);
            });
            assertEquals("Access denied: User mismatch", exception.getMessage());
        } catch (SQLException e) {
            fail("SQLException during test: " + e.getMessage());
        }
    }
}