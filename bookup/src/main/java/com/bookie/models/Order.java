package com.bookie.models;

import java.util.Date;
import java.util.List;

public class Order {
	
	private int orderID;
	private String username;
	private double total;
	private Address shipppingAddress;
	private Date orderDate;
	private String orderStatus;
	private List<InventoryItem> items;
	
	public Order(int orderID, String username, double total, Address shipppingAddress, Date orderDate,
			String orderStatus, List<InventoryItem> items) {
		super();
		this.orderID = orderID;
		this.username = username;
		this.total = total;
		this.shipppingAddress = shipppingAddress;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.items = items;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public Address getShipppingAddress() {
		return shipppingAddress;
	}

	public void setShipppingAddress(Address shipppingAddress) {
		this.shipppingAddress = shipppingAddress;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public List<InventoryItem> getItems() {
		return items;
	}

	public void setItems(List<InventoryItem> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "Order [orderID=" + orderID + ", username=" + username + ", total=" + total + ", shipppingAddress="
				+ shipppingAddress + ", orderDate=" + orderDate + ", orderStatus=" + orderStatus + ", items=" + items
				+ "]";
	}
	
}
