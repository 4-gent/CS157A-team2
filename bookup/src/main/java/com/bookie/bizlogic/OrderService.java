package com.bookie.bizlogic;

import java.util.Date;
import java.util.List;

import com.bookie.dao.OrderDAO;
import com.bookie.models.Order;

public class OrderService {

	private OrderDAO orderDAO = new OrderDAO();
	
	public Order getOrderByID(int orderID) throws Exception {
		return orderDAO.getById(orderID);
	}
	
	public List<Order> getAllOrdersForUser(String username){ //TODO
		return null; 
	}
	
	public List<Order> getAllOrdersByStatus(String status){ //TODO this needs admin privilages
		return null;
	}
	
	public List<Order> getAllOrders(Date fromDate, Date toDate){ //TODO this needs admin
		return null;
	}
	
	public List<Order> getAllOrdersByKeyword(String username, String searchKeyWord){ //TODO this can be search in past orders by book name, author name, publisher
		return null;
	}
	
	public boolean update(Order order) { //TODO
		return false;
	}
	
	 public boolean delete(Integer orderID) { //TODO
		 return false;
	 }
	 
	 public boolean cancelOrder(int orderID) { //TODO
		 return false;
	 }
	
}
