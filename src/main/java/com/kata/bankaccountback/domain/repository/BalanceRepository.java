package com.kata.bankaccountback.domain.repository;

import com.kata.bankaccountback.domain.model.entity.BalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<BalanceEntity, Long> {

}
