package com.bookie.bizlogic;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.bookie.auth.AuthorizationProxy;
import com.bookie.auth.IsAdmin;
import com.bookie.auth.IsAdminOrSameUser;
import com.bookie.auth.SameUser;
import com.bookie.bizlogic.interfaces.UserServiceInterface;
import com.bookie.dao.CartDAO;
import com.bookie.dao.UserDAO;
import com.bookie.models.Cart;
import com.bookie.models.User;

public class UserService implements UserServiceInterface {
    private UserDAO userDAO;

    private UserService() {
        this.userDAO = new UserDAO();
    }
    
    public static UserServiceInterface getServiceInstance() {
    	return AuthorizationProxy.createProxy(new UserService());
    }

    @Override
    public User register(String username, String password, String email, String phone, boolean isAdmin, int favoriteAuthorID, int favoriteGenreID) throws Exception {
        Connection connection = userDAO.getConnection(); // Fetch connection from BaseDAO

        try {
            // Step 1: Start transaction
            connection.setAutoCommit(false);

            // Step 2: Create a new user
            User newUser = new User(username, password, email, phone, isAdmin, favoriteAuthorID, favoriteGenreID);
            User registeredUser = userDAO.add(newUser);

            if (registeredUser == null) {
                throw new SQLException("Failed to create user.");
            }

            // Step 3: If the user is not an admin, create an empty cart
            if (!isAdmin) {
                CartDAO cartDAO = new CartDAO(); // Ensure CartDAO is initialized
                Cart newCart = new Cart(0, username, 0.0);
                Cart createdCart = cartDAO.add(newCart);

                if (createdCart == null) {
                    throw new SQLException("Failed to create cart for user.");
                }
            }

            // Step 4: Commit the transaction
            connection.commit();
            return registeredUser;

        } catch (Exception e) {
            // Step 5: Rollback in case of an error
            if (connection != null) {
                connection.rollback();
            }
            throw e; // Rethrow the exception to be handled by the caller
        } finally {
            // Step 6: Restore auto-commit
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }
    }
    
    @Override
    public boolean login(String username, String password) {
        User user = userDAO.getById(username);
        return user != null && user.getPassword().equals(password);
    }

    @Override
    public boolean resetPassword(String username, String oldPassword, String newPassword) throws SQLException {
        User user = userDAO.getById(username);
        if (user == null || !user.getPassword().equals(oldPassword)) return false;
        user.setPassword(newPassword);
        return userDAO.update(user);
    }

    @Override
    @SameUser("username")
    public boolean updateFavoriteAuthor(String username, int favoriteAuthorID) throws SQLException {
        User user = userDAO.getById(username);
        user.setFavoriteAuthorID(favoriteAuthorID);
        return userDAO.update(user);
    }

    @Override
    @SameUser("username")
    public boolean updateFavoriteGenre(String username, int favoriteGenreID) throws SQLException {
        User user = userDAO.getById(username);
        user.setFavoriteGenreID(favoriteGenreID);
        return userDAO.update(user);
    }

    @Override
    @IsAdmin
    public boolean deleteUser(String username) throws SQLException {
        return userDAO.delete(username);
    }   
    
    @Override
    public User getUserByUsername(String username) throws SQLException {
    	return userDAO.getUserByUsername(username);
    }

	@Override
	@IsAdmin
	public List<User> getAllNonAdminUsers() {
		return userDAO.getAllNonAdminUsers();
	}
    
    
}