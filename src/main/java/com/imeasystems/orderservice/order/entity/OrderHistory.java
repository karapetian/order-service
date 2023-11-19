package com.imeasystems.orderservice.order.entity;

import com.imeasystems.orderservice.order.util.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class OrderHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;

    @NotNull
    @CreationTimestamp
    @Column(name = "modifiedDate")
    private LocalDateTime modifiedDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "orderStatus")
    private OrderStatus orderStatus;
}
