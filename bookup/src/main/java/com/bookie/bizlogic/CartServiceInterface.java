package com.bookie.bizlogic;

import java.sql.SQLException;
import java.util.List;

import com.bookie.auth.IsAdminOrSameUser;
import com.bookie.auth.SameUser;
import com.bookie.models.Cart;
import com.bookie.models.InventoryItem;
import com.bookie.models.Order;

public interface CartServiceInterface {

	@IsAdminOrSameUser(value = "username")
	public Cart getUserCart(String username) throws SQLException;
	
	@SameUser(value = "username")
	public Cart addItemsToCart(String username, List<InventoryItem> items) throws Exception;
	
	@SameUser(value = "username")
	public Cart removeItemsFromCart(String username, List<Integer> items) throws Exception;
	
	@IsAdminOrSameUser(value = "username")
	public Cart updateInventoryItems(String username, List<InventoryItem> items) throws Exception;
	
	@SameUser(value = "username")
	public Order checkout(String username, int addressID) throws Exception;

	
}
