package com.bookie.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bookie.models.Address;

public class AddressDAO extends BaseDAO<Address, Integer>{

	@Override
	public Address getById(Integer addressID) {
		Address address = null; 
		try {
			String query = "SELECT * FROM Addresses WHERE addressID = ?";
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, addressID);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				address = new Address(
                    rs.getInt("addressID"),
                    rs.getString("street"),
                    rs.getString("city"),
                    rs.getString("state"),
                    rs.getString("zip"),
                    rs.getString("country")
                );
            }
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return address;
	}

	@Override
	public Address add(Address address) {
	    try {
	        // Prepare the INSERT statement with RETURN_GENERATED_KEYS to get the auto-generated key
	        String query = "INSERT INTO Addresses (street, city, state, zip, country) VALUES (?, ?, ?, ?, ?)";
	        PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	        
	        // Set the parameters for the query
	        stmt.setString(1, address.getStreet());
	        stmt.setString(2, address.getCity());
	        stmt.setString(3, address.getState());
	        stmt.setString(4, address.getZip());
	        stmt.setString(5, address.getCountry());
	        
	        // Execute the update
	        int rowsAffected = stmt.executeUpdate();
	        
	        // If the insertion was successful, retrieve the generated addressID
	        if (rowsAffected > 0) {
	            ResultSet generatedKeys = stmt.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                int newAddressID = generatedKeys.getInt(1);
	                // Set the new addressID to the Address object
	                address = new Address(newAddressID, address.getStreet(), address.getCity(), address.getState(), address.getZip(), address.getCountry());
	                return address;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null; // Return null if insertion fails
	}
	

	@Override
	public boolean update(Address address) {
		try {
            String query = "UPDATE Addresses SET street = ?, city = ?, state = ?, zip = ?, country = ? WHERE addressID = ?";    
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, address.getStreet());
            stmt.setString(2, address.getCity());
            stmt.setString(3, address.getState());
            stmt.setString(4, address.getZip());
            stmt.setString(5, address.getCountry());
            stmt.setInt(6, address.getAddressID());

            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
	

	@Override
	public boolean delete(Integer addressID) {
		try {
            String query = "DELETE FROM Addresses WHERE addressID = ?";    
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, addressID);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	public List<Address> getAllAddresses() {  //Only and admin can access this
        List<Address> addresses = new ArrayList<>();
        try {
            String query = "SELECT * FROM Addresses";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Address address = new Address(
                    rs.getInt("addressID"),
                    rs.getString("street"),
                    rs.getString("city"),
                    rs.getString("state"),
                    rs.getString("zip"),
                    rs.getString("country")
                );
                addresses.add(address);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addresses;
    }
	
    /**
     * Retrieves all unique shipping addresses for a given user by traversing orders.
     * @param username - The username of the user
     * @return A list of unique addresses associated with the user's orders
     * @throws SQLException
     */
    public List<Address> getUserShippingAddressesOfUser(String username) throws SQLException {
        List<Address> addresses = new ArrayList<>();

        // Constructing SQL string using `+` for appending
        String query = "SELECT DISTINCT a.addressID, a.street, a.city, a.state, a.zip, a.country "
                     + "FROM Orders o "
                     + "INNER JOIN Addresses a ON o.addressID = a.addressID "
                     + "WHERE o.username = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Address address = new Address(
                        rs.getInt("addressID"),
                        rs.getString("street"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("country"),
                        rs.getString("zip")
                    );
                    addresses.add(address);
                }
            }
        }

        return addresses;
    }
}
