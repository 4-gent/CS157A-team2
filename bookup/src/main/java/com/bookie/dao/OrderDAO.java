package com.bookie.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bookie.auth.UserContext;
import com.bookie.models.Address;
import com.bookie.models.Book;
import com.bookie.models.InventoryItem;
import com.bookie.models.Order;

public class OrderDAO extends BaseDAO<Order, Integer> {

    /**
     * Retrieve an Order by its ID, including its items and shipping address.
     */
	@Override
	public Order getById(Integer orderID) throws Exception {
	    // Step 1: Get the logged-in user's username from UserContext
	    String currentUsername = UserContext.getUserId();
	    if (currentUsername == null) {
	        throw new Exception("User is not logged in.");
	    }

	    // Step 2: Prepare the query to fetch the order details
	    String query = "SELECT o.orderID, o.username, o.total, o.orderDate, o.orderStatus, " +
	                   "a.addressID, a.street, a.city, a.state, a.zip, a.country " +
	                   "FROM Orders o " +
	                   "JOIN Addresses a ON o.addressID = a.addressID " +
	                   "WHERE o.orderID = ?";

	    PreparedStatement stmt = connection.prepareStatement(query);
	    stmt.setInt(1, orderID);
	    ResultSet rs = stmt.executeQuery();

	    Order order = null;

	    if (rs.next()) {
	        // Step 3: Fetch the owner of the order
	        String orderOwner = rs.getString("username");

	        // Step 4: Authorization check
	        // Check if the current user is either the owner of the order or an admin
	        if (!currentUsername.equals(orderOwner) && !UserDAO.isUserAnAdmin()) {
	            throw new Exception("Access denied: You do not have permission to access this order.");
	        }

	        // Step 5: Create the Address object
	        Address shippingAddress = new Address(
	            rs.getInt("addressID"),
	            rs.getString("street"),
	            rs.getString("city"),
	            rs.getString("state"),
	            rs.getString("zip"),
	            rs.getString("country")
	        );

	        // Step 6: Create and return the Order object
	        order = new Order(
	            rs.getInt("orderID"),
	            orderOwner,
	            rs.getDouble("total"),
	            shippingAddress,
	            rs.getDate("orderDate"),
	            rs.getString("orderStatus"),
	            getOrderItems(orderID) // Fetch the items associated with the order
	        );
	    } else {
	        throw new Exception("Order not found for ID: " + orderID);
	    }

	    return order;
	}
	
	
    /**
     * Retrieve all items associated with a specific order.
     */
    private List<InventoryItem> getOrderItems(int orderID) throws SQLException {
        List<InventoryItem> items = new ArrayList<>();
        String query = "SELECT i.inventoryItemID, i.ISBN, i.price, c.quantity, i.description, " +
                       "b.title, b.year, b.publisher, b.isFeatured " +
                       "FROM Contains c " +
                       "JOIN InventoryItems i ON c.inventoryItemID = i.inventoryItemID " +
                       "JOIN Books b ON i.ISBN = b.ISBN " +
                       "WHERE c.orderID = ?";

        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, orderID);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Book book = new Book(
                rs.getString("ISBN"),
                rs.getString("title"),
                rs.getInt("year"),
                rs.getString("publisher"),
                rs.getBoolean("isFeatured"),
                null,
                null
            );

            InventoryItem item = new InventoryItem(
                rs.getInt("inventoryItemID"),
                book,
                rs.getDouble("price"),
                rs.getInt("quantity"),
                rs.getString("description")
            );

