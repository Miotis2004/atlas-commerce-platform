package com.atlas.order.service;

import com.atlas.order.dto.CreateOrderRequest;
import com.atlas.order.dto.OrderResponse;
import com.atlas.order.entity.OrderEntity;
import com.atlas.order.entity.OrderItemEntity;
import com.atlas.order.entity.OrderStatus;
import com.atlas.order.exception.ResourceNotFoundException;
import com.atlas.order.kafka.OrderCancelledEvent;
import com.atlas.order.kafka.OrderCompletedEvent;
import com.atlas.order.kafka.OrderCreatedEvent;
import com.atlas.order.kafka.OrderEventPublisher;
import com.atlas.order.repository.OrderRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;

    public OrderService(OrderRepository orderRepository, OrderEventPublisher orderEventPublisher) {
        this.orderRepository = orderRepository;
        this.orderEventPublisher = orderEventPublisher;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request, String idempotencyKey) {
        OrderEntity existing = orderRepository.findByIdempotencyKey(idempotencyKey).orElse(null);
        if (existing != null) {
            return mapResponse(existing);
        }

        OrderEntity order = new OrderEntity();
        order.setOrderId(UUID.randomUUID().toString());
        order.setIdempotencyKey(idempotencyKey);
        order.setCustomerId(request.customerId());
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(Instant.now());

        BigDecimal total = request.items().stream()
                .map(item -> item.unitPrice().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(total);

        List<OrderItemEntity> items = request.items().stream().map(item -> {
            OrderItemEntity entity = new OrderItemEntity();
            entity.setOrder(order);
            entity.setProductId(item.productId());
            entity.setQuantity(item.quantity());
            entity.setUnitPrice(item.unitPrice());
            return entity;
        }).toList();
        order.setItems(items);

        OrderEntity saved = orderRepository.save(order);
        orderEventPublisher.publishCreated(toCreatedEvent(saved));
        return mapResponse(saved);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(String orderId) {
        return orderRepository.findByOrderId(orderId)
                .map(this::mapResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
    }

    @Transactional
    public void markReserved(String orderId) {
        OrderEntity order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
        if (order.getStatus() == OrderStatus.CANCELLED) {
            return;
        }
        order.setStatus(OrderStatus.RESERVED);
        orderRepository.save(order);
        orderEventPublisher.publishCompleted(new OrderCompletedEvent(orderId));
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
    }

    @Transactional
    public void markRejected(String orderId, String reason) {
        OrderEntity order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.COMPLETED) {
            return;
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        orderEventPublisher.publishCancelled(new OrderCancelledEvent(orderId, List.of(reason)));
    }

    private OrderCreatedEvent toCreatedEvent(OrderEntity order) {
        return new OrderCreatedEvent(
                order.getOrderId(),
                order.getCustomerId(),
                order.getItems().stream()
                        .map(item -> new OrderCreatedEvent.Item(item.getProductId(), item.getQuantity(), item.getUnitPrice()))
                        .toList(),
                order.getTotalAmount()
        );
    }

    private OrderResponse mapResponse(OrderEntity order) {
        return new OrderResponse(
                order.getOrderId(),
                order.getCustomerId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getItems().stream()
                        .map(item -> new OrderResponse.Item(item.getProductId(), item.getQuantity(), item.getUnitPrice()))
                        .toList()
        );
    }
}
