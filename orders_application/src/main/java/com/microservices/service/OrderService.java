package com.microservices.service;

import com.microservices.model.Order;
import com.microservices.model.OrderDTO;
import com.microservices.model.OrderStatus;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OrderService {
    ArrayList<Order> getAllOrders() throws SQLException;
    Order getOrderById(int id) throws SQLException;
    OrderDTO changeOrderStatus(int id, OrderStatus status) throws SQLException;
    int addItemToOrder(String order_id, int item_id, int item_amount, String username) throws SQLException;
    void decreaseItemAmount(int order_id, int item_id, int item_amount) throws SQLException;
}
