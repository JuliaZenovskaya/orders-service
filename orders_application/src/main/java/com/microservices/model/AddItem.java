package com.microservices.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class AddItem {
    @NotNull
    public int id;

    @NotNull
    public float price;

    public AddItem(@NotNull int id, @NotNull float price) {
        this.id = id;
        this.price = price;
    }
}
