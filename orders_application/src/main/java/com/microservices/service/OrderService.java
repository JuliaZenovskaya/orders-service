package com.microservices.service;

import com.microservices.model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface OrderService {
    List<ItemDTO> getItemDTOS(int id) throws SQLException;
    void send(ItemDTO itemDTOS);
    void consume(StatusDTO statusDTO) throws SQLException;
    ArrayList<Order> getAllOrders() throws SQLException;
    Order getOrderById(int id) throws SQLException;
    OrderDTO changeOrderStatus(int id, OrderStatus status) throws SQLException;
    int addItemToOrder(String order_id, int item_id, int item_amount, String username) throws SQLException;
    void decreaseItemAmount(int order_id, int item_id, int item_amount) throws SQLException;
}
