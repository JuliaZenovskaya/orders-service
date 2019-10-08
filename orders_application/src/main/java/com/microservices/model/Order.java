package com.microservices.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class Order {
    @NotNull
    public int id;

    @NotNull
    public String status;

    @NotNull
    public int itemID;

    @NotNull
    public int itemAmount;

    @NotNull
    public float price;

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

    public Order(@NotNull int id, @NotNull String status, @NotNull int itemID,
                 @NotNull int itemAmount, @NotNull float price, @NotNull String email,
                 @NotNull String country, @NotNull String city, @NotNull int house,
                 @NotNull String street, int corp, @NotNull int flat) {
        this.id = id;
        this.status = status;
        this.itemID = itemID;
        this.itemAmount = itemAmount;
        this.price = price;
        this.email = email;
        this.country = country;
        this.city = city;
        this.street = street;
        this.house = house;
        this.corp = corp;
        this.flat = flat;
    }
}
