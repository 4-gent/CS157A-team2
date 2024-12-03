package com.bookie.bizlogic;

import java.sql.SQLException;
import java.util.List;

import com.bookie.auth.AuthorizationProxy;
import com.bookie.auth.IsAdminOrSameUser;
import com.bookie.bizlogic.interfaces.PaymentInfoServiceInterface;
import com.bookie.dao.PaymentInfoDAO;
import com.bookie.models.PaymentInfo;

public class PaymentInfoService implements PaymentInfoServiceInterface {

    private PaymentInfoDAO paymentDetailsDAO;

    private PaymentInfoService() {
        this.paymentDetailsDAO = new PaymentInfoDAO();
    }
    
    public static PaymentInfoServiceInterface getServiceInstance() {
    	return AuthorizationProxy.createProxy(new PaymentInfoService());
    }

    /**
     * Retrieves all payment details for a specific user.
     * Only the user or an admin can access this method.
     * 
     * @param username - The username of the user
     * @return A list of payment details for the user
     * @throws SQLException
     */
    @Override
    @IsAdminOrSameUser(value = "username")
    public List<PaymentInfo> getAllPaymentDetailsForUser(String username) throws SQLException {
        return paymentDetailsDAO.getAllPaymentDetailsForUser(username);
    }

    /**
     * Adds a new payment detail for the user.
     * Only the user or an admin can access this method.
     * 
     * @param paymentDetails - The payment details to add
     * @return The added payment details
     */
    @Override
    @IsAdminOrSameUser(value = "paymentDetails.username")
    public PaymentInfo addPaymentDetailsForUser(PaymentInfo paymentDetails) {
        return paymentDetailsDAO.add(paymentDetails);
    }

    /**
     * Updates a payment detail for the user.
     * Only the user or an admin can access this method.
     * 
     * @param paymentDetails - The payment details to update
     * @return true if the update was successful, false otherwise
     */
    @Override
    @IsAdminOrSameUser(value = "paymentDetails.username")
    public boolean updatePaymentDetailsForUser(PaymentInfo paymentDetails) {
        return paymentDetailsDAO.update(paymentDetails);
    }

    /**
     * Deletes a payment detail for the user.
     * Only the user or an admin can access this method.
     * 
     * @param paymentID - The ID of the payment detail to delete
     * @return true if the deletion was successful, false otherwise
     */
    @Override
    @IsAdminOrSameUser(value = "username")
    public boolean deletePaymentDetailsForUser(String username, Integer paymentID) {
        return paymentDetailsDAO.delete(paymentID);
    }

    @Override
    public PaymentInfo getPaymentInfoById(Integer paymentID) throws SQLException {
        return paymentDetailsDAO.getPaymentInfoById(paymentID);
    }
}