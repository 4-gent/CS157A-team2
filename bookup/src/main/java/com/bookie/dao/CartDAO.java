package com.bookie.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bookie.models.Book;
import com.bookie.models.Cart;
import com.bookie.models.InventoryItem;

public class CartDAO extends BaseDAO<Cart, Integer> {

	@Override
	public Cart getById(Integer id) throws Exception {
		throw new Exception("Methid not implemented");
	}

	public Cart getCartByUsername(String username) throws SQLException {
	    Cart cart = null;

	    // Step 1: Retrieve the cart details for the given username
	    String query = "SELECT cartID, cartTotal FROM Cart WHERE username = ?";
	    PreparedStatement stmt = connection.prepareStatement(query);
	    stmt.setString(1, username);
	    ResultSet rs = stmt.executeQuery();

	    int cartID;
	    double cartTotal;
	    if (rs.next()) {
	        cartID = rs.getInt("cartID");
	        cartTotal = rs.getDouble("cartTotal");

	        // Step 2: Create a Cart object with the retrieved details
	        cart = new Cart(cartID, username, cartTotal);

	        // Step 3: Fetch the associated InventoryItems for the cart
	        List<InventoryItem> items = getCartItems(cartID);
	        cart.setInventoryItems(items);
	    }

	    return cart;
	}
	
