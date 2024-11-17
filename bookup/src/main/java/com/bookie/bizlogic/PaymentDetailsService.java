package com.bookie.bizlogic;

import java.sql.SQLException;
import java.util.List;

import com.bookie.auth.IsAdminOrSameUser;
import com.bookie.dao.PaymentDetailsDAO;
import com.bookie.models.PaymentDetails;

public class PaymentDetailsService {

	private PaymentDetailsDAO paymentDetailsDAO = new PaymentDetailsDAO();
	
	@IsAdminOrSameUser(value = "username")
	public List<PaymentDetails> getAllPaymentDetailsForUser(String username) throws SQLException {
		return paymentDetailsDAO.getAllPaymentDetailsForUser(username);
	}
	
	@IsAdminOrSameUser(value = "paymentDetails.username")
	public PaymentDetails addPaymentDetailsForUser(PaymentDetails paymentDetails) {
		return paymentDetailsDAO.add(paymentDetails);
	}
	
	@IsAdminOrSameUser(value = "paymentDetails.username")
	public boolean updatePaymentDetailsForUser(PaymentDetails paymentDetails) {
		return paymentDetailsDAO.update(paymentDetails);
	}
	
	@IsAdminOrSameUser(value = "paymentDetails.username")
	public boolean deletePaymentDetailsForUser(Integer paymentID) {
		return paymentDetailsDAO.delete(paymentID);
	}
	
}
