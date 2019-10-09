package com.microservices.service;

import com.microservices.model.AddItem;
import com.microservices.model.Order;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OrderService {
    ArrayList<Order> getAllOrders() throws SQLException;
    Order getOrderById(int id) throws SQLException;
    void changeOrderStatus(int id, String status) throws SQLException;
    void addItemToOrder(AddItem addItem) throws SQLException;
    void addAddressToOrder(int orderId, String email, String country, String city, String street, int house, int corp, int flat) throws SQLException;
    void decreaseItemAmount(int id) throws SQLException;
}