	@Override
	public Cart add(Cart cart) {  //User will have only one cart
	    try {
	        // Step 1: Check if a cart already exists for this user
	        String checkQuery = "SELECT cartID FROM Cart WHERE username = ?";
	        PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
	        checkStmt.setString(1, cart.getUsername());
	        ResultSet rs = checkStmt.executeQuery();

	        if (rs.next()) {
	            // If the user already has a cart, update it
	            int existingCartID = rs.getInt("cartID");
	            cart.setCartID(existingCartID);
	            update(cart);
	            return cart;
	        }

	        // Step 2: If no cart exists, create a new one
	        String insertCartQuery = "INSERT INTO Cart (username) VALUES (?)";
	        PreparedStatement insertCartStmt = connection.prepareStatement(insertCartQuery, Statement.RETURN_GENERATED_KEYS);
	        insertCartStmt.setString(1, cart.getUsername());
	        int rowsAffected = insertCartStmt.executeUpdate();

	        if (rowsAffected > 0) {
	            ResultSet generatedKeys = insertCartStmt.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                cart.setCartID(generatedKeys.getInt(1));
	            }

	            // Step 3: Insert associated inventory items into the Includes table
	            for (InventoryItem item : cart.getInventoryItems()) {
	                String includesQuery = "INSERT INTO Includes (cartID, inventoryItemID, quantity) VALUES (?, ?, ?)";
	                PreparedStatement includesStmt = connection.prepareStatement(includesQuery);
	                includesStmt.setInt(1, cart.getCartID());
	                includesStmt.setInt(2, item.getInventoryItemID());
	                includesStmt.setInt(3, item.getQty());
	                includesStmt.executeUpdate();
	            }
	        }
	        return cart;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	@Override
	public boolean update(Cart cart) {
	    try {
	        // Start a transaction
	        connection.setAutoCommit(false);

	        int cartID = cart.getCartID();
	        List<InventoryItem> items = cart.getInventoryItems();

	        // Step 1: Update the items in the Includes table
	        for (InventoryItem item : items) {
	            if (item.getQty() == 0) {
	                // If the quantity is zero, delete the item from the cart
	                String deleteQuery = "DELETE FROM Includes WHERE cartID = ? AND inventoryItemID = ?";
	                PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery);
	                deleteStmt.setInt(1, cartID);
	                deleteStmt.setInt(2, item.getInventoryItemID());
	                deleteStmt.executeUpdate();

	                // Restore the quantity back to the inventory
	                String restoreQtyQuery = "UPDATE InventoryItems SET qty = qty + ? WHERE inventoryItemID = ?";
	                PreparedStatement restoreQtyStmt = connection.prepareStatement(restoreQtyQuery);
	                restoreQtyStmt.setInt(1, item.getQty());
	                restoreQtyStmt.setInt(2, item.getInventoryItemID());
	                restoreQtyStmt.executeUpdate();

	            } else {
	                // Check if the item already exists in the cart
	                String checkQuery = "SELECT quantity FROM Includes WHERE cartID = ? AND inventoryItemID = ?";
	                PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
	                checkStmt.setInt(1, cartID);
	                checkStmt.setInt(2, item.getInventoryItemID());
	                ResultSet rs = checkStmt.executeQuery();

	                if (rs.next()) {
	                    // If the item exists, update its quantity
	                    String updateQuery = "UPDATE Includes SET quantity = ? WHERE cartID = ? AND inventoryItemID = ?";
	                    PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
	                    updateStmt.setInt(1, item.getQty());
	                    updateStmt.setInt(2, cartID);
	                    updateStmt.setInt(3, item.getInventoryItemID());
	                    updateStmt.executeUpdate();
	                } else {
	                    // If the item does not exist, insert it into the Includes table
	                    String insertQuery = "INSERT INTO Includes (cartID, inventoryItemID, quantity) VALUES (?, ?, ?)";
	                    PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
	                    insertStmt.setInt(1, cartID);
	                    insertStmt.setInt(2, item.getInventoryItemID());
	                    insertStmt.setInt(3, item.getQty());
	                    insertStmt.executeUpdate();
	                }
	            }
	        }

	        // Step 2: Recalculate the cart total
	        updateCartTotal(cartID);

	        // Commit the transaction
	        connection.commit();
	        return true;

	    } catch (SQLException e) {
	        try {
	            connection.rollback();
	        } catch (SQLException rollbackEx) {
	            rollbackEx.printStackTrace();
	        }
	        e.printStackTrace();
	        return false;
	    } finally {
	        try {
	            connection.setAutoCommit(true);
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    }
	}

	@Override
	public boolean delete(Integer id) {
	    try {
	        // Start a transaction
	        connection.setAutoCommit(false);

	        // Step 1: Delete associated items from the Includes table
	        String deleteIncludesQuery = "DELETE FROM Includes WHERE cartID = ?";
	        PreparedStatement includesStmt = connection.prepareStatement(deleteIncludesQuery);
	        includesStmt.setInt(1, id);
	        includesStmt.executeUpdate();

	        // Step 2: Delete the cart itself
	        String deleteCartQuery = "DELETE FROM Cart WHERE cartID = ?";
	        PreparedStatement cartStmt = connection.prepareStatement(deleteCartQuery);
	        cartStmt.setInt(1, id);
	        int rowsAffected = cartStmt.executeUpdate();

	        // Commit the transaction if everything is successful
	        connection.commit();
	        return rowsAffected > 0;

	    } catch (SQLException e) {
	        try {
	            // Rollback the transaction in case of errors
	            connection.rollback();
	        } catch (SQLException rollbackEx) {
	            rollbackEx.printStackTrace();
	        }
	        e.printStackTrace();
	    } finally {
	        try {
	            // Reset auto-commit to true after transaction is complete
	            connection.setAutoCommit(true);
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    }
	    return false;
	}
	
	public Cart addItemsToCart(String username, List<InventoryItem> items) throws Exception {
	    Cart cart = null;
	    try {
	        connection.setAutoCommit(false);

	        // Step 1: Check if a cart already exists for the user
	        String checkCartQuery = "SELECT cartID, cartTotal FROM Cart WHERE username = ?";
	        PreparedStatement checkCartStmt = connection.prepareStatement(checkCartQuery);
	        checkCartStmt.setString(1, username);
	        ResultSet rs = checkCartStmt.executeQuery();

	        int cartID;
	        if (rs.next()) {
	            cartID = rs.getInt("cartID");
	        } else {
	            // Create a new cart if it doesn't exist
	            String insertCartQuery = "INSERT INTO Cart (username, cartTotal) VALUES (?, 0)";
	            PreparedStatement insertCartStmt = connection.prepareStatement(insertCartQuery, Statement.RETURN_GENERATED_KEYS);
	            insertCartStmt.setString(1, username);
	            insertCartStmt.executeUpdate();

	            ResultSet generatedKeys = insertCartStmt.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                cartID = generatedKeys.getInt(1);
	            } else {
	                throw new SQLException("Failed to create a new cart.");
	            }
	        }

	        // Step 2: Add items to the cart and update inventory quantities
	        
	        for (InventoryItem item : items) {
	            // Check if the required quantity is available
	            String checkQtyQuery = "SELECT qty, price FROM InventoryItems WHERE inventoryItemID = ?";
	            PreparedStatement checkQtyStmt = connection.prepareStatement(checkQtyQuery);
	            checkQtyStmt.setInt(1, item.getInventoryItemID());
	            ResultSet qtyRs = checkQtyStmt.executeQuery();

	            if (qtyRs.next()) {
	                int availableQty = qtyRs.getInt("qty");
	                if (availableQty < item.getQty()) {
	                    throw new Exception("Not enough quantity available");
	                }

	                // Decrease the quantity in the InventoryItems table
	                String updateQtyQuery = "UPDATE InventoryItems SET qty = qty - ? WHERE inventoryItemID = ?";
	                PreparedStatement updateQtyStmt = connection.prepareStatement(updateQtyQuery);
	                updateQtyStmt.setInt(1, item.getQty());
	                updateQtyStmt.setInt(2, item.getInventoryItemID());
	                updateQtyStmt.executeUpdate();

	            }

	            // Add or update the item in the Includes table
	            String upsertQuery = "INSERT INTO Includes (cartID, inventoryItemID, quantity) VALUES (?, ?, ?) " +
	                                 "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)";
	            PreparedStatement upsertStmt = connection.prepareStatement(upsertQuery);
	            upsertStmt.setInt(1, cartID);
	            upsertStmt.setInt(2, item.getInventoryItemID());
	            upsertStmt.setInt(3, item.getQty());
	            upsertStmt.executeUpdate();
	        }

	        // Step 3: Update the cart total
	        updateCartTotal(cartID);

	        // Step 4: Fetch the updated cart with items to return
	        cart = getById(cartID);
	        connection.commit();

	    } catch (Exception e) {
	        connection.rollback();
	        throw e;
	    } finally {
	        connection.setAutoCommit(true);
	    }
	    return cart;
	}
	
	public Cart removeItemsFromCart(String username, List<Integer> items) throws Exception {
	    Cart cart = null;
	    try {
	        connection.setAutoCommit(false);

	        String getCartQuery = "SELECT cartID FROM Cart WHERE username = ?";
	        PreparedStatement getCartStmt = connection.prepareStatement(getCartQuery);
	        getCartStmt.setString(1, username);
	        ResultSet rs = getCartStmt.executeQuery();

	        int cartID;
	        if (rs.next()) {
	            cartID = rs.getInt("cartID");
	        } else {
	            return null;
	        }

	        for (Integer itemID : items) {
	            String deleteQuery = "DELETE FROM Includes WHERE cartID = ? AND inventoryItemID = ?";
	            PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery);
	            deleteStmt.setInt(1, cartID);
	            deleteStmt.setInt(2, itemID);
	            deleteStmt.executeUpdate();
	        }

	        // Update cart total
	        updateCartTotal(cartID);
	        cart = getById(cartID);
	        connection.commit();

	    } catch (Exception e) {
	        connection.rollback();
	        throw e;
	    } finally {
	        connection.setAutoCommit(true);
	    }
	    return cart;
	}
	
	
	public Cart updateInventoryItems(String username, List<InventoryItem> items) throws Exception {
	    Cart cart = null;
	    try {
	        // Start a transaction
	        connection.setAutoCommit(false);

	        // Step 1: Retrieve the cartID for the given user
	        String getCartQuery = "SELECT cartID FROM Cart WHERE username = ?";
	        PreparedStatement getCartStmt = connection.prepareStatement(getCartQuery);
	        getCartStmt.setString(1, username);
	        ResultSet cartRs = getCartStmt.executeQuery();

	        int cartID;
	        if (cartRs.next()) {
	            cartID = cartRs.getInt("cartID");
	        } else {
	            // If no cart exists for the user, create a new cart
	            String insertCartQuery = "INSERT INTO Cart (username) VALUES (?)";
	            PreparedStatement insertCartStmt = connection.prepareStatement(insertCartQuery, Statement.RETURN_GENERATED_KEYS);
	            insertCartStmt.setString(1, username);
	            insertCartStmt.executeUpdate();

	            ResultSet generatedKeys = insertCartStmt.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                cartID = generatedKeys.getInt(1);
	            } else {
	                throw new SQLException("Failed to create a new cart.");
	            }
	        }

	        // Step 2: Upsert items in the Includes table based on the provided list
	        for (InventoryItem item : items) {
	            if (item.getQty() == 0) {
	                // If quantity is 0, delete the item from the cart
	                String deleteItemQuery = "DELETE FROM Includes WHERE cartID = ? AND inventoryItemID = ?";
	                PreparedStatement deleteItemStmt = connection.prepareStatement(deleteItemQuery);
	                deleteItemStmt.setInt(1, cartID);
	                deleteItemStmt.setInt(2, item.getInventoryItemID());
	                deleteItemStmt.executeUpdate();

	                // Also, increase the quantity back in the InventoryItems table
	                String restoreQtyQuery = "UPDATE InventoryItems SET qty = qty + ? WHERE inventoryItemID = ?";
	                PreparedStatement restoreQtyStmt = connection.prepareStatement(restoreQtyQuery);
	                restoreQtyStmt.setInt(1, item.getQty());
	                restoreQtyStmt.setInt(2, item.getInventoryItemID());
	                restoreQtyStmt.executeUpdate();

	            } else {
	                // Check if the item already exists in the cart
	                String checkItemQuery = "SELECT quantity FROM Includes WHERE cartID = ? AND inventoryItemID = ?";
	                PreparedStatement checkItemStmt = connection.prepareStatement(checkItemQuery);
	                checkItemStmt.setInt(1, cartID);
	                checkItemStmt.setInt(2, item.getInventoryItemID());
	                ResultSet itemRs = checkItemStmt.executeQuery();

	                if (itemRs.next()) {
	                    // If the item exists, update its quantity
	                    String updateItemQuery = "UPDATE Includes SET quantity = ? WHERE cartID = ? AND inventoryItemID = ?";
	                    PreparedStatement updateItemStmt = connection.prepareStatement(updateItemQuery);
	                    updateItemStmt.setInt(1, item.getQty());
	                    updateItemStmt.setInt(2, cartID);
	                    updateItemStmt.setInt(3, item.getInventoryItemID());
	                    updateItemStmt.executeUpdate();
	                } else {
	                    // If the item does not exist, insert it into the Includes table
	                    String insertItemQuery = "INSERT INTO Includes (cartID, inventoryItemID, quantity) VALUES (?, ?, ?)";
	                    PreparedStatement insertItemStmt = connection.prepareStatement(insertItemQuery);
	                    insertItemStmt.setInt(1, cartID);
	                    insertItemStmt.setInt(2, item.getInventoryItemID());
	                    insertItemStmt.setInt(3, item.getQty());
	                    insertItemStmt.executeUpdate();
	                }

	                // Decrease the quantity from the InventoryItems table
	                String updateInventoryQuery = "UPDATE InventoryItems SET qty = qty - ? WHERE inventoryItemID = ?";
	                PreparedStatement updateInventoryStmt = connection.prepareStatement(updateInventoryQuery);
	                updateInventoryStmt.setInt(1, item.getQty());
	                updateInventoryStmt.setInt(2, item.getInventoryItemID());
	                updateInventoryStmt.executeUpdate();
	            }
	        }

	        // Commit the transaction
	        connection.commit();

	        // Fetch the updated cart with items to return
	        cart = getById(cartID);

	    } catch (Exception e) {
	        try {
	            // Rollback the transaction in case of errors
	            connection.rollback();
	        } catch (SQLException rollbackEx) {
	            rollbackEx.printStackTrace();
	        }
	        throw e;
	    } finally {
	        try {
	            // Reset auto-commit to true after transaction is complete
	            connection.setAutoCommit(true);
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    }
	    return cart;
	}
	
