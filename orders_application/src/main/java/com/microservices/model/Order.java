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
    public int order_id;

    @NotNull
    public OrderStatus status;

    @NotNull
    public float totalCost;

    @NotNull
    public int totalAmount;

    @NotNull
    public String username;

    ArrayList<Item> items;

    public Order(@NotNull int order_id, @NotNull OrderStatus status, @NotNull float totalCost, @NotNull int totalAmount, @NotNull String username, ArrayList<Item> items) {
        this.order_id = order_id;
        this.status = status;
        this.totalCost = totalCost;
        this.totalAmount = totalAmount;
        this.username = username;
        this.items = items;
    }
}
