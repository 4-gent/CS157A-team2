import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

@WebServlet("/cart.jsp")
public class AddToCartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Parse JSON input
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        }
        String isbn = new Gson().fromJson(jsonBuilder.toString(), Map.class).get("ISBN").toString();

        // Retrieve cart from session (or create a new one)
        HttpSession session = request.getSession();
        List<String> cart = (List<String>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        // Add book ISBN to the cart
        cart.add(isbn);
        session.setAttribute("cart", cart);

        // Respond with success
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
