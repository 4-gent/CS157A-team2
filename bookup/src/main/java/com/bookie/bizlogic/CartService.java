package com.bookie.bizlogic;

import java.sql.SQLException;
import java.util.List;

import com.bookie.auth.AuthorizationProxy;
import com.bookie.auth.IsAdminOrSameUser;
import com.bookie.auth.SameUser;
import com.bookie.bizlogic.interfaces.CartServiceInterface;
import com.bookie.dao.CartDAO;
import com.bookie.models.Cart;
import com.bookie.models.CartItem;
import com.bookie.models.Order;

public class CartService implements CartServiceInterface {

    private static CartService instance; // Singleton instance

    private final CartDAO cartDAO;

    // Private constructor to enforce Singleton pattern
    private CartService() {
        this.cartDAO = new CartDAO();
    }

    // Thread-safe Singleton implementation with lazy initialization
    public static CartService getInstance() {
        if (instance == null) {
            synchronized (CartService.class) {
                if (instance == null) {
                    instance = new CartService();
                }
            }
        }
        return instance;
    }

    // Proxy implementation for authorization
    public static CartServiceInterface getServiceInstance() {
        return AuthorizationProxy.createProxy(getInstance());
    }

    /**
     * Retrieves the user's cart based on their username.
     * 
     * @param username The username of the user.
     * @return The user's cart.
     * @throws SQLException If a database error occurs.
     */
    @IsAdminOrSameUser(value = "username")
    @Override
    public Cart getUserCart(String username) throws SQLException {
        return cartDAO.getCartByUsername(username);
    }

    /**
     * Adds items to the user's cart.
     * 
     * @param username The username of the user.
     * @param items    List of items to add to the cart.
     * @return The updated cart.
     * @throws Exception If an error occurs during the operation.
     */
    @SameUser(value = "username")
    @Override
    public Cart addItemsToCart(String username, List<CartItem> items) throws Exception {
        return cartDAO.addItemsToCart(username, items);
    }

    /**
     * Removes items from the user's cart.
     * 
     * @param username The username of the user.
     * @param items    List of item IDs to remove from the cart.
     * @return The updated cart.
     * @throws Exception If an error occurs during the operation.
     */
    @SameUser(value = "username")
    @Override
    public Cart removeItemsFromCart(String username, List<Integer> items) throws Exception {
        return cartDAO.removeItemsFromCart(username, items);
    }

    /**
     * Updates inventory items for the user's cart.
     * 
     * @param username The username of the user.
     * @param items    List of items to update in the cart.
     * @return The updated cart.
     * @throws Exception If an error occurs during the operation.
     */
    @IsAdminOrSameUser(value = "username")
    @Override
    public Cart updateInventoryItems(String username, List<CartItem> items) throws Exception {
        return cartDAO.updateInventoryItems(username, items);
    }

    /**
     * Checks out the cart for the user and creates an order.
     * 
     * @param username        The username of the user.
     * @param addressID       The ID of the shipping address.
     * @param paymentDetailsID The ID of the payment detail.
     * @return The created order.
     * @throws Exception If an error occurs during the operation.
     */
    @SameUser(value = "username")
    @Override
    public Order checkout(String username, int addressID, int paymentDetailsID) throws Exception {
        return cartDAO.checkout(username, addressID, paymentDetailsID);
    }

	@Override
	public List<CartItem> getCartItems(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CartItem> searchCartItems(int userId, String searchInput) {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	//public void addBookToCart(int userId, String isbn, int quantity) {
		// TODO Auto-generated method stub
		
		
		
	//}

	@Override
	public void removeCartItem(int userId, int itemId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateCartItemQuantity(int userId, int updateItemId, int newQuantity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<CartItem> filterCartItems(int userId, String category, String availability) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeItemsFromCart(int userId, String bookId) {
		// TODO Auto-generated method stub
		
	}
}
