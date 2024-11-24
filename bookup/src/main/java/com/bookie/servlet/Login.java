package com.bookie.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bookie.bizlogic.UserService;
import com.bookie.bizlogic.interfaces.UserServiceInterface;

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
        boolean login_result = userService.login(username, password);

        if (login_result) {
            // Store the username in the session
            HttpSession session = request.getSession();
            session.setAttribute("username", username);

            // Redirect to User_Info page
            response.sendRedirect(request.getContextPath() + "/pages/profile.jsp");
        } else {
            // Redirect to error page
            response.sendRedirect("/bookup/pages/error.jsp");
        }
    }
}
