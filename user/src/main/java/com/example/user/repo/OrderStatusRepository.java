package com.example.user.repo;

import com.example.user.models.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
    Optional<OrderStatus> findById(Long orderId);

    @Override
    List<OrderStatus> findAll();
}
