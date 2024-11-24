package com.bookie.bizlogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bookie.auth.UserContext;
import com.bookie.bizlogic.interfaces.AddressServiceInterface;
import com.bookie.bizlogic.interfaces.BookServiceInterface;
import com.bookie.bizlogic.interfaces.CartServiceInterface;
import com.bookie.bizlogic.interfaces.InventoryServiceInterface;
import com.bookie.bizlogic.interfaces.PaymentInfoServiceInterface;
import com.bookie.bizlogic.interfaces.UserServiceInterface;
import com.bookie.dao.AddressDAO;
import com.bookie.models.Address;
import com.bookie.models.Book;
import com.bookie.models.CartItem;
import com.bookie.models.InventoryItem;
import com.bookie.models.PaymentInfo;

public class AddressServiceTest {

	private AddressServiceInterface addressService;
	private UserServiceInterface userService;
	private CartServiceInterface cartService;
	private PaymentInfoServiceInterface paymentInfoService;
	private BookServiceInterface bookService;
	private InventoryServiceInterface inventoryService;
	private Connection connection;

	@BeforeEach
	public void setUp() {
	    addressService = AddressService.getServiceInstance();
	    userService = UserService.getServiceInstance();
	    cartService = CartService.getServiceInstance();
	    paymentInfoService = PaymentInfoService.getServiceInstance();
	    bookService = BookService.getServiceInstance();
	    inventoryService = InventoryService.getServiceInstance();

	    connection = new AddressDAO().getConnection(); // Obtain connection for cleanup purposes
	}

	@AfterEach
	public void tearDown() {
	    try {
	        // Delete from dependent tables first due to foreign key constraints
	        connection.createStatement().execute("DELETE FROM Includes"); // Cart items
	        connection.createStatement().execute("DELETE FROM Cart"); // Carts
	        connection.createStatement().execute("DELETE FROM Contains"); // Order items
	        connection.createStatement().execute("DELETE FROM Orders"); // Orders
	        connection.createStatement().execute("DELETE FROM InventoryItems"); // Inventory items
	        connection.createStatement().execute("DELETE FROM Books"); // Books
	        connection.createStatement().execute("DELETE FROM PaymentDetails"); // Payment info
	        connection.createStatement().execute("DELETE FROM Addresses"); // Addresses
	        connection.createStatement().execute("DELETE FROM Users"); // Users
	        
	        // Clear user context after database cleanup
	        UserContext.clear();
	    } catch (SQLException e) {
	        fail("Failed to clean up the database: " + e.getMessage());
	    }
	}

    @Test
    public void testAddAddress_Success() {
        try {
            Address address = new Address(0, "123 Street", "City", "State", "12345", "Country");
            Address addedAddress = addressService.addAddress(address);

            assertNotNull(addedAddress, "Address should be added successfully");
            assertNotEquals(0, addedAddress.getAddressID(), "Address ID should be generated");
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetAllAddresses_Success() {
        try {
            // Register an admin user
            userService.register("adminUser", "adminPass", "admin@example.com", "1234567890", true, 0, 0);
            UserContext.setUserId("adminUser");

            // Add addresses
            addressService.addAddress(new Address(0, "123 Street", "City", "State", "12345", "Country"));
            addressService.addAddress(new Address(0, "456 Avenue", "Town", "Province", "67890", "Region"));

            // Fetch all addresses
            List<Address> addresses = addressService.getAllAddresses();

            assertNotNull(addresses, "Address list should not be null");
            assertEquals(2, addresses.size(), "There should be two addresses in the list");
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetAllShippingAddressesOfUser_Success() {
        try {
            // Step 1: Register an admin user and set context
            userService.register("adminUser", "adminPass", "admin@example.com", "1234567890", true, 0, 0);
            UserContext.setUserId("adminUser");

            // Step 2: Add books and inventory items as admin
            Book book1 = new Book("1234567890123", "Book 1", 2023, "Publisher 1", false, null, null);
            Book book2 = new Book("9876543210987", "Book 2", 2022, "Publisher 2", false, null, null);
            bookService.addBook(book1);
            bookService.addBook(book2);

            InventoryItem inventoryItem1 = inventoryService.addInventoryItem(
                new InventoryItem(0, book1, 10.0, 100, "Sample Inventory Item 1")
            );
            InventoryItem inventoryItem2 = inventoryService.addInventoryItem(
                new InventoryItem(0, book2, 20.0, 50, "Sample Inventory Item 2")
            );

            // Step 3: Register a regular user and set context
            userService.register("user1", "password", "user1@example.com", "0987654321", false, 0, 0);
            UserContext.setUserId("user1");

            // Step 4: Add addresses using AddressService
            Address address1 = addressService.addAddress(new Address(0, "123 Street", "City", "State", "12345", "Country"));
            Address address2 = addressService.addAddress(new Address(0, "456 Avenue", "Town", "Province", "67890", "Region"));

            // Step 5: Add payment details using PaymentInfoService
            PaymentInfo payment1 = paymentInfoService.addPaymentDetailsForUser(
                new PaymentInfo(0, "user1", "1111222233334444", "12/25", "User1", "123", address1, false)
            );
            PaymentInfo payment2 = paymentInfoService.addPaymentDetailsForUser(
                new PaymentInfo(0, "user1", "5555666677778888", "11/26", "User1", "456", address2, false)
            );

            // Step 6: Add items to the cart
            CartItem cartItem1 = new CartItem(inventoryItem1, 1);
            cartService.addItemsToCart("user1", List.of(cartItem1));

            // Step 7: Checkout cart and create orders using CartService
            cartService.checkout("user1", address1.getAddressID(), payment1.getPaymentID());

            CartItem cartItem2 = new CartItem(inventoryItem2, 2);
            cartService.addItemsToCart("user1", List.of(cartItem2));
            cartService.checkout("user1", address2.getAddressID(), payment2.getPaymentID());

            // Step 8: Fetch all shipping addresses for the user using AddressService
            List<Address> addresses = addressService.getAllShippingAddressesOfUser("user1");

            // Step 9: Assertions
            assertNotNull(addresses, "Shipping addresses should not be null");
            assertEquals(2, addresses.size(), "There should be two unique shipping addresses");

        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testDeleteAddress_Success() {
        try {
            // Add address using AddressService
            Address address = addressService.addAddress(new Address(0, "123 Street", "City", "State", "12345", "Country"));

            // Delete address
            boolean isDeleted = addressService.deleteAddress(address.getAddressID());

            assertTrue(isDeleted, "Address should be deleted successfully");
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}