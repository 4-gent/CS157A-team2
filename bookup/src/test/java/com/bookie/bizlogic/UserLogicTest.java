package com.bookie.bizlogic;

import org.junit.Assert;
import org.junit.Test;

public class UserLogicTest {

	@Test
    public void testRegisterUser() {
		UserService userLogic = new UserService();

        // Register a new user
        boolean result = userLogic.register("testUser", "password123", "testuser@example.com", "1234567890", false, 0, 0);

        // Assert that the registration was successful
        Assert.assertTrue("User registration failed!", result);
    }
	
    @Test
    public void testLoginUser() {
    	UserService userLogic = new UserService();

        // Register a user first
        userLogic.register("testUser", "password123", "testuser@example.com", "1234567890", false, 0, 0);

        // Attempt to login with correct credentials
        boolean loginSuccess = userLogic.login("testUser", "password123");
        Assert.assertTrue("Login failed with correct credentials!", loginSuccess);

        // Attempt to login with incorrect credentials
        boolean loginFail = userLogic.login("testUser", "wrongPassword");
        Assert.assertFalse("Login succeeded with incorrect credentials!", loginFail);
    }
}
