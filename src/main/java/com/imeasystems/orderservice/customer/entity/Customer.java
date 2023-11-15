package com.imeasystems.orderservice.customer.entity;

import com.imeasystems.orderservice.order.entity.Order;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Customer {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    private String surname;

    @NotNull
    @Email(regexp = "^(.+)@(.+)$")
    private String email;

    @NotNull
    private String phoneNumber;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;
}