            items.add(item);
        }
        return items;
    }

    /**
     * Update the status of an existing order.
     */
    public boolean updateStatus(int orderID, String status) {
        try {
            String query = "UPDATE Orders SET orderStatus = ? WHERE orderID = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, status);
            stmt.setInt(2, orderID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Soft delete an order by setting its status to 'Cancelled'.
     */
    @Override
    public boolean delete(Integer orderID) throws Exception {
        try {
            // Step 1: Get the logged-in user's username from UserContext
            String currentUsername = UserContext.getUserId();
            if (currentUsername == null) {
                throw new Exception("User is not logged in.");
            }

            // Step 2: Fetch the username associated with the given orderID
            String query = "SELECT username FROM Orders WHERE orderID = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, orderID);
            ResultSet rs = stmt.executeQuery();

            String orderUsername = null;
            if (rs.next()) {
                orderUsername = rs.getString("username");
            } else {
                throw new Exception("Order not found for ID: " + orderID);
            }

            // Step 3: Authorization check
            // If the user is not the owner and not an admin, throw an exception
            if (!currentUsername.equals(orderUsername) && !UserDAO.isUserAnAdmin()) {
                throw new Exception("Access denied: You do not have permission to cancel this order.");
            }

            // Step 4: If validation passes, cancel the order
            query = "UPDATE Orders SET orderStatus = 'Cancelled' WHERE orderID = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, orderID);
            int rowsAffected = stmt.executeUpdate();

            // Check if the update was successful
            if (rowsAffected > 0) {
                return true;
            } else {
                throw new Exception("Failed to cancel the order. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Database error occurred while cancelling the order.");
        }
    }

	@Override
	public Order add(Order t) throws Exception {
		throw new Exception("Order is created by checking out, look into CartService checkout()");
	}
	
	/**
	 * Get all orders for a specific user with orderID, orderDate, orderStatus, total, and shipping address.
	 */
	public List<Order> getOrdersByUsername(String username) throws SQLException {
	    List<Order> orders = new ArrayList<>();
	    
	    String query = "SELECT o.orderID, o.orderDate, o.orderStatus, o.total, " +
	                   "a.addressID, a.street, a.city, a.state, a.zip, a.country " +
	                   "FROM Orders o " +
	                   "JOIN Addresses a ON o.addressID = a.addressID " +
	                   "WHERE o.username = ? " +
	                   "ORDER BY o.orderDate DESC";

	    PreparedStatement stmt = connection.prepareStatement(query);
	    stmt.setString(1, username);
	    ResultSet rs = stmt.executeQuery();

	    while (rs.next()) {
	        // Create an Address object for the shipping address
	        Address shippingAddress = new Address(
	            rs.getInt("addressID"),
	            rs.getString("street"),
	            rs.getString("city"),
	            rs.getString("state"),
	            rs.getString("zip"),
	            rs.getString("country")
	        );

	        // Create a minimal Order object with the required fields
	        Order order = new Order(
	            rs.getInt("orderID"),
	            username,
	            rs.getDouble("total"),
	            shippingAddress,
	            rs.getDate("orderDate"),
	            rs.getString("orderStatus"),
	            null // items are not needed here
	        );

	        orders.add(order);
	    }
	    
	    return orders;
	}
	
	/**
	 * Get all orders with a specific status, including orderID, username, orderDate, total, and shipping address.
	 */
	public List<Order> getAllOrdersByStatus(String status) throws SQLException {
	    List<Order> orders = new ArrayList<>();
	    
	    String query = "SELECT o.orderID, o.username, o.orderDate, o.orderStatus, o.total, " +
	                   "a.addressID, a.street, a.city, a.state, a.zip, a.country " +
	                   "FROM Orders o " +
	                   "JOIN Addresses a ON o.addressID = a.addressID " +
	                   "WHERE o.orderStatus = ? " +
	                   "ORDER BY o.orderDate DESC";

	    PreparedStatement stmt = connection.prepareStatement(query);
	    stmt.setString(1, status);
	    ResultSet rs = stmt.executeQuery();

	    while (rs.next()) {
	        // Create an Address object for the shipping address
	        Address shippingAddress = new Address(
	            rs.getInt("addressID"),
	            rs.getString("street"),
	            rs.getString("city"),
	            rs.getString("state"),
	            rs.getString("zip"),
	            rs.getString("country")
	        );

	        // Create an Order object with the retrieved fields
	        Order order = new Order(
	            rs.getInt("orderID"),
	            rs.getString("username"),
	            rs.getDouble("total"),
	            shippingAddress,
	            rs.getDate("orderDate"),
	            rs.getString("orderStatus"),
	            null // items are not needed here
	        );

	        orders.add(order);
	    }
	    
	    return orders;
	}
	
	/**
	 * Get all orders within a specific date range.
	 */
	public List<Order> getAllOrders(Date fromDate, Date toDate) throws SQLException {
	    List<Order> orders = new ArrayList<>();
	    
	    String query = "SELECT o.orderID, o.username, o.orderDate, o.orderStatus, o.total, " +
	                   "a.addressID, a.street, a.city, a.state, a.zip, a.country " +
	                   "FROM Orders o " +
	                   "JOIN Addresses a ON o.addressID = a.addressID " +
	                   "WHERE o.orderDate BETWEEN ? AND ? " +
	                   "ORDER BY o.orderDate DESC";

	    PreparedStatement stmt = connection.prepareStatement(query);
	    stmt.setDate(1, new java.sql.Date(fromDate.getTime()));
	    stmt.setDate(2, new java.sql.Date(toDate.getTime()));
	    ResultSet rs = stmt.executeQuery();

	    while (rs.next()) {
	        // Create an Address object for the shipping address
	        Address shippingAddress = new Address(
	            rs.getInt("addressID"),
	            rs.getString("street"),
	            rs.getString("city"),
	            rs.getString("state"),
	            rs.getString("zip"),
	            rs.getString("country")
	        );

	        // Create an Order object with the retrieved fields
	        Order order = new Order(
	            rs.getInt("orderID"),
	            rs.getString("username"),
	            rs.getDouble("total"),
	            shippingAddress,
	            rs.getDate("orderDate"),
	            rs.getString("orderStatus"),
	            null // items are not needed here
	        );

	        orders.add(order);
	    }
	    
	    return orders;
	}
	
	/**
	 * Get all orders for a user by searching for a keyword in past orders.
	 * The search is performed on book title, author name, and publisher name.
	 */
	public List<Order> getAllOrdersByKeyword(String username, String searchKeyWord) throws SQLException {
	    List<Order> orders = new ArrayList<>();

	    String query = 
	        "SELECT o.orderID, o.orderDate, o.orderStatus, o.total, " +
	        "a.addressID, a.street, a.city, a.state, a.zip, a.country " +
	        "FROM Orders o " +
	        "JOIN Contains c ON o.orderID = c.orderID " +
	        "JOIN InventoryItems i ON c.inventoryItemID = i.inventoryItemID " +
	        "JOIN Books b ON i.ISBN = b.ISBN " +
	        "LEFT JOIN Written w ON b.ISBN = w.ISBN " +
	        "LEFT JOIN Authors au ON w.authorID = au.authorID " +
	        "JOIN Addresses a ON o.addressID = a.addressID " +
	        "WHERE o.username = ? " +
	        "AND (b.title LIKE ? OR b.publisher LIKE ? OR au.name LIKE ?) " +
	        "ORDER BY o.orderDate DESC";

	    PreparedStatement stmt = connection.prepareStatement(query);
	    stmt.setString(1, username);
	    String keywordPattern = "%" + searchKeyWord + "%";
	    stmt.setString(2, keywordPattern);
	    stmt.setString(3, keywordPattern);
	    stmt.setString(4, keywordPattern);

	    ResultSet rs = stmt.executeQuery();

	    while (rs.next()) {
	        // Create an Address object for the shipping address
	        Address shippingAddress = new Address(
	            rs.getInt("addressID"),
	            rs.getString("street"),
	            rs.getString("city"),
	            rs.getString("state"),
	            rs.getString("zip"),
	            rs.getString("country")
	        );

	        // Create an Order object with the retrieved fields
	        Order order = new Order(
	            rs.getInt("orderID"),
	            username,
	            rs.getDouble("total"),
	            shippingAddress,
	            rs.getDate("orderDate"),
	            rs.getString("orderStatus"),
	            null // items are not needed here
	        );

	        orders.add(order);
	    }

	    return orders;
	}
	
	/**
	 * Get all orders by searching for a keyword in past orders.
	 * The search is performed on book title, author name, and publisher name.
	 */
	public List<Order> getAllOrdersByKeyword(String searchKeyWord) throws SQLException {
	    List<Order> orders = new ArrayList<>();

	    String query = 
	        "SELECT o.orderID, o.username, o.orderDate, o.orderStatus, o.total, " +
	        "a.addressID, a.street, a.city, a.state, a.zip, a.country " +
	        "FROM Orders o " +
	        "JOIN Contains c ON o.orderID = c.orderID " +
	        "JOIN InventoryItems i ON c.inventoryItemID = i.inventoryItemID " +
	        "JOIN Books b ON i.ISBN = b.ISBN " +
	        "LEFT JOIN Written w ON b.ISBN = w.ISBN " +
	        "LEFT JOIN Authors au ON w.authorID = au.authorID " +
	        "JOIN Addresses a ON o.addressID = a.addressID " +
	        "WHERE (b.title LIKE ? OR b.publisher LIKE ? OR au.name LIKE ?) " +
	        "ORDER BY o.orderDate DESC";

	    PreparedStatement stmt = connection.prepareStatement(query);
	    String keywordPattern = "%" + searchKeyWord + "%";
	    stmt.setString(1, keywordPattern);
	    stmt.setString(2, keywordPattern);
	    stmt.setString(3, keywordPattern);

	    ResultSet rs = stmt.executeQuery();

	    while (rs.next()) {
	        // Create an Address object for the shipping address
	        Address shippingAddress = new Address(
	            rs.getInt("addressID"),
	            rs.getString("street"),
	            rs.getString("city"),
	            rs.getString("state"),
	            rs.getString("zip"),
	            rs.getString("country")
	        );

	        // Create an Order object with the retrieved fields
	        Order order = new Order(
	            rs.getInt("orderID"),
	            rs.getString("username"),
	            rs.getDouble("total"),
	            shippingAddress,
	            rs.getDate("orderDate"),
	            rs.getString("orderStatus"),
	            null // items are not needed here
	        );

	        orders.add(order);
	    }

	    return orders;
	}

	@Override
	public boolean update(Order t) throws Exception {
		throw new Exception("Not implmeneted");
	}
	
}