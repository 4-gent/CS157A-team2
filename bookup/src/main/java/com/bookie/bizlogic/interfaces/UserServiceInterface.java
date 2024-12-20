package com.bookie.bizlogic.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.bookie.models.User;

public interface UserServiceInterface {
    User register(String username, String password, String email, String phone, boolean isAdmin, int favoriteAuthorID, int favoriteGenreID) throws SQLException, Exception;
    boolean login(String username, String password);
    boolean resetPassword(String username, String oldPassword, String newPassword) throws SQLException;
    boolean updateFavoriteAuthor(String username, int favoriteAuthorID) throws SQLException;
    boolean updateFavoriteGenre(String username, int favoriteGenreID) throws SQLException;
    boolean deleteUser(String username) throws SQLException;
    User getUserByUsername(String username) throws SQLException;
    List<User> getAllNonAdminUsers();
}