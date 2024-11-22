package com.bookie.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bookie.models.Address;

public class AddressDAOTest {

    private AddressDAO addressDAO;

    @BeforeEach
    public void setUp() {
        addressDAO = new AddressDAO();
    }

    @AfterEach
    public void cleanUp() {
        try (Statement stmt = addressDAO.getConnection().createStatement()) {
            stmt.execute("DELETE FROM Addresses");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to clean up the database: " + e.getMessage());
        }
    }

    @Test
    public void testAddAddress_Success() {
        Address address = new Address(0, "123 Main St", "Cityville", "Stateville", "12345", "Country");
        Address savedAddress = addressDAO.add(address);
        assertNotNull(savedAddress, "Address should be added successfully");
        assertTrue(savedAddress.getAddressID() > 0, "Address ID should be greater than 0");
        assertEquals("123 Main St", savedAddress.getStreet(), "Street should match");
    }

    @Test
    public void testGetById_Success() {
        Address address = new Address(0, "456 Elm St", "Townsville", "Region", "67890", "Country");
        Address savedAddress = addressDAO.add(address);
        assertNotNull(savedAddress, "Address should be added successfully");

        Address fetchedAddress = addressDAO.getById(savedAddress.getAddressID());
        assertNotNull(fetchedAddress, "Fetched address should not be null");
        assertEquals(savedAddress.getAddressID(), fetchedAddress.getAddressID(), "Address ID should match");
        assertEquals("456 Elm St", fetchedAddress.getStreet(), "Street should match");
    }

    @Test
    public void testUpdateAddress_Success() {
        Address address = new Address(0, "789 Oak St", "Metro City", "Province", "11111", "Country");
        Address savedAddress = addressDAO.add(address);
        assertNotNull(savedAddress, "Address should be added successfully");

        savedAddress.setStreet("789 Updated Oak St");
        savedAddress.setCity("New Metro City");
        boolean isUpdated = addressDAO.update(savedAddress);
        assertTrue(isUpdated, "Address should be updated successfully");

        Address updatedAddress = addressDAO.getById(savedAddress.getAddressID());
        assertNotNull(updatedAddress, "Updated address should not be null");
        assertEquals("789 Updated Oak St", updatedAddress.getStreet(), "Updated street should match");
        assertEquals("New Metro City", updatedAddress.getCity(), "Updated city should match");
    }

    @Test
    public void testDeleteAddress_Success() {
        Address address = new Address(0, "101 Pine St", "Village", "District", "22222", "Country");
        Address savedAddress = addressDAO.add(address);
        assertNotNull(savedAddress, "Address should be added successfully");

        boolean isDeleted = addressDAO.delete(savedAddress.getAddressID());
        assertTrue(isDeleted, "Address should be deleted successfully");

        Address deletedAddress = addressDAO.getById(savedAddress.getAddressID());
        assertNull(deletedAddress, "Deleted address should not exist");
    }

    @Test
    public void testGetAllAddresses_Success() {
        Address address1 = new Address(0, "123 Main St", "Cityville", "Stateville", "12345", "Country");
        Address address2 = new Address(0, "456 Elm St", "Townsville", "Region", "67890", "Country");
        addressDAO.add(address1);
        addressDAO.add(address2);

        List<Address> addresses = addressDAO.getAllAddresses();
        assertNotNull(addresses, "Addresses list should not be null");
        assertEquals(2, addresses.size(), "There should be two addresses in the list");
    }
}