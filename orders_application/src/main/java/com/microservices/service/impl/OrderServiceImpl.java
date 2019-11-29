package com.microservices.service.impl;

import com.microservices.feign.ItemFeignClient;
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
    private ItemFeignClient itemFeignClient;
    private DBHelper dbHelper = new DBHelper();
    private final KafkaTemplate kafkaItemTemplateSend;

    @Autowired
    public OrderServiceImpl(KafkaTemplate kafkaItemTemplateSend, ItemFeignClient itemFeignClient) {
        this.kafkaItemTemplateSend = kafkaItemTemplateSend;
        this.itemFeignClient = itemFeignClient;
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
    public int addItemToOrder(String order_id, int item_id, int item_amount, float item_price, String item_name, String username) throws SQLException {
        return dbHelper.addItemToOrder(order_id, item_id, item_amount, item_price, item_name, username);
    }

    @Override
    public void decreaseItemAmount(int order_id, int item_id, int item_amount) throws SQLException {
        dbHelper.decreaseItemAmount(order_id, item_id, item_amount);
    }

    @Override
    public Item sendHttpToItem(Integer item_id, Integer amount) {
        //String url = "http://localhost:9001/warehouse/items/" + item_id + "/addition/" + amount;
        //String urlGet = "http://localhost:9001/warehouse/items/" + item_id;
        //HttpHeaders headers = new HttpHeaders();
        //headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
//
        //RestTemplate restTemplate = new RestTemplate();
//
        //ItemDTO itemDTO = new ItemDTO(item_id, amount);
        //HttpEntity<ItemDTO> requestBody = new HttpEntity<>(itemDTO, headers);
//
        //restTemplate.exchange(url, HttpMethod.PUT, requestBody, Void.class);
//
        //Item item = restTemplate.getForObject(urlGet, Item.class);

        Item item = itemFeignClient.changeItem(item_id, amount);

        if (item != null) {
            System.out.println("(Client side) Employee after update: " + item.toString());
            if (item.amount > -amount) {
                item.amount = -amount;
                item.price *= -amount;
                return item;
            } else {
                return null;
            }
        } else {
            return null;
        }


        //HttpURLConnection connection = null;
        //String query = "http://localhost:9001/warehouse/items/" + item_id + "/addition/" + amount;
        //connection = (HttpURLConnection) new URL(query).openConnection();
        //connection.setRequestMethod("PUT");
//
        //connection.setDoOutput(true);
        //connection.setUseCaches(false);
        //connection.connect();
//
        //connection.setRequestProperty(Item);
        //Logger log = Logger.getLogger(OrderController.class);
//
        //if(HttpURLConnection.HTTP_OK == connection.getResponseCode()){
        //    if (new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine() != null) {
        //        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        //        System.out.println(new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine());
        //        return true;
        //    } else {
        //        return false;
        //    }
        //} else {
        //    return false;
        //}
    }
}
