package com.bookie.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bookie.models.Author;
import com.bookie.models.Book;
import com.bookie.models.Genre;
import com.bookie.models.InventoryItem;

public class InventoryDAO extends BaseDAO<InventoryItem, Integer>{

	@Override
	public InventoryItem add(InventoryItem inventoryItem) {
	    try {
	        // Insert the InventoryItem into the InventoryItems table
	        String query = "INSERT INTO InventoryItems (ISBN, price, qty, description) VALUES (?, ?, ?, ?)";
	        PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

	        // Set parameters
	        stmt.setString(1, inventoryItem.getBook().getISBN());
	        stmt.setDouble(2, inventoryItem.getPrice());
	        stmt.setInt(3, inventoryItem.getQty());
	        stmt.setString(4, inventoryItem.getDescription());

	        // Execute the insert query
	        int rowsAffected = stmt.executeUpdate();
	        if (rowsAffected == 0) {
	            return null;
	        }

	        // Retrieve the generated inventoryItemID
	        ResultSet generatedKeys = stmt.getGeneratedKeys();
	        if (generatedKeys.next()) {
	            int newInventoryItemID = generatedKeys.getInt(1);
	            return new InventoryItem(newInventoryItemID, inventoryItem.getBook(), 
	                                     inventoryItem.getPrice(), inventoryItem.getQty(), 
	                                     inventoryItem.getDescription());
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	@Override
	public InventoryItem getById(Integer id) {
	    InventoryItem inventoryItem = null;
	    try {
	        // Enhanced query to fetch InventoryItem along with Book, Author, and Genre details
	        String query = 
	            "SELECT i.inventoryItemID, i.price, i.qty, i.description, " +
	            "b.ISBN, b.title, b.year, b.publisher, b.isFeatured, " +
	            "a.authorID, a.name AS authorName, " +
	            "g.genreID, g.name AS genreName " +
	            "FROM InventoryItems i " +
	            "JOIN Books b ON i.ISBN = b.ISBN " +
	            "LEFT JOIN Written w ON b.ISBN = w.ISBN " +
	            "LEFT JOIN Authors a ON w.authorID = a.authorID " +
	            "LEFT JOIN Belong bl ON b.ISBN = bl.ISBN " +
	            "LEFT JOIN Genres g ON bl.genreID = g.genreID " +
	            "WHERE i.inventoryItemID = ?";

	        PreparedStatement stmt = connection.prepareStatement(query);
	        stmt.setInt(1, id);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            // Create Author object if available
	            Author author = null;
	            if (rs.getInt("authorID") != 0) {
	                author = new Author(rs.getInt("authorID"), rs.getString("authorName"));
	            }

	            // Create Genre object if available
	            Genre genre = null;
	            if (rs.getInt("genreID") != 0) {
	                genre = new Genre(rs.getInt("genreID"), rs.getString("genreName"));
	            }

	            // Create Book object with associated Author and Genre
	            Book book = new Book(
	                rs.getString("ISBN"),
	                rs.getString("title"),
	                rs.getInt("year"),
	                rs.getString("publisher"),
	                rs.getBoolean("isFeatured"),
	                author,
	                genre
	            );

	            // Create InventoryItem object
	            inventoryItem = new InventoryItem(
	                rs.getInt("inventoryItemID"),
	                book,
	                rs.getDouble("price"),
	                rs.getInt("qty"),
	                rs.getString("description")
	            );
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return inventoryItem;
	}


	@Override
	public boolean update(InventoryItem t) {
	    try {
	        // Query to update the InventoryItem details
	        String query = "UPDATE InventoryItems SET price = ?, qty = ?, description = ? WHERE inventoryItemID = ?";
	        PreparedStatement stmt = connection.prepareStatement(query);
	        
	        // Set the parameters
	        stmt.setDouble(1, t.getPrice());
	        stmt.setInt(2, t.getQty());
	        stmt.setString(3, t.getDescription());
	        stmt.setInt(4, t.getInventoryItemID());

	        // Execute the update
	        int rowsAffected = stmt.executeUpdate();
	        return rowsAffected > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	@Override
	public boolean delete(Integer id) {     //FIXME Do a soft delete
	    try {
	        // Query to delete the InventoryItem by its ID
	        String query = "DELETE FROM InventoryItems WHERE inventoryItemID = ?";
	        PreparedStatement stmt = connection.prepareStatement(query);
	        stmt.setInt(1, id);

	        // Execute the delete
	        int rowsAffected = stmt.executeUpdate();
	        return rowsAffected > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	public InventoryItem searchByISBN(String ISBN) {
	    InventoryItem inventoryItem = null;
	    try {
	        // Query to fetch InventoryItem along with Book, Author, and Genre details
	        String query = 
	            "SELECT i.inventoryItemID, i.price, i.qty, i.description, " +
	            "b.ISBN, b.title, b.year, b.publisher, b.isFeatured, " +
	            "a.authorID, a.name AS authorName, " +
	            "g.genreID, g.name AS genreName " +
	            "FROM InventoryItems i " +
	            "JOIN Books b ON i.ISBN = b.ISBN " +
	            "LEFT JOIN Written w ON b.ISBN = w.ISBN " +
	            "LEFT JOIN Authors a ON w.authorID = a.authorID " +
	            "LEFT JOIN Belong bl ON b.ISBN = bl.ISBN " +
	            "LEFT JOIN Genres g ON bl.genreID = g.genreID " +
	            "WHERE b.ISBN = ?";

	        PreparedStatement stmt = connection.prepareStatement(query);
	        stmt.setString(1, ISBN);
	        ResultSet rs = stmt.executeQuery();

	        // If a record is found, populate the InventoryItem object
	        if (rs.next()) {
	            // Create Author object if available
	            Author author = null;
	            if (rs.getInt("authorID") != 0) {
	                author = new Author(rs.getInt("authorID"), rs.getString("authorName"));
	            }

	            // Create Genre object if available
	            Genre genre = null;
	            if (rs.getInt("genreID") != 0) {
	                genre = new Genre(rs.getInt("genreID"), rs.getString("genreName"));
	            }

	            // Create Book object with associated Author and Genre
	            Book book = new Book(
	                rs.getString("ISBN"),
	                rs.getString("title"),
	                rs.getInt("year"),
	                rs.getString("publisher"),
	                rs.getBoolean("isFeatured"),
	                author,
	                genre
	            );

	            // Create InventoryItem object
	            inventoryItem = new InventoryItem(
	                rs.getInt("inventoryItemID"),
	                book,
	                rs.getDouble("price"),
	                rs.getInt("qty"),
	                rs.getString("description")
	            );
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return inventoryItem;
	}
	
	public List<InventoryItem> searchInventoryItemsByKeyword(String keyword) {
	    List<InventoryItem> inventoryItems = new ArrayList<>();
	    try {
	        // Enhanced query to search in book title, author name, and publisher
	        String query = 
	            "SELECT i.inventoryItemID, i.price, i.qty, i.description, " +
	            "b.ISBN, b.title, b.year, b.publisher, b.isFeatured, " +
	            "a.authorID, a.name AS authorName, " +
	            "g.genreID, g.name AS genreName " +
	            "FROM InventoryItems i " +
	            "JOIN Books b ON i.ISBN = b.ISBN " +
	            "LEFT JOIN Written w ON b.ISBN = w.ISBN " +
	            "LEFT JOIN Authors a ON w.authorID = a.authorID " +
	            "LEFT JOIN Belong bl ON b.ISBN = bl.ISBN " +
	            "LEFT JOIN Genres g ON bl.genreID = g.genreID " +
	            "WHERE b.title LIKE ? OR a.name LIKE ? OR b.publisher LIKE ? " +
	            "ORDER BY b.title";

	        PreparedStatement stmt = connection.prepareStatement(query);

	        // Prepare the keyword pattern for the LIKE clause
	        String keywordPattern = "%" + keyword + "%";
	        stmt.setString(1, keywordPattern);
	        stmt.setString(2, keywordPattern);
	        stmt.setString(3, keywordPattern);

	        ResultSet rs = stmt.executeQuery();

	        // Iterate through the result set and create InventoryItem objects
	        while (rs.next()) {
	            // Create Author object if available
	            Author author = null;
	            if (rs.getInt("authorID") != 0) {
	                author = new Author(rs.getInt("authorID"), rs.getString("authorName"));
	            }

	            // Create Genre object if available
	            Genre genre = null;
	            if (rs.getInt("genreID") != 0) {
	                genre = new Genre(rs.getInt("genreID"), rs.getString("genreName"));
	            }

	            // Create Book object with associated Author and Genre
	            Book book = new Book(
	                rs.getString("ISBN"),
	                rs.getString("title"),
	                rs.getInt("year"),
	                rs.getString("publisher"),
	                rs.getBoolean("isFeatured"),
	                author,
	                genre
	            );

	            // Create InventoryItem object
	            InventoryItem inventoryItem = new InventoryItem(
	                rs.getInt("inventoryItemID"),
	                book,
	                rs.getDouble("price"),
	                rs.getInt("qty"),
	                rs.getString("description")
	            );

	            // Add the InventoryItem to the list
	            inventoryItems.add(inventoryItem);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return inventoryItems;
	}
	
	
	public List<InventoryItem> getAllInventoryItems() {
	    List<InventoryItem> inventoryItems = new ArrayList<>();
	    try {
	        // Query to fetch all InventoryItems along with Book, Author, and Genre details
	        String query = 
	            "SELECT i.inventoryItemID, i.price, i.qty, i.description, " +
	            "b.ISBN, b.title, b.year, b.publisher, b.isFeatured, " +
	            "a.authorID, a.name AS authorName, " +
	            "g.genreID, g.name AS genreName " +
	            "FROM InventoryItems i " +
	            "JOIN Books b ON i.ISBN = b.ISBN " +
	            "LEFT JOIN Written w ON b.ISBN = w.ISBN " +
	            "LEFT JOIN Authors a ON w.authorID = a.authorID " +
	            "LEFT JOIN Belong bl ON b.ISBN = bl.ISBN " +
	            "LEFT JOIN Genres g ON bl.genreID = g.genreID";

	        PreparedStatement stmt = connection.prepareStatement(query);
	        ResultSet rs = stmt.executeQuery();

	        // Iterate through the result set and create InventoryItem objects
	        while (rs.next()) {
	            // Create Author object if available
	            Author author = null;
	            if (rs.getInt("authorID") != 0) {
	                author = new Author(rs.getInt("authorID"), rs.getString("authorName"));
	            }

	            // Create Genre object if available
	            Genre genre = null;
	            if (rs.getInt("genreID") != 0) {
	                genre = new Genre(rs.getInt("genreID"), rs.getString("genreName"));
	            }

	            // Create Book object with associated Author and Genre
	            Book book = new Book(
	                rs.getString("ISBN"),
	                rs.getString("title"),
	                rs.getInt("year"),
	                rs.getString("publisher"),
	                rs.getBoolean("isFeatured"),
	                author,
	                genre
	            );

	            // Create InventoryItem object
	            InventoryItem inventoryItem = new InventoryItem(
	                rs.getInt("inventoryItemID"),
	                book,
	                rs.getDouble("price"),
	                rs.getInt("qty"),
	                rs.getString("description")
	            );

	            // Add the InventoryItem to the list
	            inventoryItems.add(inventoryItem);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return inventoryItems;
	}
}
