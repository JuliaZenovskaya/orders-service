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
    public int id;

    @NotNull
    public String status;

    ArrayList<Item> items;

    @NotNull
    public float totalCost;

    @NotNull
    public int totalAmount;

    @NotNull
    public String email;

    @NotNull
    public String country;

    @NotNull
    public String city;

    @NotNull
    public String street;

    @NotNull
    public int house;

    public int corp;

    @NotNull
    public int flat;

    public Order(@NotNull int id, @NotNull String status, ArrayList<Item> items, @NotNull String email,
                 @NotNull String country, @NotNull String city, @NotNull int house,
                 @NotNull String street, int corp, @NotNull int flat) {
        this.id = id;
        this.status = status;
        this.items = items;
        this.email = email;
        this.country = country;
        this.city = city;
        this.street = street;
        this.house = house;
        this.corp = corp;
        this.flat = flat;
        this.totalCost = items.get(0).price;
        this.totalAmount = items.get(0).amount;
    }
}
