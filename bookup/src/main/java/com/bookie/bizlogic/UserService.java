package com.bookie.bizlogic;

import com.bookie.dao.UserDAO;
import com.bookie.models.User;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Registers a new user in the system.
     */
    public boolean register(String username, String password, String email, String phone, boolean isAdmin, int favoriteAuthorID, int favoriteGenreID) {
        User existingUser = userDAO.getById(username);
        if (existingUser != null) {
            System.out.println("User already exists!");
            return false;
        }

        User newUser = new User(username, password, email, phone, isAdmin, favoriteAuthorID, favoriteGenreID);
        return userDAO.add(newUser);
    }

    /**
     * Logs in a user by validating the username and password.
     */
    public boolean login(String username, String password) {
        User user = userDAO.getById(username);
        
        if (user == null) {
            System.out.println("User not found!");
            return false;
        }

        if (user.getPassword().equals(password)) {
            System.out.println("Login successful!");
            return true;
        } else {
            System.out.println("Invalid password!");
            return false;
        }
    }

    /**
     * Resets the user's password by validating the old password and updating it to a new password.
     * @param username - The username of the user
     * @param oldPassword - The old password of the user
     * @param newPassword - The new password to be updated
     * @return true if the password is successfully updated, false otherwise
     */
    public boolean resetPassword(String username, String oldPassword, String newPassword) {
        // Retrieve the user by username
        User user = userDAO.getById(username);
        
        if (user == null) {
            System.out.println("User not found!");
            return false;
        }

        // Check if the old password matches the stored password
        if (!user.getPassword().equals(oldPassword)) {
            System.out.println("Old password does not match!");
            return false;
        }

        // Update the password to the new password
        user.setPassword(newPassword);
        return userDAO.update(user);
    }

    /**
     * Deletes a user from the system.
     */
    public boolean deleteUser(String username) {
        return userDAO.delete(username);
    }
}
