package com.bookie.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bookie.models.Address;
import com.bookie.models.Author;

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
                    rs.getInt(addressID),
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
	public boolean add(Address address) {
		try {
			String query = "INSERT INTO Addresses (addressID, street, city, state, zip, country) VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, address.getAddressID());
			stmt.setString(2, address.getStreet());
			stmt.setString(3, address.getCity());
			stmt.setString(4, address.getState());
			stmt.setString(5, address.getZip());
			stmt.setString(6, address.getCountry());
			
			return stmt.executeUpdate() > 0;
			
		} catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
	
//	public List<Address> getAllAddresses() {
//        List<Address> addresses = new ArrayList<>();
//        try {
//            String query = "SELECT * FROM Addresses";
//            PreparedStatement stmt = connection.prepareStatement(query);
//            ResultSet rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                Address address = new Address(
//                    rs.getInt("addressID"),
//                    rs.getString("street"),
//                    rs.getString("city"),
//                    rs.getString("state"),
//                    rs.getString("zip"),
//                    rs.getString("country")
//                );
//                addresses.add(address);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return addresses;
//    }
	
}
