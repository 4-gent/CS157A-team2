package com.bookie.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bookie.models.Author;

public class AuthorDAO extends BaseDAO<Author, Integer>{
	
	@Override
	public Author getById(Integer authorID) {
		Author author = null; 
		try {
			String query = "SELECT * FROM Books WHERE authorID = ?";
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, authorID);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
                author = new Author(
                    rs.getInt(authorID),
                    rs.getString("name")
                );
            }
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return author;
	}

	@Override
	public Author add(Author author) {
	    try {
	        // Prepare the SQL query with RETURN_GENERATED_KEYS to get the auto-generated key
	        String query = "INSERT INTO Authors (name) VALUES (?)";
	        PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	        stmt.setString(1, author.getName());
	        
	        // Execute the insert query
	        int rowsAffected = stmt.executeUpdate();
	        
	        if (rowsAffected > 0) {
	            // Retrieve the generated keys (authorID)
	            ResultSet generatedKeys = stmt.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                // Set the generated authorID on the Author object
	                int newAuthorID = generatedKeys.getInt(1);
	                author.setAuthorID(newAuthorID);
	                return author;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	

	@Override
	public boolean update(Author author) {
		try {
            String query = "UPDATE Authors SET name = ? WHERE authorID = ?";    
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, author.getName());
            stmt.setInt(2, author.getAuthorID());

            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
	

	@Override
	public boolean delete(Integer authorID) {
		try {
            String query = "DELETE FROM Authors WHERE authorID = ?";    
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, authorID);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	public List<Author> getAllAuthors() {
        List<Author> authors = new ArrayList<>();
        try {
            String query = "SELECT * FROM Authors";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Author author = new Author(
                    rs.getInt("authorID"),
                    rs.getString("name")
                );
                authors.add(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authors;
    }

}
