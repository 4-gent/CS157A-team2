package com.bookie.bizlogic;

import java.util.List;

import com.bookie.dao.BookDAO;
import com.bookie.models.Book;

public class BookService {
    private BookDAO bookDAO;

    public BookService() {
        this.bookDAO = new BookDAO();
    }

    /**
     * Adds a new book to the system.
     * @param book - The book to be added.
     * @return true if the book is added successfully, false otherwise.
     */
    public boolean addBook(Book book) {
        return bookDAO.add(book);
    }

    /**
     * Updates the details of an existing book.
     * @param book - The book with updated details.
     * @return true if the update is successful, false otherwise.
     */
    public boolean updateBook(Book book) {
        return bookDAO.update(book);
    }

    /**
     * Deletes a book from the system.
     * @param ISBN - The ISBN of the book to be deleted.
     * @return true if the deletion is successful, false otherwise.
     */
    public boolean deleteBook(String ISBN) {
        return bookDAO.delete(ISBN);
    }

    /**
     * Retrieves a book by its ISBN.
     * @param ISBN - The ISBN of the book.
     * @return The book if found, otherwise null.
     */
    public Book getBookByISBN(String ISBN) {
        return bookDAO.getById(ISBN);
    }

    /**
     * Renamed from getAllBooks to browseBooks
     * Retrieves all books in the system.
     * @return A list of all books.
     */
    public List<Book> browseBooks() {
        return bookDAO.getAllBooks();
    }
    
    /**
     * Get a book by a keyword
     * @param title - Title of book
     * @param publisher - Publisher of book
     * @return If the book is found it'll be returned, otherwise null
     */
    public List<Book> bookSearch(String phrase) {
    	return bookDAO.searchByKeyword(phrase);
    }
    
    /**
     * Retrieves all books by an author the system.
     * @return A list of all books.
     * @param authorId
     */
	public List<Book> getBooksByAuthor(int authorId) {
		return bookDAO.getBooksByAuthor(authorId);
	}
}
