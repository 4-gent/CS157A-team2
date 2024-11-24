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

public class CartService implements CartServiceInterface{

	private CartDAO cartDAO;
	
	private CartService() {
		cartDAO = new CartDAO();
	}
	
	public static CartServiceInterface getServiceInstance() {
    	return AuthorizationProxy.createProxy(new CartService());
    }
	
	@IsAdminOrSameUser(value = "username")
	public Cart getUserCart(String username) throws SQLException {
		return cartDAO.getCartByUsername(username);
	}
	
	@SameUser(value = "username")
	public Cart addItemsToCart(String username, List<CartItem> items) throws Exception {
		return cartDAO.addItemsToCart(username, items);
	}
	
	@SameUser(value = "username")
	public Cart removeItemsFromCart(String username, List<Integer> items) throws Exception {
		return cartDAO.removeItemsFromCart(username, items);
	}
	
	@IsAdminOrSameUser(value = "username")
	public Cart updateInventoryItems(String username, List<CartItem> items) throws Exception{
		return cartDAO.updateInventoryItems(username, items);
	}
	
	/**
	 * Checks out the cart for the user, and creates an Order
	 * @param username
	 * @param addressID , the ID of the shipping address
	 * @param paymentDetailsID , the ID of the Payment Detail
	 * @return
	 * @throws Exception
	 */
	@SameUser(value = "username")
	public Order checkout(String username, int addressID, int paymentDetailsID) throws Exception {
		return cartDAO.checkout(username, addressID, paymentDetailsID); 
	}
	
}
