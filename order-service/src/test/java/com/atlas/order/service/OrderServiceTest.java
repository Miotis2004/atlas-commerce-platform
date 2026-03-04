package com.atlas.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.atlas.order.dto.CreateOrderRequest;
import com.atlas.order.dto.OrderResponse;
import com.atlas.order.entity.OrderEntity;
import com.atlas.order.entity.OrderItemEntity;
import com.atlas.order.entity.OrderStatus;
import com.atlas.order.kafka.OrderEventPublisher;
import com.atlas.order.repository.OrderRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderEventPublisher orderEventPublisher;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldReturnExistingOrderWhenIdempotencyKeyAlreadyUsed() {
        OrderEntity existing = new OrderEntity();
        existing.setOrderId("ord-1");
        existing.setIdempotencyKey("idem-1");
        existing.setCustomerId("customer");
        existing.setStatus(OrderStatus.CREATED);
        existing.setTotalAmount(new BigDecimal("12.50"));
        existing.setCreatedAt(Instant.now());
        existing.setItems(List.of());
        when(orderRepository.findByIdempotencyKey("idem-1")).thenReturn(Optional.of(existing));

        OrderResponse response = orderService.createOrder(new CreateOrderRequest("customer", List.of()), "idem-1");

        assertThat(response.orderId()).isEqualTo("ord-1");
    }

    @Test
    void shouldPersistAndPublishCreatedEvent() {
        CreateOrderRequest request = new CreateOrderRequest("customer-1", List.of(
                new CreateOrderRequest.Item(1L, 2, new BigDecimal("5.00"))
        ));
        when(orderRepository.findByIdempotencyKey("idem-2")).thenReturn(Optional.empty());
        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = orderService.createOrder(request, "idem-2");

        assertThat(response.totalAmount()).isEqualByComparingTo("10.00");
        verify(orderEventPublisher).publishCreated(any());
    }
}
