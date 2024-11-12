package com.bookie.models;

public class InventoryItem{
	
	private int inventoryItemID;
	private Book book;
	private double price;
	private int qty;
	private String description;
	
	public InventoryItem(int inventoryItemID, Book book, double price, int qty, String description) {
		super();
		this.inventoryItemID = inventoryItemID;
		this.book = book;
		this.price = price;
		this.qty = qty;
		this.description = description;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getInventoryItemID() {
		return inventoryItemID;
	}

	@Override
	public String toString() {
		return "InventoryItem [inventoryItemID=" + inventoryItemID + ", book=" + book + ", price=" + price + ", qty="
				+ qty + ", description=" + description + "]";
	}
	

}