package com.bookie.bizlogic;

import java.sql.SQLException;

import com.bookie.auth.IsAdmin;
import com.bookie.auth.SameUser;
import com.bookie.dao.UserDAO;
import com.bookie.models.User;

public class UserService implements UserServiceInterface {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    @Override
    public User register(String username, String password, String email, String phone, boolean isAdmin, int favoriteAuthorID, int favoriteGenreID) throws SQLException {
        User newUser = new User(username, password, email, phone, isAdmin, favoriteAuthorID, favoriteGenreID);
        return userDAO.add(newUser);
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
}