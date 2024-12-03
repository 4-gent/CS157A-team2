package com.bookie.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bookie.bizlogic.UserService;
import com.bookie.bizlogic.interfaces.UserServiceInterface;
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

        try {
            boolean loginResult = userService.login(username, password);

            if (loginResult) {
                // Retrieve user details after successful login
                User userDetails = userService.getUserByUsername(username); // Assuming this method exists

                // Invalidate old session (if any) and create a new session
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                session = request.getSession(true);

                // Store user details in the session
                session.setAttribute("userId", userDetails.getId()); // Add userId to session

                session.setAttribute("username", userDetails.getUsername());
                session.setAttribute("email", userDetails.getEmail());
                session.setAttribute("isAdmin", userDetails.isAdmin()); // Boolean for profile.jsp
                session.setAttribute("role", userDetails.isAdmin() ? "admin" : "user"); // Optional, for other usage
                session.setAttribute("phone", userDetails.getPhone());

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