package com.bookie.models;

import java.util.ArrayList;
import java.util.List;

public class Cart {

	private int cartID;
	private String username;
	private double total;
	private List<CartItem> cartItems= new ArrayList<CartItem>();
	
	public Cart(int cartID, String username, double total) {
		super();
		this.cartID = cartID;
		this.username = username;
		this.total = total;
	}

	public List<CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
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
