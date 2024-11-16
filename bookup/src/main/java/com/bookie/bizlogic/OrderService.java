package com.bookie.bizlogic;

import java.util.Date;
import java.util.List;

import com.bookie.dao.OrderDAO;
import com.bookie.dao.UserDAO;
import com.bookie.models.Order;

public class OrderService {

	private OrderDAO orderDAO = new OrderDAO();
	
	public Order getOrderByID(int orderID) throws Exception {
		return orderDAO.getById(orderID);
	}
	

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
	public List<Order> getAllOrdersByStatus(String status) throws Exception{ 
		if(UserDAO.isUserAnAdmin()) {
			return orderDAO.getAllOrdersByStatus(status);
		}
		else {
			throw new Exception("Admin Privillages Needed");
		}
	}
	
	public List<Order> getAllOrders(Date fromDate, Date toDate) throws Exception{ //TODO this needs admin
		return orderDAO.getAllOrders(fromDate, toDate);
	}
	
	public List<Order> getAllOrdersByKeyword(String username, String searchKeyWord) throws Exception{ 
		return orderDAO.getAllOrdersByKeyword(username, searchKeyWord);
	}
	
	/***
	 * Specific for an Admin
	 * @param searchKeyWord
	 * @return
	 * @throws Exception
	 */
	public List<Order> getAllOrdersByKeyword(String searchKeyWord) throws Exception{
		return orderDAO.getAllOrdersByKeyword(searchKeyWord);
	}
	
	public boolean updateOrderStatus(int orderID, String status) { 
		return orderDAO.updateStatus(orderID, status);
	}
	 
	 public boolean cancelOrder(int orderID) { 
		 return orderDAO.delete(orderID);
	 }
	
}
