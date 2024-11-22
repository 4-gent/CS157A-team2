package com.bookie.bizlogic.interfaces;

import java.util.List;

import com.bookie.models.Address;

public interface AddressServiceInterface {
	
	public Address addAddress(Address addr);
	public Address updateAddress(Address addr);
	public boolean deleteAddress(int addressId);
	public List<Address> getAllShippingAddressesOfUser(String userName);
	public List<Address> getAllAddresses();

}
