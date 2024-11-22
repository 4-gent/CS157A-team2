package com.bookie.models;

public class CartItem {

	private InventoryItem inventoryItem;
	private int quantity;
	
	
	public CartItem(InventoryItem inventoryItem, int quantity) {
		super();
		this.inventoryItem = inventoryItem;
		this.quantity = quantity;
	}


	public InventoryItem getInventoryItem() {
		return inventoryItem;
	}


	public void setInventoryItem(InventoryItem inventoryItem) {
		this.inventoryItem = inventoryItem;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
}
