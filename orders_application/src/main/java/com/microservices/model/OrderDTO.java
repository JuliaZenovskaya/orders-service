package com.microservices.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class OrderDTO {
    @NotNull
    private int order_id;

    private OrderStatus status;

    public OrderDTO(@NotNull int order_id, OrderStatus status) {
        this.order_id = order_id;
        this.status = status;
    }
}
