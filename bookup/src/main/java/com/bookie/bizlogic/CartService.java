package com.bookie.bizlogic;

import java.sql.SQLException;
import java.util.List;

import com.bookie.dao.CartDAO;
import com.bookie.models.Cart;
import com.bookie.models.InventoryItem;
import com.bookie.models.Order;

public class CartService {

	private CartDAO cartDAO = new CartDAO();
	
	public Cart getUserCart(String username) throws SQLException {
		return cartDAO.getCartByUsername(username);
	}
	
	public Cart addItemsToCart(String username, List<InventoryItem> items) throws Exception {
		return cartDAO.addItemsToCart(username, items);
	}
	
	public Cart removeItemsFromCart(String username, List<Integer> items) throws Exception {
		return cartDAO.removeItemsFromCart(username, items);
	}
	
	public Cart updateInventoryItems(String username, List<InventoryItem> items) throws Exception{
		return cartDAO.updateInventoryItems(username, items);
	}
	
	/**
	 * Checks out the cart for the user, and creates an Order
	 * @param username
	 * @param addressID , the ID of the shipping address
	 * @return
	 * @throws Exception
	 */
	public Order checkout(String username, int addressID) throws Exception {
		return cartDAO.checkout(username, addressID); 
	}
	
	
}
