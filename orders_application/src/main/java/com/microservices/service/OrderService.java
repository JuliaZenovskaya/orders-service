package com.microservices.service;

import com.microservices.model.AddItem;
import com.microservices.model.Order;
import com.microservices.model.OrderDTO;
import com.microservices.model.OrderStatus;
import jdk.internal.jline.internal.Nullable;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OrderService {
    ArrayList<Order> getAllOrders() throws SQLException;
    Order getOrderById(int id) throws SQLException;
    OrderDTO changeOrderStatus(int id, OrderStatus status) throws SQLException;
    int addItemToOrder(@Nullable Integer order_id, int item_id, int item_amount, String username) throws SQLException;
    void decreaseItemAmount(int order_id, int item_id, int item_amount) throws SQLException;
}
