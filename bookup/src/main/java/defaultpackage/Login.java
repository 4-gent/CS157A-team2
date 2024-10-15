package defaultpackage;
import java.io.IOException; 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse; 
@WebServlet("/Login")
public class Login extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	public Login() {
		super();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		Connector con = new Connector();
		User user = con.authUser(username, password);
		if(user != null)
			response.sendRedirect("/bookup/pages/success.jsp");
		else
			response.sendRedirect("/bookup/pages/error.jsp");
	}
}
