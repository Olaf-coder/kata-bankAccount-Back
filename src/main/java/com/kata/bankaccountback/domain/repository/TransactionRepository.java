package com.kata.bankaccountback.domain.repository;

import com.kata.bankaccountback.domain.model.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("com.kata.bankaccountback.domain.repository.TransactionRepository")
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    List<TransactionEntity> findAll();

    Optional<TransactionEntity> findById(Long id);
}
