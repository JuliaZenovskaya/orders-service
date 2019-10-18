package com.microservices.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class Item {
    @NotNull
    public int id;
    //@NotNull
    //public String name;
    @NotNull
    public int amount;
    @NotNull
    public float price;

    public Item(@NotNull int id, @NotNull int amount, @NotNull float price) {
        this.id = id;
        this.amount = amount;
        this.price = price;
    }
}
