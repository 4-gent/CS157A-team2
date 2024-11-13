package com.bookie.bizlogic;

import org.junit.Assert;
import org.junit.Test;

import com.bookie.models.Author;
import com.bookie.models.Book;

import java.util.List;

public class BookServiceTest {
	
	private BookService bookService = new BookService();
	private AuthorService authorService = new AuthorService();
	

    @Test
    public void testBrowseBooks() {

        // Add some books for browsing
        bookService.addBook(new Book("978-0451524935", "1984", 1949, "Secker & Warburg", true));
        bookService.addBook(new Book("978-0747532743", "Harry Potter and the Philosopher's Stone", 1997, "Bloomsbury", true));

        // Browse books (get all books)
        List<Book> books = bookService.browseBooks();

        // Assert that books are available for browsing
        Assert.assertNotNull("Book list is null!", books);
        Assert.assertTrue("No books available for browsing!", books.size() > 0);
    }
    
    @Test
    public void testseachBookByAuthorKeyword() {

        // Add some books for browsing
        Book b1 = bookService.addBook(new Book("978-0451524935", "1984", 1949, "Secker & Warburg", true));
        Book b2 = bookService.addBook(new Book("978-0747532743", "Harry Potter and the Philosopher's Stone", 1997, "Bloomsbury", true));
        Book b3 = bookService.addBook(new Book("978-1338878943", "Harry Potter and the Prisoner of Azkaban", 2023, "Scholastic Inc.", true));
        
        Author a1 = new Author("George Orwell");
        a1 = authorService.addAuthor(a1);
        
        Author a2 = new Author("J. K. Rowling");
        a2 = authorService.addAuthor(a2);
        
        bookService.addAuthorToBook(b1.getISBN(), a1.getAuthorID());
        bookService.addAuthorToBook(b2.getISBN(), a2.getAuthorID());
        bookService.addAuthorToBook(b3.getISBN(), a2.getAuthorID());

        List<Book> b1 = bookService.seachBookByAuthorKeyword("Geor");
        Assert.assertTrue("Expected exactly 1 book", b1.size() == 1);
        
        List<Book> b2 = bookService.seachBookByAuthorKeyword("Row");
        Assert.assertTrue("Expected more than 1 book", b2.size() > 1);
    }
}
