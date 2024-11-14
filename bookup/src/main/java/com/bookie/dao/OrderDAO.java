package com.bookie.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        Order order = null;
        String query = "SELECT o.orderID, o.username, o.total, o.orderDate, o.orderStatus, " +
                       "a.addressID, a.street, a.city, a.state, a.zip, a.country " +
                       "FROM Orders o " +
                       "JOIN Addresses a ON o.addressID = a.addressID " +
                       "WHERE o.orderID = ?";

        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, orderID);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            // Fetch the shipping address
            Address shippingAddress = new Address(
                rs.getInt("addressID"),
                rs.getString("street"),
                rs.getString("city"),
                rs.getString("state"),
                rs.getString("zip"),
                rs.getString("country")
            );

            // Fetch the order details
            order = new Order(
                rs.getInt("orderID"),
                rs.getString("username"),
                rs.getDouble("total"),
                shippingAddress,
                rs.getDate("orderDate"),
                rs.getString("orderStatus"),
                getOrderItems(orderID)
            );
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
    @Override
    public boolean update(Order order) {
        try {
            String query = "UPDATE Orders SET orderStatus = ? WHERE orderID = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, order.getOrderStatus());
            stmt.setInt(2, order.getOrderID());
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
    public boolean delete(Integer orderID) {
        try {
            String query = "UPDATE Orders SET orderStatus = 'Cancelled' WHERE orderID = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, orderID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

	@Override
	public Order add(Order t) throws Exception {
		throw new Exception("Order is created by checking out, look into CartService checkout()");
	}
}