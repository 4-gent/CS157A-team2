package com.bookie.bizlogic;

import java.util.List;

import com.bookie.auth.SameUser;
import com.bookie.dao.AddressDAO;
import com.bookie.models.Address;

public class AddressService {

private AddressDAO addressDAO;
	
	public AddressService() {
		addressDAO = new AddressDAO();
	}
	
	public List<Address> getUserAddresses(String username) {

		return null;  //FIXME we might have to create another relationship table between Users and Addresses
	}
	
	
	public Address addAddress(Address address) {
		
		return addressDAO.add(address);
	}
	
	public boolean changeAddress(int addressID, String street, String city, String state, String zip, String country) {
		Address a = addressDAO.getById(addressID);
		a.setStreet(street);;
		a.setCity(city);
		a.setState(state);
		a.setZip(zip);
		a.setCountry(country);
		return addressDAO.update(a);
	}
	
	public boolean removeAddress(Address address) {
		return addressDAO.delete(address.getAddressID());
	}
	
}
