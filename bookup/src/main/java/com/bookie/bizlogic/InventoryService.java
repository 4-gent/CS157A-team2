package com.bookie.bizlogic;

import java.util.List;

import com.bookie.auth.AuthorizationProxy;
import com.bookie.auth.IsAdmin;
import com.bookie.bizlogic.interfaces.InventoryServiceInterface;
import com.bookie.dao.InventoryDAO;
import com.bookie.models.InventoryItem;

public class InventoryService implements InventoryServiceInterface {

	private InventoryDAO inventoryDAO;
	
	private InventoryService() {
		inventoryDAO = new InventoryDAO();
	}
	
	public static InventoryServiceInterface getServiceInstance() {
    	return AuthorizationProxy.createProxy(new InventoryService());
    }
	
	@IsAdmin
	public InventoryItem addInventoryItem(InventoryItem item) {
		return inventoryDAO.add(item);
	}
	
	public InventoryItem getByInventoryItemID(int id) {
		return inventoryDAO.getById(id);
	}
	
	@IsAdmin
	public boolean updateInventoryItem(InventoryItem t) {
		return inventoryDAO.update(t);
	}
	
	@IsAdmin
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
