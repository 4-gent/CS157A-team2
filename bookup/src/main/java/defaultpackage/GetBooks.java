package defaultpackage;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@WebServlet("/Books")
public class GetBooks extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	public GetBooks() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String book_name = request.getParameter("books");
		
		Connector con = new Connector();
		Books book = con.getBookByName(book_name);
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		if(book != null) {
            out.println("<html><body>");
            out.println("<h1>Book Details</h1>");
            out.println("<p>Name: " + book.getName() + "</p>");
            out.println("<p>Author: " + book.getAuthor() + "</p>");
            out.println("<p>ISBN: " + book.getISBN() + "</p>");
            out.println("<p>Genre: " + book.getGenre() + "</p>");
            out.println("<p>Price: " + book.getPrice() + "</p>");
            out.println("</body></html>");
		} else 
			out.println("<html><body><h1>No Book Found</h1></body></html>");
		
	}
}
