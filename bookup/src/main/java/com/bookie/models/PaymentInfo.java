package com.bookie.models;

public class PaymentInfo {
	
	private int paymentID;
	private String username;
	private String cardNumber;
	private String monthYear;
	private String cardHolderName;
	private String cvv;
	private Address billingAddress;
	private boolean isDeleted;


	public PaymentInfo(int paymentID, String username, String cardNumber, String monthYear, String cardHolderName,
			String cvv, Address billingAddress, boolean isDeleted) {
		super();
		this.paymentID = paymentID;
		this.username = username;
		this.cardNumber = cardNumber;
		this.monthYear = monthYear;
		this.cardHolderName = cardHolderName;
		this.cvv = cvv;
		this.billingAddress = billingAddress;
		this.isDeleted = isDeleted;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getCardNumber() {
		return cardNumber;
	}


	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}


	public String getMonthYear() {
		return monthYear;
	}


	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}


	public String getCardHolderName() {
		return cardHolderName;
	}


	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}


	public String getCvv() {
		return cvv;
	}


	public void setCvv(String cvv) {
		this.cvv = cvv;
	}


	public Address getBillingAddress() {
		return billingAddress;
	}


	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}


	public boolean isDeleted() {
		return isDeleted;
	}


	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getPaymentID() {
		return paymentID;
	}


	public void setPaymentID(int paymentID) {
		this.paymentID = paymentID;
	}


	@Override
	public String toString() {
		return "PaymentDetails [paymentID=" + paymentID + ", username=" + username + ", cardNumber=" + cardNumber
				+ ", monthYear=" + monthYear + ", cardHolderName=" + cardHolderName + ", cvv=" + cvv
				+ ", billingAddress=" + billingAddress + ", isDeleted=" + isDeleted + "]";
	}


	
	
	
	
}
