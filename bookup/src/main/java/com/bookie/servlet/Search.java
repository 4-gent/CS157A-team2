package com.bookie.servlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookie.bizlogic.BookService;
import com.bookie.bizlogic.interfaces.BookServiceInterface;
import com.bookie.models.Book;

@WebServlet("/Search")
public class Search extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	public Search() {
		super();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String input_phrase = request.getParameter("search-input");

	    BookServiceInterface book = BookService.getServiceInstance();
	    List<Book> search_result = book.bookSearch(input_phrase);

	    if (search_result != null && !search_result.isEmpty()) {
	        request.setAttribute("search_result", search_result);
	        request.getRequestDispatcher("/pages/search.jsp").forward(request, response);
	    } else {
	        // Forward to the same page with an empty search_result list to show "No books found"
	        request.setAttribute("search_result", new ArrayList<Book>());
	        request.getRequestDispatcher("/pages/search.jsp").forward(request, response);
	    }
	}

}
