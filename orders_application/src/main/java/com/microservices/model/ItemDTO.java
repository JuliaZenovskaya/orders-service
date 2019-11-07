package com.microservices.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class ItemDTO {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("amount")
    private Integer amount;

    public ItemDTO(Integer id, Integer amount) {
        this.id = id;
        this.amount = amount;
    }
}
