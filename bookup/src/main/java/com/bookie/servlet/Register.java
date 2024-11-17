package com.bookie.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookie.bizlogic.UserService;

@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public Register() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		boolean admin = false;
		int favoriteAuthor = 0, favoriteGenre = 0;
		
		UserService user = new UserService();
		
		try {
			user.register(username, password, email, phone, admin, favoriteAuthor, favoriteGenre);
		} catch (SQLException e) {
			// FIXME SHOW to the User that new user creation failed
			e.printStackTrace();
		}
		
		response.sendRedirect("/bookup/pages/login.jsp");
	}
}
