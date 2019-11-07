package com.microservices.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class StatusDTO {
    @JsonProperty("order_id")
    public int order_id;

    @JsonProperty("status")
    public OrderStatus status;

    @JsonCreator
    public StatusDTO(@JsonProperty("order_id") int order_id, @JsonProperty("status)") OrderStatus status) {
        this.order_id = order_id;
        this.status = status;
    }
}
