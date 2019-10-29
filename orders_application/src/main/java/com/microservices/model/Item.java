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
    private int id;

    @NotNull
    private String name;

    @NotNull
    private int amount;

    @NotNull
    private float price;

    public Item(@NotNull int id, @NotNull String name, @NotNull int amount, @NotNull float price) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.price = price;
    }
}
