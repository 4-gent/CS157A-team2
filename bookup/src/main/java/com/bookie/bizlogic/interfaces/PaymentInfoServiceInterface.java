package com.bookie.bizlogic.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.bookie.auth.IsAdminOrSameUser;
import com.bookie.models.PaymentInfo;

public interface PaymentInfoServiceInterface {
	
	@IsAdminOrSameUser(value = "username")
	public List<PaymentInfo> getAllPaymentDetailsForUser(String username) throws SQLException;
	
	@IsAdminOrSameUser(value = "paymentDetails.username")
	public PaymentInfo addPaymentDetailsForUser(PaymentInfo paymentDetails);
	
	@IsAdminOrSameUser(value = "paymentDetails.username")
	public boolean updatePaymentDetailsForUser(PaymentInfo paymentDetails);
	
	@IsAdminOrSameUser(value = "username")
	public boolean deletePaymentDetailsForUser(String username, Integer paymentID);

	@IsAdminOrSameUser(value = "username")
    public PaymentInfo getPaymentInfoById(Integer paymentID) throws SQLException;
}
