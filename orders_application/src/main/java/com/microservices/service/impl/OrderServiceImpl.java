package com.microservices.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.model.*;
import com.microservices.database.DBHelper;
import com.microservices.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private DBHelper dbHelper = new DBHelper();
    private final KafkaTemplate kafkaItemTemplateSend;

    @Autowired
    public OrderServiceImpl(KafkaTemplate kafkaItemTemplateSend) {
        this.kafkaItemTemplateSend = kafkaItemTemplateSend;
    }

    @Override
    public void send(ItemDTO itemDTOS) {
      kafkaItemTemplateSend.send("items", itemDTOS);
    }

    @Override
    @KafkaListener(id = "Order", topics = {"orders"}, containerFactory = "singleFactory")
    public void consume(StatusDTO statusDTO) throws SQLException {
        dbHelper.changeOrderStatus(statusDTO.order_id, statusDTO.status);
        if (statusDTO.status == OrderStatus.FAILED){
            for (ItemDTO i:
                    getItemDTOS(statusDTO.order_id)) {
                send(i);
            }
        }
    }

    @Override
    public List<ItemDTO> getItemDTOS(int id) throws SQLException {
        return dbHelper.getItemDTO(id);
    }


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
