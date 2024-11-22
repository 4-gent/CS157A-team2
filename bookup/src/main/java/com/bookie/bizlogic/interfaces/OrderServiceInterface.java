package com.bookie.bizlogic.interfaces;

import java.util.Date;
import java.util.List;

import com.bookie.auth.IsAdmin;
import com.bookie.auth.IsAdminOrSameUser;
import com.bookie.models.Order;

public interface OrderServiceInterface {

	@IsAdminOrSameUser(value = "username")
	public Order getOrderByID(int orderID, String username) throws Exception;
	
	@IsAdminOrSameUser(value = "username")
	public List<Order> getAllOrdersForUser(String username) throws Exception;
	
	@IsAdmin
	public List<Order> getAllOrdersByStatus(String status) throws Exception;
	
	@IsAdmin
	public List<Order> getAllOrders(Date fromDate, Date toDate) throws Exception;
	
	@IsAdminOrSameUser("username")
	public List<Order> getAllOrdersByKeyword(String username, String searchKeyWord) throws Exception;
	@IsAdmin
	public List<Order> getAllOrdersByKeyword(String searchKeyWord) throws Exception;
	
	@IsAdmin
	public boolean updateOrderStatus(int orderID, String status);
	 
	@IsAdminOrSameUser("username")
	public boolean cancelOrder(int orderID, String username) throws Exception;

	
}
