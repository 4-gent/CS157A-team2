package com.bookie.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bookie.models.Genre;

public class GenreDAO extends BaseDAO<Genre, Integer> {

	@Override
	public Genre getById(Integer genreID) {
		Genre genre = null; 
		try {
			String query = "SELECT * FROM Genres WHERE genreID = ?";
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, genreID);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
                genre = new Genre(
                    rs.getInt(genreID),
                    rs.getString("name")
                );
            }
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return genre;
	}

	@Override
	public Genre add(Genre genre) {
	    try {
	        // Prepare the INSERT statement with RETURN_GENERATED_KEYS to get the auto-generated key
	        String query = "INSERT INTO Genres (name) VALUES (?)";
	        PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	        
	        // Set the parameter for the query
	        stmt.setString(1, genre.getName());

	        // Execute the update
	        int rowsAffected = stmt.executeUpdate();

	        // If the insertion was successful, retrieve the generated genreID
	        if (rowsAffected > 0) {
	            ResultSet generatedKeys = stmt.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                int newGenreID = generatedKeys.getInt(1);
	                // Return a new Genre object with the generated genreID
	                return new Genre(newGenreID, genre.getName());
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null; // Return null if insertion fails
	}
	

	@Override
	public boolean update(Genre genre) {
		try {
            String query = "UPDATE Genres SET name = ? WHERE genreID = ?";    
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, genre.getName());
            stmt.setInt(2, genre.getGenreID());

            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
	

	@Override
	public boolean delete(Integer genreID) {
		try {
            String query = "DELETE FROM Genres WHERE genreID = ?";    
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, genreID);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	public List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        try {
            String query = "SELECT * FROM Genres";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Genre genre = new Genre(
                    rs.getInt("genreID"),
                    rs.getString("name")
                );
                genres.add(genre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genres;
    }

}
