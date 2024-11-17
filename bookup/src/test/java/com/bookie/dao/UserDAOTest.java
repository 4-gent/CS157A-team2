package com.bookie.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.bookie.models.User;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDAOTest {

    private UserDAO userDAO;

    @BeforeAll
    public void setUp() {
        // Initialize UserDAO (which sets up the database connection via BaseDAO)
        userDAO = new UserDAO();
    }

    @BeforeEach
    public void cleanUpDatabase() throws Exception {
        // Clean up the database before each test
        userDAO.connection.createStatement().execute("DELETE FROM Users");
    }

    @Test
    public void testAddUser_Success() throws Exception {
        User newUser = new User("johnDoe", "password123", "john@example.com", "1234567890", false, 0, 0);
        User addedUser = userDAO.add(newUser);

        assertNotNull(addedUser, "User should be added successfully");
        assertEquals("johnDoe", addedUser.getUsername());
        assertEquals("john@example.com", addedUser.getEmail());

        // Fetch the user from the database to verify
        User fetchedUser = userDAO.getById("johnDoe");
        assertNotNull(fetchedUser);
        assertEquals("johnDoe", fetchedUser.getUsername());
    }

    @Test
    public void testAddUser_DuplicateUser_ThrowsSQLException() {
        User newUser = new User("duplicateUser", "password123", "duplicate@example.com", "1234567890", false, 0, 0);
        
        // Add the user for the first time - this should succeed
        assertDoesNotThrow(() -> userDAO.add(newUser));

        // Try adding the same user again, expecting an SQLException
        SQLException exception = assertThrows(SQLException.class, () -> {
            userDAO.add(newUser);
        });

        // Verify that the exception message contains information related to the issue
        assertNotNull(exception.getMessage());
        System.out.println("SQLException caught as expected: " + exception.getMessage());
    }

    @Test
    public void testGetById_UserExists() throws Exception {
        User newUser = new User("alice", "securePass", "alice@example.com", "5555555555", false, 0, 0);
        userDAO.add(newUser);

        User fetchedUser = userDAO.getById("alice");

        assertNotNull(fetchedUser);
        assertEquals("alice", fetchedUser.getUsername());
        assertEquals("alice@example.com", fetchedUser.getEmail());
    }

    @Test
    public void testGetById_UserDoesNotExist() throws Exception {
        User fetchedUser = userDAO.getById("nonExistentUser");
        assertNull(fetchedUser, "User should not exist in the database");
    }

    @Test
    public void testUpdateUser_Success() throws Exception {
        User newUser = new User("bob", "initialPass", "bob@example.com", "2223334444", false, 0, 0);
        userDAO.add(newUser);

        // Update user details
        newUser.setEmail("bob.new@example.com");
        boolean isUpdated = userDAO.update(newUser);
        assertTrue(isUpdated);

        // Fetch updated user
        User updatedUser = userDAO.getById("bob");
        assertEquals("bob.new@example.com", updatedUser.getEmail());
    }

    @Test
    public void testDeleteUser_Success() throws Exception {
        User newUser = new User("charlie", "charliePass", "charlie@example.com", "7778889990", false, 0, 0);
        userDAO.add(newUser);

        // Delete user
        boolean isDeleted = userDAO.delete("charlie");
        assertTrue(isDeleted);

        // Verify user is deleted
        User deletedUser = userDAO.getById("charlie");
        assertNull(deletedUser, "User should be deleted");
    }
}