package com.bookie.models;

import java.util.ArrayList;
import java.util.List;

public class Cart {

	private int cartID;
	private String username;
	private double total;
	private List<InventoryItem> inventoryItems= new ArrayList<InventoryItem>();
	
	public Cart(int cartID, String username, double total) {
		super();
		this.cartID = cartID;
		this.username = username;
		this.total = total;
	}

	public List<InventoryItem> getInventoryItems() {
		return inventoryItems;
	}

	public void setInventoryItems(List<InventoryItem> inventoryItems) {
		this.inventoryItems = inventoryItems;
	}

	public int getCartID() {
		return cartID;
	}

	public String getUsername() {
		return username;
	}

	public void setCartID(int cartID) {
		this.cartID = cartID;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
}
