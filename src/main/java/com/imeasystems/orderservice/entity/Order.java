package com.imeasystems.orderservice.entity;

import com.imeasystems.orderservice.util.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customerId", nullable = false)
    private Customer customer;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

//    private List<OrderItem> orderItems;

    @Column(name = "shippingAddress")
    private String shippingAddress;

    @Column(name = "paymentDetails")
    private String paymentDetails;

    @CreationTimestamp
    @Column(name = "orderCreationDate")
    private LocalDateTime orderCreationDate;

    @Column(name = "paymentDate")
    private LocalDateTime paymentDate;

    @Column(name = "shippedDate")
    private LocalDateTime shippedDate;

    @Column(name = "deliveredDate")
    private LocalDateTime deliveredDate;
}
