package com.bookie.bizlogic;

import java.sql.SQLException;
import java.util.List;

import com.bookie.auth.AuthorizationProxy;
import com.bookie.auth.IsAdmin;
import com.bookie.auth.IsAdminOrSameUser;
import com.bookie.bizlogic.interfaces.AddressServiceInterface;
import com.bookie.dao.AddressDAO;
import com.bookie.models.Address;

public class AddressService implements AddressServiceInterface {

    private AddressDAO addressDAO;

    private AddressService() {
        this.addressDAO = new AddressDAO();
    }
    
    public static AddressServiceInterface getServiceInstance() {
    	return AuthorizationProxy.createProxy(new AddressService());
    }


    /**
     * Adds a new address.
     * @param addr - The address to add.
     * @return The added address.
     */
    @Override
    public Address addAddress(Address addr) {
        return addressDAO.add(addr);
    }

    /**
     * Updates an existing address.
     * @param addr - The address to update.
     * @return The updated address.
     */
    @Override
    public Address updateAddress(Address addr) {
        if (addressDAO.update(addr)) {
            return addr;
        }
        return null;
    }

    /**
     * Deletes an address by ID.
     * @param addressId - The ID of the address to delete.
     * @return true if the address was deleted successfully; false otherwise.
     */
    @Override
    public boolean deleteAddress(int addressId) {
        return addressDAO.delete(addressId);
    }

    /**
     * Retrieves all shipping addresses for a specific user by traversing orders.
     * Only the user or an admin can access this method.
     * @param username - The username of the user.
     * @return A list of unique addresses associated with the user.
     */
    @Override
    @IsAdminOrSameUser("userName")
    public List<Address> getAllShippingAddressesOfUser(String userName) {
        try {
            return addressDAO.getUserShippingAddressesOfUser(userName);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves all addresses in the system.
     * Only an admin can access this method.
     * @return A list of all addresses.
     */
    @Override
    @IsAdmin
    public List<Address> getAllAddresses() {
        return addressDAO.getAllAddresses();
    }
}