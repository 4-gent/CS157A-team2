package com.bookie.bizlogic.interfaces;

import java.util.List;

import com.bookie.auth.IsAdmin;
import com.bookie.models.InventoryItem;

public interface InventoryServiceInterface {

	@IsAdmin
	public InventoryItem addInventoryItem(InventoryItem item);
	
	public InventoryItem getByInventoryItemID(int id);
	
	@IsAdmin
	public boolean updateInventoryItem(InventoryItem t);
	
	@IsAdmin
	public boolean removeInventoryItem(Integer id);
	
	public InventoryItem searchByISBN(String ISBN);
	
	public List<InventoryItem> searchInventoryItemsByKeyword(String keyword);

	public List<InventoryItem> getAllInventoryItems();

	
}
