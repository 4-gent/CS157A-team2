package com.bookie.bizlogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bookie.auth.AuthorizationProxy;
import com.bookie.auth.UserContext;
import com.bookie.bizlogic.interfaces.PaymentInfoServiceInterface;
import com.bookie.bizlogic.interfaces.UserServiceInterface;
import com.bookie.dao.AddressDAO;
import com.bookie.dao.PaymentInfoDAO;
import com.bookie.dao.UserDAO;
import com.bookie.models.Address;
import com.bookie.models.PaymentInfo;

public class PaymentInfoServiceTest {

    private PaymentInfoServiceInterface paymentInfoService;
    private UserServiceInterface userService;
    private UserDAO userDAO;
    private AddressDAO addressDAO;
    private PaymentInfoDAO paymentInfoDAO;

    @BeforeEach
    public void setUp() {
        userService = AuthorizationProxy.createProxy(new UserService());
        paymentInfoService = AuthorizationProxy.createProxy(new PaymentInfoService());
        userDAO = new UserDAO();
        addressDAO = new AddressDAO();
        paymentInfoDAO = new PaymentInfoDAO();
    }

    @AfterEach
    public void cleanUp() {
        try {
            userDAO.getConnection().createStatement().execute("DELETE FROM PaymentDetails");
            userDAO.getConnection().createStatement().execute("DELETE FROM Addresses");
            userDAO.getConnection().createStatement().execute("DELETE FROM Users");
            UserContext.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddPaymentDetailsForUser_Success() throws Exception {
        // Step 1: Register a user
        userService.register("user1", "password", "user1@example.com", "1234567890", false, 0, 0);
        UserContext.setUserId("user1");

        // Step 2: Add an address
        Address address = new Address(0, "123 Main St", "Springfield", "IL", "62704", "USA");
        Address savedAddress = addressDAO.add(address);
        assertNotNull(savedAddress, "Address should be added successfully");

        // Step 3: Add payment details
        PaymentInfo paymentInfo = new PaymentInfo(
            0,
            "user1",
            "4111111111111111",
            "12/2025",
            "John Doe",
            "123",
            savedAddress,
            false
        );
        PaymentInfo savedPaymentInfo = paymentInfoService.addPaymentDetailsForUser(paymentInfo);
        assertNotNull(savedPaymentInfo, "Payment details should be added successfully");
        assertEquals("user1", savedPaymentInfo.getUsername(), "Username should match");
    }

    @Test
    public void testGetAllPaymentDetailsForUser_Success() throws Exception {
        // Step 1: Register a user
        userService.register("user1", "password", "user1@example.com", "1234567890", false, 0, 0);
        UserContext.setUserId("user1");

        // Step 2: Add an address
        Address address = new Address(0, "123 Main St", "Springfield", "IL", "62704", "USA");
        Address savedAddress = addressDAO.add(address);
        assertNotNull(savedAddress, "Address should be added successfully");

        // Step 3: Add payment details
        PaymentInfo paymentInfo = new PaymentInfo(
            0,
            "user1",
            "4111111111111111",
            "12/2025",
            "John Doe",
            "123",
            savedAddress,
            false
        );
        paymentInfoService.addPaymentDetailsForUser(paymentInfo);

        // Step 4: Fetch payment details
        List<PaymentInfo> paymentDetails = paymentInfoService.getAllPaymentDetailsForUser("user1");
        assertNotNull(paymentDetails, "Payment details list should not be null");
        assertEquals(1, paymentDetails.size(), "There should be one payment detail");
    }

    @Test
    public void testUpdatePaymentDetailsForUser_Success() throws Exception {
        // Step 1: Register a user
        userService.register("user1", "password", "user1@example.com", "1234567890", false, 0, 0);
        UserContext.setUserId("user1");

        // Step 2: Add an address
        Address address = new Address(0, "123 Main St", "Springfield", "IL", "62704", "USA");
        Address savedAddress = addressDAO.add(address);
        assertNotNull(savedAddress, "Address should be added successfully");

        // Step 3: Add payment details
        PaymentInfo paymentInfo = new PaymentInfo(
            0,
            "user1",
            "4111111111111111",
            "12/25",
            "John Doe",
            "123",
            savedAddress,
            false
        );
        PaymentInfo savedPaymentInfo = paymentInfoService.addPaymentDetailsForUser(paymentInfo);

        // Step 4: Update payment details
        savedPaymentInfo.setCardHolderName("Jane Doe");
        boolean isUpdated = paymentInfoService.updatePaymentDetailsForUser(savedPaymentInfo);
        assertTrue(isUpdated, "Payment details should be updated successfully");

        // Verify update
        PaymentInfo updatedPaymentInfo = paymentInfoDAO.getById(savedPaymentInfo.getPaymentID());
        assertEquals("Jane Doe", updatedPaymentInfo.getCardHolderName(), "Card holder name should be updated");
    }

    @Test
    public void testDeletePaymentDetailsForUser_Success() throws Exception {
        // Step 1: Register a user
        userService.register("user1", "password", "user1@example.com", "1234567890", false, 0, 0);
        UserContext.setUserId("user1");

        // Step 2: Add an address
        Address address = new Address(0, "123 Main St", "Springfield", "IL", "62704", "USA");
        Address savedAddress = addressDAO.add(address);
        assertNotNull(savedAddress, "Address should be added successfully");

        // Step 3: Add payment details
        PaymentInfo paymentInfo = new PaymentInfo(
            0,
            "user1",
            "4111111111111111",
            "12/2025",
            "John Doe",
            "123",
            savedAddress,
            false
        );
        PaymentInfo savedPaymentInfo = paymentInfoService.addPaymentDetailsForUser(paymentInfo);

        // Step 4: Delete payment details
        boolean isDeleted = paymentInfoService.deletePaymentDetailsForUser(savedPaymentInfo.getUsername(), savedPaymentInfo.getPaymentID());
        assertTrue(isDeleted, "Payment details should be deleted successfully");

        // Verify deletion
        PaymentInfo deletedPaymentInfo = paymentInfoDAO.getById(savedPaymentInfo.getPaymentID());
        assertNull(deletedPaymentInfo, "Payment details should be null after deletion");
    }
}