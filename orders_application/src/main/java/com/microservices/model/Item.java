package com.microservices.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class Item {
    @JsonProperty("id")
    public int id;

    @JsonProperty("name")
    public String name;

    @JsonProperty("amount")
    public int amount;

    @JsonProperty("price")
    public float price;

    @JsonCreator
    public Item(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("amount") int amount,
                @JsonProperty("price") float price) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.price = price;
    }
}
