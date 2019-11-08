package com.microservices.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.controller.OrderController;
import com.microservices.model.*;
import com.microservices.database.DBHelper;
import com.microservices.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
                //todo отправить все сразу
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

    @Override
    public void sendHttpToItem(Integer item_id, Integer amount) throws IOException {
        HttpURLConnection connection = null;
        String query = "http://localhost:9001/warehouse/items/" + item_id + "/addition/" + amount;
        connection = (HttpURLConnection) new URL(query).openConnection();
        connection.setRequestMethod("PUT");

        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.connect();

        Logger log = Logger.getLogger(OrderController.class);

        if(HttpURLConnection.HTTP_OK == connection.getResponseCode()){
            if (new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine() != null) {
                log.info("ok");
            }
        }
    }
}
