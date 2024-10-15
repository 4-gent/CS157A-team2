package defaultpackage;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@WebServlet("/Books")
public class GetBooks extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	public GetBooks() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connector con = new Connector();
		List<Books> booksList = con.getAllBooks();
		
		request.setAttribute("booksList", booksList);
		
		request.getRequestDispatcher("/bookup/pages/books.jsp").forward(request, response);
	}
}