	/**
	 * This method will check out cart, and create the order and order Items
	 * @return
	 */
//	public Order checkOut() { //FIXME IMEPLEMENT ME
//		
//	}
	
	
	private void updateCartTotal(int cartID) throws SQLException {
	    // Constructing the query using string concatenation
	    String totalQuery = "SELECT SUM(i.price * inc.quantity) AS total " +
	                        "FROM Includes inc " +
	                        "JOIN InventoryItems i ON inc.inventoryItemID = i.inventoryItemID " +
	                        "WHERE inc.cartID = ?";

	    PreparedStatement totalStmt = connection.prepareStatement(totalQuery);
	    totalStmt.setInt(1, cartID);
	    ResultSet rs = totalStmt.executeQuery();

	    double cartTotal = 0.0;
	    if (rs.next()) {
	        cartTotal = rs.getDouble("total");
	    }

	    String updateQuery = "UPDATE Cart SET cartTotal = ? WHERE cartID = ?";
	    PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
	    updateStmt.setDouble(1, cartTotal);
	    updateStmt.setInt(2, cartID);
	    updateStmt.executeUpdate();
	}
	
	private List<InventoryItem> getCartItems(int cartID) throws SQLException {
	    List<InventoryItem> items = new ArrayList<>();

	    String query = "SELECT i.inventoryItemID, i.ISBN, i.price, inc.quantity, i.description, " +
	                   "b.title, b.year, b.publisher, b.isFeatured " +
	                   "FROM Includes inc " +
	                   "JOIN InventoryItems i ON inc.inventoryItemID = i.inventoryItemID " +
	                   "JOIN Books b ON i.ISBN = b.ISBN " +
	                   "WHERE inc.cartID = ?";
	    PreparedStatement stmt = connection.prepareStatement(query);
	    stmt.setInt(1, cartID);
	    ResultSet rs = stmt.executeQuery();

	    while (rs.next()) {
	        // Create a Book object
	        Book book = new Book(
	            rs.getString("ISBN"),
	            rs.getString("title"),
	            rs.getInt("year"),
	            rs.getString("publisher"),
	            rs.getBoolean("isFeatured"),
	            null,
	            null
	        );

	        // Create an InventoryItem object
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
}
