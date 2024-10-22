package com.bookie.bizlogic;

import org.junit.Assert;
import org.junit.Test;
import com.bookie.models.Book;

import java.util.List;

public class BookServiceTest {

    @Test
    public void testBrowseBooks() {
        BookService bookService = new BookService();

        // Add some books for browsing
        bookService.addBook(new Book("9780451524935", "1984", 1949, "Secker & Warburg", true));
        bookService.addBook(new Book("9780747532743", "Harry Potter and the Philosopher's Stone", 1997, "Bloomsbury", true));

        // Browse books (get all books)
        List<Book> books = bookService.browseBooks();

        // Assert that books are available for browsing
        Assert.assertNotNull("Book list is null!", books);
        Assert.assertTrue("No books available for browsing!", books.size() > 0);
    }
}
