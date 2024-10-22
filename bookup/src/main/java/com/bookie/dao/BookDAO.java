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
    public boolean add(Book book) {
        try {
            String query = "INSERT INTO Books (ISBN, title, year, publisher, isFeatured) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, book.getISBN());
            stmt.setString(2, book.getTitle());
            stmt.setInt(3, book.getYear());
            stmt.setString(4, book.getPublisher());
            stmt.setBoolean(5, book.isFeatured());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
}
