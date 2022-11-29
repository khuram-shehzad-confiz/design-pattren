package com.confiz.io.paymentservice.repo;

import com.confiz.io.paymentservice.entities.UserTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserTransactionRepo extends JpaRepository<UserTransaction, UUID> {
}
