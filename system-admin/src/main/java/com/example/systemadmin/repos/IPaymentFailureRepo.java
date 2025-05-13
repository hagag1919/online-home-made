package com.example.systemadmin.repos;

import com.example.systemadmin.models.PaymentFailure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPaymentFailureRepo extends JpaRepository<PaymentFailure, Long> {
}
