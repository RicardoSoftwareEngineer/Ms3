package com.order.ms3.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.ms3.dto.OrderDTO;
import com.order.ms3.entity.OrderEntity;
import com.order.ms3.entity.OrderStatus;
import com.order.ms3.repository.CacheRepository;
import com.order.ms3.repository.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Receiver {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CacheRepository mongoRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    ObjectMapper objectMapper = new ObjectMapper();
    OrderDTO orderDTO = null;

    public OrderDTO receiveMessage(String message) throws JsonProcessingException, InterruptedException {
        orderDTO = new ObjectMapper().readValue(message, OrderDTO.class);
        System.out.println("sup");
        System.out.println(orderDTO.getOrderId());
        Optional<OrderEntity> orderFromPostgre = orderRepository.findById(orderDTO.getId());
        if(orderFromPostgre.isPresent()){
            orderFromPostgre.get().setStatus(OrderStatus.PROCESSED);
            orderFromPostgre.get().setTotalPrice(orderDTO.getTotal());
            orderRepository.save(orderFromPostgre.get());
            System.out.println("Save oi");
        }
        System.out.println("nossa");
        try{
            System.out.println("here?");
            mongoRepository.deleteById(orderDTO.getId());
            System.out.println("delete");
        }catch (Exception e){

        }
        return orderDTO;
    }
}