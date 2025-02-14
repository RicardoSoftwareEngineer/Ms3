package com.order.ms3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.ms3.dto.OrderDTO;
import com.order.ms3.entity.CacheEntity;
import com.order.ms3.entity.ItemEntity;
import com.order.ms3.entity.OrderEntity;
import com.order.ms3.entity.OrderStatus;
import com.order.ms3.queue.Receiver;
import com.order.ms3.repository.CacheRepository;
import com.order.ms3.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class Ms3ApplicationTests {
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private CacheRepository cacheRepository;

	@Autowired
	private Receiver receiver;

	private TestUtils testUtils = new TestUtils();

	private OrderDTO orderDTO;
	private OrderEntity orderEntity;
	private ItemEntity item1, item2;

	@BeforeEach
	void setUp() throws JsonProcessingException {

	}

	@Test
	void test_order_database_update() throws JsonProcessingException, InterruptedException {
		OrderEntity order = new OrderEntity(testUtils.createSampleOrder());
		order = orderRepository.save(order);
		OrderDTO orderDTO = receiver.receiveMessage(testUtils.toJson(order));
		Optional<OrderEntity> orderCreated = orderRepository.findById(orderDTO.getId());
		assertEquals(orderCreated.isPresent(), true);
		assertEquals(orderCreated.get().getStatus(), OrderStatus.PROCESSED);
		assertNotEquals(orderCreated.get().getTotalPrice(), 0.0);
	}

	@Test
	void test_order_cache_database_deletion() throws JsonProcessingException, InterruptedException {
		OrderEntity order = new OrderEntity(testUtils.createSampleOrder());
		order = orderRepository.save(order);
		OrderDTO orderDTO = receiver.receiveMessage(testUtils.toJson(order));
		Optional<CacheEntity> orderCreated = cacheRepository.findById(orderDTO.getId());
		assertEquals(orderCreated.isPresent(), false);
	}

	@Test
	void test_microservice_contract() throws Exception {

	}
}
