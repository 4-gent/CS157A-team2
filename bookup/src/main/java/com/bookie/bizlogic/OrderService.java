package com.bookie.bizlogic;

import java.util.Date;
import java.util.List;

import com.bookie.auth.IsAdmin;
import com.bookie.auth.IsAdminOrSameUser;
import com.bookie.dao.OrderDAO;
import com.bookie.dao.UserDAO;
import com.bookie.models.Order;

public class OrderService implements OrderServiceInterface{

	private OrderDAO orderDAO = new OrderDAO();
	
	@IsAdminOrSameUser(value = "username")
	public Order getOrderByID(int orderID, String username) throws Exception {
		return orderDAO.getById(orderID);
	}
	
	@IsAdminOrSameUser(value = "username")
	public List<Order> getAllOrdersForUser(String username) throws Exception{ 
		if(UserDAO.isUserAnAdmin() || UserDAO.isSameUser(username)) {
			return orderDAO.getOrdersByUsername(username); 
		}
		throw new Exception("Not Authorized");
	}
	
	/***
	 * Needs admin credentials
	 * @param status
	 * @return
	 * @throws Exception
	 */
	
	@IsAdmin
	public List<Order> getAllOrdersByStatus(String status) throws Exception{ 
		return orderDAO.getAllOrdersByStatus(status);
	}
	
	@IsAdmin
	public List<Order> getAllOrders(Date fromDate, Date toDate) throws Exception{ 
		return orderDAO.getAllOrders(fromDate, toDate);
	}
	
	@IsAdminOrSameUser("username")
	public List<Order> getAllOrdersByKeyword(String username, String searchKeyWord) throws Exception{ 
		return orderDAO.getAllOrdersByKeyword(username, searchKeyWord);
	}
	
	/***
	 * Specific for an Admin
	 * @param searchKeyWord
	 * @return
	 * @throws Exception
	 */
	@IsAdmin
	public List<Order> getAllOrdersByKeyword(String searchKeyWord) throws Exception{
		return orderDAO.getAllOrdersByKeyword(searchKeyWord);
	}
	
	@IsAdmin
	public boolean updateOrderStatus(int orderID, String status) { 
		return orderDAO.updateStatus(orderID, status);
	}
	 
	@IsAdminOrSameUser("username")
	public boolean cancelOrder(int orderID, String username) throws Exception { 
		return orderDAO.delete(orderID);
	}
	
}
