package com.lamnd.corebanking.repository;

import com.lamnd.corebanking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepo extends JpaRepository<Transaction, String> {
}
