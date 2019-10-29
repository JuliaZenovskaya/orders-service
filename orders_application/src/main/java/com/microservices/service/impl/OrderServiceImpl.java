package com.microservices.service.impl;

import com.microservices.model.Order;
import com.microservices.database.DBHelper;
import com.microservices.model.OrderDTO;
import com.microservices.model.OrderStatus;
import com.microservices.service.OrderService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class OrderServiceImpl implements OrderService {
    private DBHelper dbHelper = new DBHelper();

    @Override
    public ArrayList<Order> getAllOrders() throws SQLException {
        return dbHelper.getAllOrders();
    }

    @Override
    public Order getOrderById(int id) throws SQLException {
        return dbHelper.getOrderById(id);
    }

    @Override
    public OrderDTO changeOrderStatus(int id, OrderStatus status) throws SQLException {
        return dbHelper.changeOrderStatus(id, status);
    }

    @Override
    public int addItemToOrder(String order_id, int item_id, int item_amount, String username) throws SQLException {
        return dbHelper.addItemToOrder(order_id, item_id, item_amount, username);
    }

    @Override
    public void decreaseItemAmount(int order_id, int item_id, int item_amount) throws SQLException {
        dbHelper.decreaseItemAmount(order_id, item_id, item_amount);
    }
}
