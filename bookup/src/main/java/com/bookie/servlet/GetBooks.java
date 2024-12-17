package com.bookie.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookie.bizlogic.AuthorService;
import com.bookie.bizlogic.BookService;
import com.bookie.bizlogic.GenreService;
import com.bookie.bizlogic.interfaces.AuthorServiceInterface;
import com.bookie.bizlogic.interfaces.BookServiceInterface;
import com.bookie.bizlogic.interfaces.GenreServiceInterface;
import com.bookie.models.Author;
import com.bookie.models.Book;
import com.bookie.models.Genre;

@WebServlet("/Books")
public class GetBooks extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private BookServiceInterface bookService;
    private GenreServiceInterface genreService;
    private AuthorServiceInterface authorService;

    public GetBooks() {
        super();
        bookService = BookService.getServiceInstance();
        genreService = GenreService.getServiceInstance();
        authorService = AuthorService.getServiceInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
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
            String yearStr = request.getParameter("year");
            String author = request.getParameter("author");
            int year = (yearStr != null && !yearStr.isEmpty()) ? Integer.parseInt(yearStr) : 0;

            if (genre != null || year > 0 || author != null) {
                try {
                	List<Book> filterResult = bookService.filterBooks(genre, "", year, author);
                    System.out.println("Filter: " + filterResult);
                    request.setAttribute("filter_result", filterResult);
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while filtering books.");
                    return;
                }
            } else {
                request.setAttribute("filter_result", bookList);  // Set all books if no filters are applied
            }

            // Set data for the dropdowns (for genres, publishers, years, authors)
            List<Genre> genres = genreService.getGenres();
            List<String> years = new ArrayList<>();
            years.add("1952");
            years.add("1934");
            List<Author> authors = authorService.getAuthors();

            request.setAttribute("genres", genres);
            request.setAttribute("years", years);
            request.setAttribute("authors", authors);

            // Forward to JSP
            request.getRequestDispatcher("/pages/books.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing your request.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}