package com.order.ms3;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.ms3.dto.OrderDTO;
import com.order.ms3.entity.ItemEntity;

import java.util.UUID;

public class TestUtils {
    private ObjectMapper objectMapper = new ObjectMapper();

    public OrderDTO createSampleOrder(){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(UUID.randomUUID().toString());
        orderDTO.setClientId("99999212-d4fa-9659-32c7-3d7c65e2a70a");
        ItemEntity item = new ItemEntity();
        item.setName("Product a");
        item.setQuantity(13);
        item.setPrice(10.00);
        orderDTO.getItems().add(item);
        item = new ItemEntity();
        item.setName("Product b");
        item.setQuantity(3);
        item.setPrice(20.00);
        orderDTO.getItems().add(item);
        item = new ItemEntity();
        item.setName("Product c");
        item.setQuantity(5);
        item.setPrice(30.00);
        orderDTO.getItems().add(item);
        return orderDTO;
    }

    public String toJson(Object object){
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonString;
    }
}
