package com.bookie.bizlogic;

import java.util.List;

import com.bookie.dao.InventoryDAO;
import com.bookie.models.InventoryItem;

public class InventoryService {

	private InventoryDAO inventoryDAO = new InventoryDAO();
	
	public InventoryItem addInventoryItem(InventoryItem item) {
		return inventoryDAO.add(item);
	}
	
	public InventoryItem getByInventoryItemID(int id) {
		return inventoryDAO.getById(id);
	}
	
	public boolean updateInventoryItem(InventoryItem t) {
		return inventoryDAO.update(t);
	}
	
	public boolean removeInventoryItem(Integer id) {
		return inventoryDAO.delete(id);
	}
	
	public InventoryItem searchByISBN(String ISBN) {
		return inventoryDAO.searchByISBN(ISBN);
	}
	
	public List<InventoryItem> searchInventoryItemsByKeyword(String keyword){
		return inventoryDAO.searchInventoryItemsByKeyword(keyword);
	}
}