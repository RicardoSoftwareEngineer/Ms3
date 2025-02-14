package com.order.ms3.entity;

import com.order.ms3.dto.OrderDTO;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OrderTable")
public class OrderEntity {
    @Id
    private String id;
    private OrderStatus status;
    private double totalPrice;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<ItemEntity> items = new ArrayList<>();

    public OrderEntity() {
    }

    public OrderEntity(OrderDTO orderDTO) {
        this.id = orderDTO.getOrderId();
        this.status = OrderStatus.PROCESSING;
        for(ItemEntity itemEntity: orderDTO.getItems()){
            this.items.add(itemEntity);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<ItemEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemEntity> items) {
        this.items = items;
    }
}
