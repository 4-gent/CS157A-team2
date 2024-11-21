package com.bookie.servlet;

import com.bookie.bizlogic.BookService;
import com.bookie.bizlogic.BookServiceInterface;
import com.bookie.models.Book;
import java.io.IOException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Books")
public class GetBooks extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Use the interface to refer to the BookService
    private BookServiceInterface bookService;

    public GetBooks() {
        super();
        // Instantiate the BookService class
        bookService = new BookService(); // Ensure BookService implements BookServiceInterface
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Always fetch the list of all books (default view)
        List<Book> bookList = bookService.browseBooks();
        request.setAttribute("bookList", bookList);  // Always set this

        // Handle search functionality
        String searchInput = request.getParameter("search-input");
        if (searchInput != null && !searchInput.isEmpty()) {
            List<Book> searchResult = bookService.bookSearch(searchInput);
            request.setAttribute("search_result", searchResult);
        }

        // Handle filter functionality
        String genre = request.getParameter("genre");
        String availability = request.getParameter("availability");
        String publisher = request.getParameter("publisher");
        String yearStr = request.getParameter("year");
        String author = request.getParameter("author");
        int year = (yearStr != null && !yearStr.isEmpty()) ? Integer.parseInt(yearStr) : 0;

        if (genre != null || availability != null || publisher != null || year > 0 || author != null) {
            List<Book> filterResult = bookService.filterBooks(genre, availability, publisher, year, author);
            request.setAttribute("filter_result", filterResult);
        } else {
            request.setAttribute("filter_result", bookList);  // Set all books if no filters are applied
        }

        // Set data for the dropdowns (for genres, publishers, years, authors)
        List<String> genres = new ArrayList<>();
        genres.add("Fiction");
        genres.add("Fantasy");

        List<String> publishers = new ArrayList<>();
        publishers.add("Bloomsbury");
        publishers.add("Putnam");

        List<String> years = new ArrayList<>();
        years.add("1952");
        years.add("1934");

        List<String> authors = new ArrayList<>();
        authors.add("George Orwell");
        authors.add("Harper Lee");

        request.setAttribute("genres", genres);
        request.setAttribute("publishers", publishers);
        request.setAttribute("years", years);
        request.setAttribute("authors", authors);

        // Forward to the JSP page to display the results
        request.getRequestDispatcher("/pages/books.jsp").forward(request, response);
    }



    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // POST request logic is handled similarly to GET requests
        doGet(request, response);
    }
}

