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
import com.bookie.bizlogic.interfaces.AuthorServiceInterface;
import com.bookie.bizlogic.interfaces.GenreServiceInterface;
import com.bookie.bizlogic.interfaces.UserServiceInterface;
import com.bookie.models.Author;
import com.bookie.models.Genre;
import com.bookie.models.User;

@WebServlet("/Login")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Login() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserServiceInterface userService = UserService.getServiceInstance();
        AuthorServiceInterface authorService = AuthorService.getServiceInstance();
        GenreServiceInterface genreService = GenreService.getServiceInstance();

        try {
            boolean loginResult = userService.login(username, password);

            if (loginResult) {
                // Retrieve user details after successful login
                User userDetails = userService.getUserByUsername(username); // Assuming this method exists
                // Retrieve all authors and genres
                List<Author> authors = authorService.getAuthors();
                List<Genre> genres = genreService.getGenres();

                // Find the favorite author and genre by ID
                Author favoriteAuthor = null;
                Genre favoriteGenre = null;

                for (Author author : authors) {
                    if (author.getAuthorID() == userDetails.getFavoriteAuthorID()) {
                        favoriteAuthor = author;
                        break;
                    }
                }

                for (Genre genre : genres) {
                    if (genre.getGenreID() == userDetails.getFavoriteGenreID()) {
                        favoriteGenre = genre;
                        break;
                    }
                }

                // Invalidate old session (if any) and create a new session
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                session = request.getSession(true);

                // Store user details in the session
                session.setAttribute("username", userDetails.getUsername());
                session.setAttribute("email", userDetails.getEmail());
                session.setAttribute("isAdmin", userDetails.isAdmin()); // Boolean for profile.jsp
                session.setAttribute("role", userDetails.isAdmin() ? "admin" : "user"); // Optional, for other usage
                session.setAttribute("phone", userDetails.getPhone());
                session.setAttribute("favoriteAuthor", favoriteAuthor != null ? favoriteAuthor.getName() : "N/A");
                session.setAttribute("favoriteGenre", favoriteGenre != null ? favoriteGenre.getName() : "N/A");

                // Redirect to User_Info page
                response.sendRedirect(request.getContextPath() + "/pages/profile.jsp");
            } else {
                // Redirect to login page with an error message
                response.sendRedirect(request.getContextPath() + "/pages/login.jsp?error=Invalid credentials");
            }
        } catch (SQLException e) {
            // Log the error and redirect to an error page
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/pages/error.jsp?message=Database error occurred");
        } catch (Exception e) {
            // Handle any other unexpected errors
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/pages/error.jsp?message=Unexpected error occurred");
        }
    }
}