package com.bookie.bizlogic;
import java.util.List;

import com.bookie.auth.AuthorizationProxy;
import com.bookie.auth.IsAdmin;
import com.bookie.bizlogic.interfaces.BookServiceInterface;
import com.bookie.dao.BookDAO;
import com.bookie.models.Book;

public class BookService implements BookServiceInterface {
    private BookDAO bookDAO;

    private BookService() {
        this.bookDAO = new BookDAO();
    }
    
    public static BookServiceInterface getServiceInstance() {
    	return AuthorizationProxy.createProxy(new BookService());
    }

    @Override
    @IsAdmin
    public Book addBook(Book book) {
        return bookDAO.add(book);
    }

    @Override
    @IsAdmin
    public boolean updateBook(Book book) {
        return bookDAO.update(book);
    }

    @Override
    @IsAdmin
    public boolean deleteBook(String ISBN) {
        return bookDAO.delete(ISBN);
    }

    @Override
    public Book getBookByISBN(String ISBN) {
        return bookDAO.getById(ISBN);
    }

    @Override
    public List<Book> browseBooks() {
        return bookDAO.getAllBooks();
    }

    @Override
    public List<Book> bookSearch(String phrase) {
        return bookDAO.searchByKeyword(phrase);
    }

    @Override
    public List<Book> getBooksByAuthor(int authorId) {
        return bookDAO.getBooksByAuthor(authorId);
    }

    @Override
    public List<Book> searchBookByAuthorKeyword(String keyword) {
        return bookDAO.searchBookByAuthorKeyword(keyword);
    }
    
    @Override
    public List<Book> filterBooks(String genre, String availability, String publisher, int year, String author){
    	return bookDAO.filterBooks(genre, availability, publisher, year, author);
    }
    
}