package com.bookie.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bookie.errors.NotEnoughQuantityException;
import com.bookie.models.Address;
import com.bookie.models.Book;
import com.bookie.models.Cart;
import com.bookie.models.CartItem;
import com.bookie.models.InventoryItem;
import com.bookie.models.Order;

public class CartDAO extends BaseDAO<Cart, Integer> {

    @Override
    public Cart getById(Integer id) throws Exception {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public Cart add(Cart cart) throws Exception {
        if (cart == null || cart.getUsername() == null || cart.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Invalid cart or username provided.");
        }

        String insertCartQuery = "INSERT INTO Cart (username, cartTotal) VALUES (?, 0.0)";
        try (PreparedStatement stmt = connection.prepareStatement(insertCartQuery, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cart.getUsername());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to create an empty cart for user: " + cart.getUsername());
            }

            // Retrieve the generated cart ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cart.setCartID(generatedKeys.getInt(1));
                    cart.setTotal(0.0); // Ensure the total is explicitly set to 0.0
                    return cart;
                } else {
                    throw new SQLException("Failed to retrieve the cart ID for the newly created cart.");
                }
            }
        }
    }
    @Override
    public boolean update(Cart cart) throws Exception {
        throw new UnsupportedOperationException("Use updateInventoryItems or checkout.");
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        throw new UnsupportedOperationException("Delete specific items or clear cart via removeItemsFromCart.");
    }

    public Cart getCartByUsername(String username) throws SQLException {
        Cart cart = null;

        String query = "SELECT c.cartID, c.cartTotal, i.inventoryItemID, i.price, inc.quantity, i.description, " +
                       "b.ISBN, b.title, b.year, b.publisher, b.isFeatured " +
                       "FROM Cart c " +
                       "LEFT JOIN Includes inc ON c.cartID = inc.cartID " +
                       "LEFT JOIN InventoryItems i ON inc.inventoryItemID = i.inventoryItemID " +
                       "LEFT JOIN Books b ON i.ISBN = b.ISBN " +
                       "WHERE c.username = ?";

        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();

        List<CartItem> cartItems = new ArrayList<>();
        int cartID = -1;
        double total = 0.0;

        while (rs.next()) {
            if (cart == null) {
                cartID = rs.getInt("cartID");
                total = rs.getDouble("cartTotal");
                cart = new Cart(cartID, username, total);
            }

            if (rs.getInt("inventoryItemID") != 0) {
                Book book = new Book(
                    rs.getString("ISBN"),
                    rs.getString("title"),
                    rs.getInt("year"),
                    rs.getString("publisher"),
                    rs.getBoolean("isFeatured"),
                    null, null
                );

                InventoryItem inventoryItem = new InventoryItem(
                    rs.getInt("inventoryItemID"),
                    book,
                    rs.getDouble("price"),
                    rs.getInt("quantity"),
                    rs.getString("description")
                );

                CartItem cartItem = new CartItem(inventoryItem, rs.getInt("quantity"));
                cartItems.add(cartItem);
            }
        }

        if (cart != null) {
            cart.setCartItems(cartItems);
        }

        return cart;
    }
    
    public Cart addItemsToCart(String username, List<CartItem> items) throws Exception {
        Cart cart = null;
        try {
            connection.setAutoCommit(false);

            int cartID = ensureCartExists(username);

            System.out.println("CartID: " + cartID);
            
            for (CartItem item : items) {
                System.out.println("Item: " + item.getInventoryItem().toString() + ", Quantity: " + item.getQuantity());
            }
            
            for (CartItem item : items) {
            	validateInventoryAvailability(item);
                
                // Update inventory quantity
                String updateQtyQuery = "UPDATE InventoryItems SET qty = qty - ? WHERE inventoryItemID = ?";
                try (PreparedStatement updateQtyStmt = connection.prepareStatement(updateQtyQuery)) {
                    updateQtyStmt.setInt(1, item.getQuantity());
                    updateQtyStmt.setInt(2, item.getInventoryItem().getInventoryItemID());
                    updateQtyStmt.executeUpdate();
                }

                // Add or update the cart item
                String upsertQuery = "INSERT INTO Includes (cartID, inventoryItemID, quantity) " +
                                     "VALUES (?, ?, ?) " +
                                     "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)";
                try (PreparedStatement upsertStmt = connection.prepareStatement(upsertQuery)) {
                    upsertStmt.setInt(1, cartID);
                    upsertStmt.setInt(2, item.getInventoryItem().getInventoryItemID());
                    upsertStmt.setInt(3, item.getQuantity());
                    upsertStmt.executeUpdate();
                }
            }

            updateCartTotal(cartID);

            cart = getCartByUsername(username);
            connection.commit();

        } catch (Exception e) {
            connection.rollback();
            throw new NotEnoughQuantityException("Not Enough Quantity Available");
        } finally {
            connection.setAutoCommit(true);
        }
        return cart;
    }

    public Cart removeItemsFromCart(String username, List<Integer> items) throws Exception {
        Cart cart = null;
        try {
            connection.setAutoCommit(false);

            int cartID = getCartIDByUsername(username);

            for (Integer itemID : items) {
                String deleteQuery = "DELETE FROM Includes WHERE cartID = ? AND inventoryItemID = ?";
                try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
                    deleteStmt.setInt(1, cartID);
                    deleteStmt.setInt(2, itemID);
                    deleteStmt.executeUpdate();
                }
            }

            updateCartTotal(cartID);
            cart = getCartByUsername(username);
            connection.commit();

        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
        return cart;
    }

    public Cart updateInventoryItems(String username, List<CartItem> items) throws Exception {
        Cart cart = null;
        try {
            connection.setAutoCommit(false);

            int cartID = ensureCartExists(username);

            for (CartItem item : items) {
                if (item.getQuantity() == 0) {
                    // Remove item from cart and restore inventory
                    removeCartItem(cartID, item.getInventoryItem().getInventoryItemID());
                } else {
                    upsertCartItem(cartID, item.getInventoryItem());
                }
            }

            updateCartTotal(cartID);
            cart = getCartByUsername(username);

            connection.commit();

        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
        return cart;
    }

    public Order checkout(String username, int addressID, int paymentID) throws Exception {
        Order order = null;

        try {
            connection.setAutoCommit(false);

            Cart cart = getCartByUsername(username);
            if (cart == null || cart.getCartItems().isEmpty()) {
                throw new Exception("Cart is empty or does not exist.");
            }

            int orderID = createOrder(username, addressID, paymentID, cart.getTotal());

            insertOrderItems(orderID, cart.getCartItems(), addressID);

            clearCart(cart.getCartID());

            Address shippingAddress = new AddressDAO().getById(addressID);

            order = new Order(orderID, username, cart.getTotal(), shippingAddress, new java.sql.Date(System.currentTimeMillis()), "Pending", cart.getCartItems());

            connection.commit();

        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
        return order;
    }
    
    public int getCartIDByUsername(String username) throws SQLException {
        String query = "SELECT cartID FROM Cart WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cartID");
                } else {
                    throw new SQLException("Cart not found for username: " + username);
                }
            }
        }
    }
    
    private int ensureCartExists(String username) throws SQLException {
        String checkCartQuery = "SELECT cartID FROM Cart WHERE username = ?";
        try (PreparedStatement checkCartStmt = connection.prepareStatement(checkCartQuery)) {
            checkCartStmt.setString(1, username);
            ResultSet rs = checkCartStmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("cartID");
            } else {
                String createCartQuery = "INSERT INTO Cart (username, cartTotal) VALUES (?, 0)";
                try (PreparedStatement createCartStmt = connection.prepareStatement(createCartQuery, Statement.RETURN_GENERATED_KEYS)) {
                    createCartStmt.setString(1, username);
                    createCartStmt.executeUpdate();
                    ResultSet generatedKeys = createCartStmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        }
        throw new SQLException("Failed to ensure cart existence for user: " + username);
    }
    
    private void validateInventoryAvailability(CartItem item) throws Exception {
        String checkQtyQuery = "SELECT qty FROM InventoryItems WHERE inventoryItemID = ?";
        System.out.println("Item quyantity in validation: " + item.getQuantity());
        try (PreparedStatement checkQtyStmt = connection.prepareStatement(checkQtyQuery)) {
            checkQtyStmt.setInt(1, item.getInventoryItem().getInventoryItemID());
            ResultSet rs = checkQtyStmt.executeQuery();
            if (rs.next()) {
                int availableQty = rs.getInt("qty");
                System.out.println("Available qty: " + availableQty);
                if (availableQty < item.getQuantity()) {
                    throw new Exception("Not enough quantity available for item ID: " + item.getInventoryItem().getInventoryItemID());
                }
            } else {
                throw new Exception("Inventory item not found for item ID: " + item.getInventoryItem().getInventoryItemID());
            }
        }
    }
    
    private void upsertCartItem(int cartID, InventoryItem item) throws SQLException {
        String upsertQuery = "INSERT INTO Includes (cartID, inventoryItemID, quantity) "
                           + "VALUES (?, ?, ?) "
                           + "ON DUPLICATE KEY UPDATE quantity = ?";
        try (PreparedStatement upsertStmt = connection.prepareStatement(upsertQuery)) {
            upsertStmt.setInt(1, cartID);
            upsertStmt.setInt(2, item.getInventoryItemID());
            upsertStmt.setInt(3, item.getQty());
            upsertStmt.setInt(4, item.getQty());
            upsertStmt.executeUpdate();
        }
    }
    
    private void removeCartItem(int cartID, int inventoryItemID) throws SQLException {
        String getQtyQuery = "SELECT quantity FROM Includes WHERE cartID = ? AND inventoryItemID = ?";
        int quantityToRestore = 0;

        try (PreparedStatement getQtyStmt = connection.prepareStatement(getQtyQuery)) {
            getQtyStmt.setInt(1, cartID);
            getQtyStmt.setInt(2, inventoryItemID);
            ResultSet rs = getQtyStmt.executeQuery();
            if (rs.next()) {
                quantityToRestore = rs.getInt("quantity");
            }
        }

        // Restore inventory quantity
        String restoreQtyQuery = "UPDATE InventoryItems SET qty = qty + ? WHERE inventoryItemID = ?";
        try (PreparedStatement restoreQtyStmt = connection.prepareStatement(restoreQtyQuery)) {
            restoreQtyStmt.setInt(1, quantityToRestore);
            restoreQtyStmt.setInt(2, inventoryItemID);
            restoreQtyStmt.executeUpdate();
        }

        // Remove the item from the cart
        String deleteQuery = "DELETE FROM Includes WHERE cartID = ? AND inventoryItemID = ?";
        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, cartID);
            deleteStmt.setInt(2, inventoryItemID);
            deleteStmt.executeUpdate();
        }
    }
    
    private void updateCartTotal(int cartID) throws SQLException {
        String calculateTotalQuery = "SELECT SUM(i.price * inc.quantity) AS total "
                                   + "FROM Includes inc "
                                   + "INNER JOIN InventoryItems i ON inc.inventoryItemID = i.inventoryItemID "
                                   + "WHERE inc.cartID = ?";

        double total = 0.0;
        try (PreparedStatement calculateTotalStmt = connection.prepareStatement(calculateTotalQuery)) {
            calculateTotalStmt.setInt(1, cartID);
            ResultSet rs = calculateTotalStmt.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("total");
            }
        }

        String updateTotalQuery = "UPDATE Cart SET cartTotal = ? WHERE cartID = ?";
        try (PreparedStatement updateTotalStmt = connection.prepareStatement(updateTotalQuery)) {
            updateTotalStmt.setDouble(1, total);
            updateTotalStmt.setInt(2, cartID);
            updateTotalStmt.executeUpdate();
        }
    }
    
    
    private int createOrder(String username, int addressID, int paymentID, double total) throws SQLException {
        String createOrderQuery = "INSERT INTO Orders (username, addressID, paymentID, orderDate, orderStatus, total) "
                                 + "VALUES (?, ?, ?, CURDATE(), 'Pending', ?)";
        try (PreparedStatement orderStmt = connection.prepareStatement(createOrderQuery, Statement.RETURN_GENERATED_KEYS)) {
            orderStmt.setString(1, username);
            orderStmt.setInt(2, addressID);
            orderStmt.setInt(3, paymentID);
            orderStmt.setDouble(4, total);

            int rowsAffected = orderStmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            ResultSet generatedKeys = orderStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating order failed, no ID obtained.");
            }
        }
    }
    
    private void insertOrderItems(int orderID, List<CartItem> cartItems, int addressID) throws SQLException {
        String insertContainsQuery = "INSERT INTO Contains (orderID, inventoryItemID, addressID, quantity) "
                                    + "VALUES (?, ?, ?, ?)";
        try (PreparedStatement containsStmt = connection.prepareStatement(insertContainsQuery)) {
            for (CartItem cartItem : cartItems) {
                containsStmt.setInt(1, orderID);
                containsStmt.setInt(2, cartItem.getInventoryItem().getInventoryItemID());
                containsStmt.setInt(3, addressID);
                containsStmt.setInt(4, cartItem.getQuantity());
                containsStmt.addBatch();
            }
            containsStmt.executeBatch();
        }
    }
    
    private void clearCart(int cartID) throws SQLException {
        String clearCartQuery = "DELETE FROM Includes WHERE cartID = ?";
        try (PreparedStatement clearCartStmt = connection.prepareStatement(clearCartQuery)) {
            clearCartStmt.setInt(1, cartID);
            clearCartStmt.executeUpdate();
        }
    }
    
    public boolean deleteByUsername(String username) throws SQLException {
        String query = "DELETE FROM Cart WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
}