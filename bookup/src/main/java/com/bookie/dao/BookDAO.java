package com.bookie.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bookie.models.Author;
import com.bookie.models.Book;
import com.bookie.models.Genre;

public class BookDAO extends BaseDAO<Book, String> {

	@Override
	public Book getById(String ISBN) {
	    Book book = null;
	    try {
	        // Query to fetch book details along with author and genre information
	        String query = 
	            "SELECT b.ISBN, b.title, b.year, b.publisher, b.isFeatured, " +
	            "a.authorID, a.name AS authorName, " +
	            "g.genreID, g.name AS genreName " +
	            "FROM Books b " +
	            "LEFT JOIN Written w ON b.ISBN = w.ISBN " +
	            "LEFT JOIN Authors a ON w.authorID = a.authorID " +
	            "LEFT JOIN Belong bl ON b.ISBN = bl.ISBN " +
	            "LEFT JOIN Genres g ON bl.genreID = g.genreID " +
	            "WHERE b.ISBN = ?";

	        PreparedStatement stmt = connection.prepareStatement(query);
	        stmt.setString(1, ISBN);
	        ResultSet rs = stmt.executeQuery();

	        // Fetch book details along with related author and genre information
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

	            // Create the Book object with all the details
	            book = new Book(
	                rs.getString("ISBN"),
	                rs.getString("title"),
	                rs.getInt("year"),
	                rs.getString("publisher"),
	                rs.getBoolean("isFeatured"),
	                author,
	                genre
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
            // Start transaction
            connection.setAutoCommit(false);

            // Step 1: Insert the book into the Books table
            String bookQuery = "INSERT INTO Books (ISBN, title, year, publisher, isFeatured) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement bookStmt = connection.prepareStatement(bookQuery);
            bookStmt.setString(1, book.getISBN());
            bookStmt.setString(2, book.getTitle());
            bookStmt.setInt(3, book.getYear());
            bookStmt.setString(4, book.getPublisher());
            bookStmt.setBoolean(5, book.isFeatured());
            int rowsAffected = bookStmt.executeUpdate();

            if (rowsAffected == 0) {
                connection.rollback();
                return null;
            }

            // Step 2: Insert into Written table (author relationship)
            if (book.getAuthor() != null) {
                String writtenQuery = "INSERT INTO Written (authorID, ISBN) VALUES (?, ?)";
                PreparedStatement writtenStmt = connection.prepareStatement(writtenQuery);
                writtenStmt.setInt(1, book.getAuthor().getAuthorID());
                writtenStmt.setString(2, book.getISBN());
                writtenStmt.executeUpdate();
            }

            // Step 3: Insert into Belong table (genre relationship)
            if (book.getGenre() != null) {
                String belongQuery = "INSERT INTO Belong (ISBN, genreID) VALUES (?, ?)";
                PreparedStatement belongStmt = connection.prepareStatement(belongQuery);
                belongStmt.setString(1, book.getISBN());
                belongStmt.setInt(2, book.getGenre().getGenreID());
                belongStmt.executeUpdate();
            }

            // Step 4: Commit the transaction
            connection.commit();
            return book;

        } catch (SQLException e) {
            try {
                // Rollback the transaction in case of any errors
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                // Reset auto-commit to true after transaction is complete
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
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
    public boolean delete(String ISBN) {     //FIXME Do a soft delete
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
            // Updated query to fetch books along with their authors and genres
            String query = 
                "SELECT b.ISBN, b.title, b.year, b.publisher, b.isFeatured, " +
                "a.authorID, a.name AS authorName, " +
                "g.genreID, g.name AS genreName " +
                "FROM Books b " +
                "LEFT JOIN Written w ON b.ISBN = w.ISBN " +
                "LEFT JOIN Authors a ON w.authorID = a.authorID " +
                "LEFT JOIN Belong bl ON b.ISBN = bl.ISBN " +
                "LEFT JOIN Genres g ON bl.genreID = g.genreID";

            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // Iterate through the result set and create Book objects
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

                // Create the Book object with all the details
                Book book = new Book(
                    rs.getString("ISBN"),
                    rs.getString("title"),
                    rs.getInt("year"),
                    rs.getString("publisher"),
                    rs.getBoolean("isFeatured"),
                    author,
                    genre
                );

                // Add the book to the list
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }    
    
    public List<Book> searchByKeyword(String keyword) {
        List<Book> books = new ArrayList<>();
        try {
            // Updated query to search books and fetch related author and genre information
            String query = 
                "SELECT b.ISBN, b.title, b.year, b.publisher, b.isFeatured, " +
                "a.authorID, a.name AS authorName, " +
                "g.genreID, g.name AS genreName " +
                "FROM Books b " +
                "LEFT JOIN Written w ON b.ISBN = w.ISBN " +
                "LEFT JOIN Authors a ON w.authorID = a.authorID " +
                "LEFT JOIN Belong bl ON b.ISBN = bl.ISBN " +
                "LEFT JOIN Genres g ON bl.genreID = g.genreID " +
                "WHERE b.title LIKE ? OR b.publisher LIKE ?";

            PreparedStatement statement = connection.prepareStatement(query);

            // Prepare the keyword pattern for the LIKE clause
            String keywordPattern = "%" + keyword + "%";
            statement.setString(1, keywordPattern);
            statement.setString(2, keywordPattern);

            ResultSet rs = statement.executeQuery();

            // Iterate through the result set and create Book objects
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

                // Create the Book object with all the details
                Book book = new Book(
                    rs.getString("ISBN"),
                    rs.getString("title"),
                    rs.getInt("year"),
                    rs.getString("publisher"),
                    rs.getBoolean("isFeatured"),
                    author,
                    genre
                );

                // Add the book to the list
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
            // Query to fetch books written by the specified author along with genre information
            String query = 
                "SELECT b.ISBN, b.title, b.year, b.publisher, b.isFeatured, " +
                "a.authorID, a.name AS authorName, " +
                "g.genreID, g.name AS genreName " +
                "FROM Books b " +
                "INNER JOIN Written w ON b.ISBN = w.ISBN " +
                "INNER JOIN Authors a ON w.authorID = a.authorID " +
                "LEFT JOIN Belong bl ON b.ISBN = bl.ISBN " +
                "LEFT JOIN Genres g ON bl.genreID = g.genreID " +
                "WHERE a.authorID = ?";

            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, authorId);
            ResultSet rs = stmt.executeQuery();

            // Iterate through the result set and create Book objects
            while (rs.next()) {
                // Create the Author object
                Author author = new Author(rs.getInt("authorID"), rs.getString("authorName"));

                // Create the Genre object if available
                Genre genre = null;
                if (rs.getInt("genreID") != 0) {
                    genre = new Genre(rs.getInt("genreID"), rs.getString("genreName"));
                }

                // Create the Book object with all the details
                Book book = new Book(
                    rs.getString("ISBN"),
                    rs.getString("title"),
                    rs.getInt("year"),
                    rs.getString("publisher"),
                    rs.getBoolean("isFeatured"),
                    author,
                    genre
                );

                // Add the book to the list
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
    
    public List<Book> searchBookByAuthorKeyword(String keyword) {
        List<Book> books = new ArrayList<>();
        try {
            // Enhanced query to include genre information along with books and authors
            String query = 
                "SELECT a.authorID, a.name AS authorName, " +
                "b.ISBN, b.title, b.year, b.publisher, b.isFeatured, " +
                "g.genreID, g.name AS genreName " +
                "FROM Authors a " +
                "JOIN Written w ON a.authorID = w.authorID " +
                "JOIN Books b ON w.ISBN = b.ISBN " +
                "LEFT JOIN Belong bl ON b.ISBN = bl.ISBN " +
                "LEFT JOIN Genres g ON bl.genreID = g.genreID " +
                "WHERE a.name LIKE ? " +
                "ORDER BY a.authorID, b.title";

            PreparedStatement statement = connection.prepareStatement(query);

            // Prepare the keyword pattern for the LIKE clause
            String keywordPattern = "%" + keyword + "%";
            statement.setString(1, keywordPattern);
            ResultSet rs = statement.executeQuery();

            // Iterate through the result set and create Book objects
            while (rs.next()) {
                // Create Author object
                Author author = new Author(rs.getInt("authorID"), rs.getString("authorName"));

                // Create Genre object if available
                Genre genre = null;
                if (rs.getInt("genreID") != 0) {
                    genre = new Genre(rs.getInt("genreID"), rs.getString("genreName"));
                }

                // Create the Book object with all the details
                Book book = new Book(
                    rs.getString("ISBN"),
                    rs.getString("title"),
                    rs.getInt("year"),
                    rs.getString("publisher"),
                    rs.getBoolean("isFeatured"),
                    author,
                    genre
                );

                // Add the book to the list
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
    
//    public boolean addAuthorToBook(String ISBN, int authorID) {
//        String query = "INSERT INTO Written (authorID, ISBN) VALUES (?, ?)";
//        try {
//        	PreparedStatement statement = connection.prepareStatement(query);
//                
//            // Set the parameters
//            statement.setInt(1, authorID);
//            statement.setString(2, ISBN);
//            
//            // Execute the update
//            int rowsAffected = statement.executeUpdate();
//            
//            // If one row was inserted, return true
//            return rowsAffected > 0;
//            
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    
}
