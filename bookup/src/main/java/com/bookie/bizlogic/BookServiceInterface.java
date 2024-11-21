package com.bookie.bizlogic;

import com.bookie.models.Book;

import java.util.List;

public interface BookServiceInterface {

    /**
     * Adds a new book to the system.
     * @param book - The book to be added.
     * @return The added book object if successful, otherwise null.
     */
    Book addBook(Book book);

    /**
     * Updates the details of an existing book.
     * @param book - The book with updated details.
     * @return true if the update is successful, false otherwise.
     */
    boolean updateBook(Book book);

    /**
     * Deletes a book from the system.
     * @param ISBN - The ISBN of the book to be deleted.
     * @return true if the deletion is successful, false otherwise.
     */
    boolean deleteBook(String ISBN);

    /**
     * Retrieves a book by its ISBN.
     * @param ISBN - The ISBN of the book.
     * @return The book if found, otherwise null.
     */
    Book getBookByISBN(String ISBN);

    /**
     * Retrieves all books in the system.
     * @return A list of all books.
     */
    List<Book> browseBooks();

    /**
     * Searches books by a keyword.
     * @param phrase - The search phrase.
     * @return A list of books matching the search criteria.
     */
    List<Book> bookSearch(String phrase);

    /**
     * Retrieves all books by an author.
     * @param authorId - The ID of the author.
     * @return A list of books by the specified author.
     */
    List<Book> getBooksByAuthor(int authorId);

    /**
     * Searches for books by an author's keyword.
     * @param keyword - The search keyword.
     * @return A list of books matching the author's keyword.
     */
    List<Book> searchBookByAuthorKeyword(String keyword);
    
    /**
     * Searches books by filter
     * @param genre
     * @param availability
     * @param publisher
     * @param year
     * @param author
     */
    List<Book> filterBooks(String genre, String availability, String publisher, int year, String author);
}