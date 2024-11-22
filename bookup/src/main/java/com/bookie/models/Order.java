package com.bookie.models;

import java.util.Date;
import java.util.List;

public class Order {

    private int orderID;
    private String username;
    private double total;
    private Address shippingAddress;
    private Date orderDate;
    private String orderStatus;
    private List<CartItem> items;

    public Order(int orderID, String username, double total, Address shippingAddress, Date orderDate,
                 String orderStatus, List<CartItem> items) {
        this.orderID = orderID;
        this.username = username;
        this.total = total;
        this.shippingAddress = shippingAddress;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.items = items;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Order [orderID=" + orderID + ", username=" + username + ", total=" + total + ", shippingAddress="
                + shippingAddress + ", orderDate=" + orderDate + ", orderStatus=" + orderStatus + ", items=" + items
                + "]";
    }
}