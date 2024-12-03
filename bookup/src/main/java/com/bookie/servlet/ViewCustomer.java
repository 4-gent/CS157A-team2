package com.bookie.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bookie.bizlogic.AuthorService;
import com.bookie.bizlogic.GenreService;
import com.bookie.bizlogic.UserService;
import com.bookie.bizlogic.interfaces.UserServiceInterface;
import com.bookie.models.User;
import com.bookie.models.Author;
import com.bookie.models.Genre;
import com.bookie.bizlogic.interfaces.GenreServiceInterface;
import com.bookie.bizlogic.interfaces.AuthorServiceInterface;

@WebServlet("/ViewCustomer")
public class ViewCustomer extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ViewCustomer() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");

        UserServiceInterface userService = UserService.getServiceInstance();
        GenreServiceInterface genreService = GenreService.getServiceInstance();
        AuthorServiceInterface authorService = AuthorService.getServiceInstance();

        try {
            User userDetails = userService.getUserByUsername(username);

            if (userDetails != null) {
                // Invalidate old session (if any) and create a new session
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                session = request.getSession(true);

                // Fetch all genres and authors
                List<Genre> genres = genreService.getGenres();
                List<Author> authors = authorService.getAuthors();

                // Find the favorite genre and author by ID
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