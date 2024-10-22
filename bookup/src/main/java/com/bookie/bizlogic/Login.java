package com.bookie.bizlogic;
import java.io.IOException; 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Login")
public class Login extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	public Login() {
		super();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		

		UserService user = new UserService();
		boolean login_result = user.login(username, password);
		if(login_result == true)
			response.sendRedirect("/bookup/Books");
		else
			response.sendRedirect("/bookup/pages/error.jsp");
	}
}
