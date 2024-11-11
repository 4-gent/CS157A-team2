package com.bookie.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bookie.models.Book;

public class BookDAO extends BaseDAO<Book, String> {

    @Override
    public Book getById(String ISBN) {
        Book book = null;
        try {
            String query = "SELECT * FROM Books WHERE ISBN = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, ISBN);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                book = new Book(
                    rs.getString("ISBN"),
                    rs.getString("title"),
                    rs.getInt("year"),
                    rs.getString("publisher"),
                    rs.getBoolean("isFeatured")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    @Override
    public Book add(Book book) {
        try {
            // Prepare the INSERT statement
            String query = "INSERT INTO Books (ISBN, title, year, publisher, isFeatured) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            
            // Set the parameters
            stmt.setString(1, book.getISBN());
            stmt.setString(2, book.getTitle());
            stmt.setInt(3, book.getYear());
            stmt.setString(4, book.getPublisher());
            stmt.setBoolean(5, book.isFeatured());

            // Execute the update
            int rowsAffected = stmt.executeUpdate();
            
            // If the insertion is successful, return the book object
            if (rowsAffected > 0) {
                return book;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(Book book) {
        try {
            String query = "UPDATE Books SET title = ?, year = ?, publisher = ?, isFeatured = ? WHERE ISBN = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, book.getTitle());
            stmt.setInt(2, book.getYear());
            stmt.setString(3, book.getPublisher());
            stmt.setBoolean(4, book.isFeatured());
            stmt.setString(5, book.getISBN());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String ISBN) {
        try {
            String query = "DELETE FROM Books WHERE ISBN = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, ISBN);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try {
            String query = "SELECT * FROM Books";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Book book = new Book(
                    rs.getString("ISBN"),
                    rs.getString("title"),
                    rs.getInt("year"),
                    rs.getString("publisher"),
                    rs.getBoolean("isFeatured")
                );
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
    
    public List<Book> searchByKeyword(String keyword){
    	List<Book> books = new ArrayList<>();
    	
    	try {
    		String query = "SELECT * From Books WHERE title LIKE ? or publisher LIKE ?";
    		PreparedStatement statement = connection.prepareStatement(query);
    		
    		String keywordPattern = "%" + keyword + "%";
    		
    		statement.setString(1, keywordPattern);
    		statement.setString(2, keywordPattern);
    		ResultSet rs = statement.executeQuery();
    		
    		while(rs.next()) {
    			Book book = new Book(
    				rs.getString("ISBN"),
    				rs.getString("title"),
    				rs.getInt("year"),
    				rs.getString("publisher"),
    				rs.getBoolean("isFeatured")
    			);
    			books.add(book);
    		}
    	
    	} catch (SQLException e) {
    		e.printStackTrace();   	
    	}
    	return books;
    }
    
    /**
     * Fetches all books by the given authorId.
     *
     * @param authorId The ID of the author whose books you want to retrieve.
     * @return A list of books written by the specified author.
     */
    public List<Book> getBooksByAuthor(int authorId) {
        List<Book> books = new ArrayList<>();
        try {
            String query = "SELECT B.*, A.name AS authorName " +
                           "FROM Books B " +
                           "INNER JOIN Written W ON B.ISBN = W.ISBN " +
                           "INNER JOIN Authors A ON W.authorID = A.authorID " +
                           "WHERE A.authorID = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, authorId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Create a new Book object with the data from the result set
                Book book = new Book(
                    rs.getString("ISBN"),
                    rs.getString("title"),
                    rs.getInt("year"),
                    rs.getString("publisher"),
                    rs.getBoolean("isFeatured")
                );
                books.add(book);
                System.out.println("Author: " + rs.getString("authorName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
    
    public List<Book> seachBookByAuthorKeyword(String keyword) {
		List<Book> books = new ArrayList<>();
	    	
    	try {
    		String query = "SELECT \n"
    				+ "    a.authorID, \n"
    				+ "    a.name AS authorName, \n"
    				+ "    b.ISBN, \n"
    				+ "    b.title AS bookTitle, \n"
    				+ "    b.publisher\n"
    				+ "FROM \n"
    				+ "    Authors a\n"
    				+ "JOIN \n"
    				+ "    Written w ON a.authorID = w.authorID\n"
    				+ "JOIN \n"
    				+ "    Books b ON w.ISBN = b.ISBN\n"
    				+ "WHERE \n"
    				+ "    a.name LIKE CONCAT('%', ?, '%')\n"
    				+ "ORDER BY \n"
    				+ "    a.authorID, b.title;";
    		PreparedStatement statement = connection.prepareStatement(query);
    		
    		String keywordPattern = "%" + keyword + "%";
    		
    		statement.setString(1, keywordPattern);
    		ResultSet rs = statement.executeQuery();
    		
    		while(rs.next()) {
    			Book book = new Book(
    				rs.getString("ISBN"),
    				rs.getString("title"),
    				rs.getInt("year"),
    				rs.getString("publisher"),
    				rs.getBoolean("isFeatured"),
    				rs.getString("authorName")
    			);
    			books.add(book);
    		}
    	
    	} catch (SQLException e) {
    		e.printStackTrace();   	
    	}
    	return books;   	
    	
    }
    
    public boolean addAuthorToBook(String ISBN, int authorID) {
        String query = "INSERT INTO Written (authorID, ISBN) VALUES (?, ?)";
        try {
        	PreparedStatement statement = connection.prepareStatement(query);
                
            // Set the parameters
            statement.setInt(1, authorID);
            statement.setString(2, ISBN);
            
            // Execute the update
            int rowsAffected = statement.executeUpdate();
            
            // If one row was inserted, return true
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    
}
