package com.bookie.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookie.auth.UserContext;
import com.bookie.bizlogic.AuthorService;
import com.bookie.bizlogic.GenreService;
import com.bookie.bizlogic.UserService;
import com.bookie.bizlogic.interfaces.AuthorServiceInterface;
import com.bookie.bizlogic.interfaces.GenreServiceInterface;
import com.bookie.bizlogic.interfaces.UserServiceInterface;
import com.bookie.models.Author;
import com.bookie.models.Genre;
import com.bookie.models.User;

@WebServlet("/Customers")
public class ViewCustomer extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ViewCustomer() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserServiceInterface userService = UserService.getServiceInstance();
        AuthorServiceInterface authorService = AuthorService.getServiceInstance();
        GenreServiceInterface genreService = GenreService.getServiceInstance();

        try {
            // Ensure UserContext is set
            String username = (String) request.getSession().getAttribute("username");
            if (username == null) {
                throw new Exception("User is not logged in");
            }
            UserContext.setUserId(username);

            // Retrieve all non-admin users
            List<User> nonAdminUsers = userService.getAllNonAdminUsers();
            List<Author> authors = authorService.getAuthors();
            List<Genre> genres = genreService.getGenres();

            // Create maps for quick lookup of authors and genres by ID
            Map<Integer, Author> authorMap = new HashMap<>();
            for (Author author : authors) {
                authorMap.put(author.getAuthorID(), author);
            }

            Map<Integer, Genre> genreMap = new HashMap<>();
            for (Genre genre : genres) {
                genreMap.put(genre.getGenreID(), genre);
            }

            // Set the non-admin users, authors, and genres as request attributes
            request.setAttribute("nonAdminUsers", nonAdminUsers);
            request.setAttribute("authorMap", authorMap);
            request.setAttribute("genreMap", genreMap);

            // Forward the request to the JSP page
            request.getRequestDispatcher("/pages/customers.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/pages/customers.jsp?error=Unknown Error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");

        UserServiceInterface userService = UserService.getServiceInstance();
        AuthorServiceInterface authorService = AuthorService.getServiceInstance();
        GenreServiceInterface genreService = GenreService.getServiceInstance();

        try {
            User userDetails = userService.getUserByUsername(username);

            if (userDetails != null) {
                // Retrieve all authors and genres
                List<Author> authors = authorService.getAuthors();
                List<Genre> genres = genreService.getGenres();

                // Find the favorite author and genre by ID
                Genre favoriteGenre = null;
                Author favoriteAuthor = null;

                for (Genre genre : genres) {
                    if (genre.getGenreID() == userDetails.getFavoriteGenreID()) {
                        favoriteGenre = genre;
                        break;
                    }
                }

                for (Author author : authors) {
                    if (author.getAuthorID() == userDetails.getFavoriteAuthorID()) {
                        favoriteAuthor = author;
                        break;
                    }
                }

                // Set user details and favorite genre/author as request attributes
                request.setAttribute("userDetails", userDetails);
                request.setAttribute("favoriteGenre", favoriteGenre);
                request.setAttribute("favoriteAuthor", favoriteAuthor);

                // Forward the request to customers.jsp
                request.getRequestDispatcher("/pages/customers.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/pages/customers.jsp?error=Incorrect Input");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/pages/customers.jsp?error=Database Error");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/pages/customers.jsp?error=Unknown Error");
        }
    }
}