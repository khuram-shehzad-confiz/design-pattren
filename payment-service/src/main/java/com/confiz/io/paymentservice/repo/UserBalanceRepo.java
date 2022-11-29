package com.confiz.io.paymentservice.repo;

import com.confiz.io.paymentservice.entities.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBalanceRepo extends JpaRepository<UserBalance, Integer> {
    UserBalance getById(Integer id);
}
