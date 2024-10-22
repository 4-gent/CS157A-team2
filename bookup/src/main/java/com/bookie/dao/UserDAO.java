package com.bookie.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bookie.models.User;

public class UserDAO extends BaseDAO<User, String> {

	@Override
    public User getById(String id) {
        User user = null;
        try {
            String query = "SELECT * FROM Users WHERE username = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getBoolean("isAdmin"),
                    rs.getInt("favoriteAuthorID"),
                    rs.getInt("favoriteGenreID")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

	@Override
	public boolean add(User user) {
	    try {
	        String query = "INSERT INTO Users (username, password, email, phone, isAdmin, favoriteAuthorID, favoriteGenreID) VALUES (?, ?, ?, ?, ?, ?, ?)";
	        PreparedStatement stmt = connection.prepareStatement(query);
	        stmt.setString(1, user.getUsername());
	        stmt.setString(2, user.getPassword());
	        stmt.setString(3, user.getEmail());
	        stmt.setString(4, user.getPhone());
	        stmt.setBoolean(5, user.isAdmin());

	        // Set favoriteAuthorID to NULL if it's not set
	        if (user.getFavoriteAuthorID() == 0) {
	            stmt.setNull(6, java.sql.Types.INTEGER);  // Use setNull for optional fields
	        } else {
	            stmt.setInt(6, user.getFavoriteAuthorID());
	        }

	        // Set favoriteGenreID to NULL if it's not set
	        if (user.getFavoriteGenreID() == 0) {
	            stmt.setNull(7, java.sql.Types.INTEGER);  // Use setNull for optional fields
	        } else {
	            stmt.setInt(7, user.getFavoriteGenreID());
	        }

	        return stmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	@Override
    public boolean update(User user) {
        try {
            String query = "UPDATE Users SET password = ?, email = ?, phone = ?, isAdmin = ?, favoriteAuthorID = ?, favoriteGenreID = ? WHERE username = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, user.getPassword());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhone());
            stmt.setBoolean(4, user.isAdmin());
            stmt.setInt(5, user.getFavoriteAuthorID());
            stmt.setInt(6, user.getFavoriteGenreID());
            stmt.setString(7, user.getUsername());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

	@Override
    public boolean delete(String id) {
        try {
            String query = "DELETE FROM Users WHERE username = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
