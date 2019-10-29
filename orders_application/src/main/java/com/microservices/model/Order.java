package com.microservices.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Getter
@Setter
@ToString
public class Order {
    @NotNull
    private int order_id;

    @NotNull
    private OrderStatus status;

    @NotNull
    private float totalCost;

    @NotNull
    private int totalAmount;

    @NotNull
    private String username;

    private ArrayList<Item> items;

    public Order(@NotNull int order_id, @NotNull OrderStatus status, @NotNull float totalCost, @NotNull int totalAmount, @NotNull String username, ArrayList<Item> items) {
        this.order_id = order_id;
        this.status = status;
        this.totalCost = totalCost;
        this.totalAmount = totalAmount;
        this.username = username;
        this.items = items;
    }
}
