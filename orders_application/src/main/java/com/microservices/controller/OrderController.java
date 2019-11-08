package com.microservices.controller;


import com.microservices.model.ItemDTO;
import com.microservices.model.Order;
import com.microservices.model.OrderDTO;
import com.microservices.model.OrderStatus;
import com.microservices.service.OrderService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("orders")
public class OrderController {
    private OrderService orderService;
    private static final Logger log = Logger.getLogger(OrderController.class);

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PutMapping(value = "{id}/items")
    public void decreaseItemAmount(@PathVariable int id, @RequestParam int item_id, @RequestParam int amount) {
        try {
            orderService.decreaseItemAmount(id, item_id, amount);
            orderService.send(new ItemDTO(item_id,amount));
            log.info("Decreased");
        } catch (SQLException e) {
            log.error(e.toString());
        }
    }

    @GetMapping
    public ArrayList<Order> getAllOrders() throws SQLException {
        return orderService.getAllOrders();
    }

    @PostMapping (value = "{order_id}/item")
    @ResponseStatus(HttpStatus.CREATED)
    public int addItemToOrder(@PathVariable String order_id, @RequestParam int item_id, @RequestParam int amount,
                              @RequestParam String username){
        try {
            orderService.sendHttpToItem(item_id, -amount);

            return orderService.addItemToOrder(order_id, item_id, amount, username);
            //log.info("Item with id = " + item_id + " added to cart");
        } catch (SQLException | IOException e) {
            log.error("Error adding product with id = " + item_id + " to cart: " + e.toString());
            return 0;
        }
    }

    @GetMapping(value = "{order_id}")
    public Order getOrderById (@PathVariable int order_id) {
        try {
            Order temp = orderService.getOrderById(order_id);
            log.info("Order with id = " + order_id + " was found: " + temp.toString());
            return temp;
        } catch (SQLException e) {
            log.error("Order with id = " + order_id + "was not found: " + e.toString());
            return null;
        }
    }

    @PutMapping(value = "{id}/status/{status}")
    public OrderDTO changeStatus(@PathVariable int id, @PathVariable OrderStatus status) {
        try {
            if (status == OrderStatus.CANCELLED) {
                for (ItemDTO i:
                        orderService.getItemDTOS(id)) {
                    orderService.send(i);
                }
            }
            return orderService.changeOrderStatus(id, status);
            //log.info("Order status replaced by " + status);
        } catch (SQLException e) {
            log.error("Error changing order status: " + e.toString());
            return null;
        }
    }
}
