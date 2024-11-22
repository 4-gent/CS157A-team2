package com.bookie.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bookie.models.Address;
import com.bookie.models.PaymentInfo;

public class PaymentInfoDAO extends BaseDAO<PaymentInfo, Integer> {

    /**
     * Retrieve a PaymentDetails entry by its paymentID.
     */
    @Override
    public PaymentInfo getById(Integer paymentID) throws SQLException {
        PaymentInfo paymentDetails = null;
        String query = "SELECT pd.paymentID, pd.username, pd.cardNumber, pd.exp, pd.cardHolderName, pd.cvv, pd.isDeleted, " +
                       "a.addressID, a.street, a.city, a.state, a.zip, a.country " +
                       "FROM PaymentDetails pd " +
                       "JOIN Addresses a ON pd.addressID = a.addressID " +
                       "WHERE pd.paymentID = ? AND pd.isDeleted = false";

        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, paymentID);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Address billingAddress = new Address(
                rs.getInt("addressID"),
                rs.getString("street"),
                rs.getString("city"),
                rs.getString("state"),
                rs.getString("zip"),
                rs.getString("country")
            );

            paymentDetails = new PaymentInfo(
                rs.getInt("paymentID"),
                rs.getString("username"),
                rs.getString("cardNumber"),
                rs.getString("exp"),
                rs.getString("cardHolderName"),
                rs.getString("cvv"),
                billingAddress,
                rs.getBoolean("isDeleted")
            );
        }
        return paymentDetails;
    }

    /**
     * Add a new PaymentDetails entry to the database.
     */
    @Override
    public PaymentInfo add(PaymentInfo paymentDetails) {
        try {
            // Step 1: Insert the billing address and get addressID
            int addressID = insertAddress(paymentDetails.getBillingAddress());

            // Step 2: Insert into PaymentDetails table
            String query = "INSERT INTO PaymentDetails (username, cardNumber, exp, cardHolderName, cvv, addressID, isDeleted) " +
                           "VALUES (?, ?, ?, ?, ?, ?, false)";
            PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, paymentDetails.getUsername());
            stmt.setString(2, paymentDetails.getCardNumber());
            stmt.setString(3, paymentDetails.getMonthYear());
            stmt.setString(4, paymentDetails.getCardHolderName());
            stmt.setString(5, paymentDetails.getCvv());
            stmt.setInt(6, addressID);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    paymentDetails.setPaymentID(generatedKeys.getInt(1));
                    paymentDetails.setDeleted(false);
                    return paymentDetails;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Update an existing PaymentDetails entry.
     */
    @Override
    public boolean update(PaymentInfo paymentDetails) {
        try {
            // Update PaymentDetails entry
            String query = "UPDATE PaymentDetails SET cardNumber = ?, exp = ?, cardHolderName = ?, cvv = ?, isDeleted = ? " +
                           "WHERE paymentID = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, paymentDetails.getCardNumber());
            stmt.setString(2, paymentDetails.getMonthYear());
            stmt.setString(3, paymentDetails.getCardHolderName());
            stmt.setString(4, paymentDetails.getCvv());
            stmt.setBoolean(5, paymentDetails.isDeleted());
            stmt.setInt(6, paymentDetails.getPaymentID());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Soft delete a PaymentDetails entry by setting isDeleted to true.
     */
    @Override
    public boolean delete(Integer paymentID) {
        try {
            String query = "UPDATE PaymentDetails SET isDeleted = true WHERE paymentID = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, paymentID);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get all payment details for a specific user.
     */
    public List<PaymentInfo> getAllPaymentDetailsForUser(String username) throws SQLException {
        List<PaymentInfo> paymentDetailsList = new ArrayList<>();
        
        String query = "SELECT pd.paymentID, pd.username, pd.cardNumber, pd.exp, pd.cardHolderName, pd.cvv, pd.isDeleted, " +
                       "a.addressID, a.street, a.city, a.state, a.zip, a.country " +
                       "FROM PaymentDetails pd " +
                       "JOIN Addresses a ON pd.addressID = a.addressID " +
                       "WHERE pd.username = ? AND pd.isDeleted = false";

        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            // Fetch the billing address
            Address billingAddress = new Address(
                rs.getInt("addressID"),
                rs.getString("street"),
                rs.getString("city"),
                rs.getString("state"),
                rs.getString("zip"),
                rs.getString("country")
            );

            // Create a PaymentDetails object
            PaymentInfo paymentDetails = new PaymentInfo(
                rs.getInt("paymentID"),
                rs.getString("username"),
                rs.getString("cardNumber"),
                rs.getString("exp"),
                rs.getString("cardHolderName"),
                rs.getString("cvv"),
                billingAddress,
                rs.getBoolean("isDeleted")
            );

            // Add to the list
            paymentDetailsList.add(paymentDetails);
        }
        
        return paymentDetailsList;
    }

    /**
     * Helper method to insert an Address into the Addresses table.
     */
    private int insertAddress(Address address) throws SQLException {
        String query = "INSERT INTO Addresses (street, city, state, zip, country) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, address.getStreet());
        stmt.setString(2, address.getCity());
        stmt.setString(3, address.getState());
        stmt.setString(4, address.getZip());
        stmt.setString(5, address.getCountry());

        stmt.executeUpdate();
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
        throw new SQLException("Failed to insert address.");
    }
 
}