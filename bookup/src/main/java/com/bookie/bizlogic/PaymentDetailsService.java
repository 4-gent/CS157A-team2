package com.bookie.bizlogic;

import java.sql.SQLException;
import java.util.List;

import com.bookie.dao.PaymentDetailsDAO;
import com.bookie.models.PaymentDetails;

public class PaymentDetailsService {

	private PaymentDetailsDAO paymentDetailsDAO = new PaymentDetailsDAO();
	
	public List<PaymentDetails> getAllPaymentDetailsForUser(String username) throws SQLException {
		return paymentDetailsDAO.getAllPaymentDetailsForUser(username);
	}
	
	public PaymentDetails addPaymentDetailsForUser(PaymentDetails paymentDetails) {
		return paymentDetailsDAO.add(paymentDetails);
	}
	
	public boolean updatePaymentDetailsForUser(PaymentDetails paymentDetails) {
		return paymentDetailsDAO.update(paymentDetails);
	}
	
	public boolean deletePaymentDetailsForUser(Integer paymentID) {
		return paymentDetailsDAO.delete(paymentID);
	}
	
}
