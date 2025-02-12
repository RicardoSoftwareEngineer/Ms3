package com.order.ms3.repository;

import com.order.ms3.entity.OrderEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderRepository extends CrudRepository<OrderEntity, String> {

    @Modifying
    @Transactional
    @Query("update OrderEntity o set o.totalPrice = :newTotal where o.id = :orderId")
    void updateTotalByOrderId(@Param("newTotal") double newTotal, @Param("orderId") String orderId);

    List<OrderEntity> findAll();
}
