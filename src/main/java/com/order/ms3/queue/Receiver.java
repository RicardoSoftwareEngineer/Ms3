package com.order.ms3.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.ms3.dto.OrderDTO;
import com.order.ms3.entity.CacheEntity;
import com.order.ms3.entity.OrderEntity;
import com.order.ms3.entity.OrderStatus;
import com.order.ms3.repository.MongoRepository;
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
    private MongoRepository mongoRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    ObjectMapper objectMapper = new ObjectMapper();
    OrderDTO orderDTO = null;

    public void receiveMessage(String message) throws JsonProcessingException, InterruptedException {
        orderDTO = objectMapper.readValue(message, OrderDTO.class);
        Optional<OrderEntity> orderFromPostgre = orderRepository.findById(orderDTO.getOrderId());
        if(orderFromPostgre.isPresent()){
            orderFromPostgre.get().setStatus(OrderStatus.PROCESSED);
            orderFromPostgre.get().setTotalPrice(orderDTO.getTotal());
            orderRepository.save(orderFromPostgre.get());
        }
        Optional<CacheEntity> cacheEntity = mongoRepository.findById(orderDTO.getOrderId());
        if(cacheEntity.isPresent()){
            mongoRepository.delete(cacheEntity.get());
        }
    }
}