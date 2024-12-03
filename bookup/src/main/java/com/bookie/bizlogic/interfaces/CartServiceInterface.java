package com.bookie.bizlogic.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.bookie.auth.IsAdminOrSameUser;
import com.bookie.auth.SameUser;
import com.bookie.models.Cart;
import com.bookie.models.CartItem;
import com.bookie.models.Order;

public interface CartServiceInterface {

	@IsAdminOrSameUser(value = "username")
	public Cart getUserCart(String username) throws SQLException;
	
	@SameUser(value = "username")
	public Cart addItemsToCart(String username, List<CartItem> items) throws Exception;
	
	@SameUser(value = "username")
	public Cart removeItemsFromCart(String username, List<Integer> items) throws Exception;
	
	@IsAdminOrSameUser(value = "username")
	public Cart updateInventoryItems(String username, List<CartItem> items) throws Exception;
	
	@SameUser(value = "username")
	public Order checkout(String username, int addressID, int paymentDetailsID) throws Exception;

	public List<CartItem> getCartItems(int userId);

	public List<CartItem> searchCartItems(int userId, String searchInput);


	public void removeCartItem(int userId, int itemId);

	public void updateCartItemQuantity(int userId, int updateItemId, int newQuantity);

	public List<CartItem> filterCartItems(int userId, String category, String availability);

	public void removeItemsFromCart(int userId, String bookId);




	
}
