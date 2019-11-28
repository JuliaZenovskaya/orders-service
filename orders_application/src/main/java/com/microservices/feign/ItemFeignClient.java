package com.microservices.feign;

import com.microservices.model.Item;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "items-service")
public interface ItemFeignClient {
    @RequestMapping(method = RequestMethod.PUT, value = "warehouse/items/{id}/addition/{amount}")
    public Item changeItem(@PathVariable int id, @PathVariable int amount);
}
