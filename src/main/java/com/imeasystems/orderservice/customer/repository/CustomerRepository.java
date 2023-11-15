package com.imeasystems.orderservice.customer.repository;

import com.imeasystems.orderservice.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}