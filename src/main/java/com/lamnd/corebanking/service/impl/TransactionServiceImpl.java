package com.lamnd.corebanking.service.impl;

import com.lamnd.corebanking.dto.TransactionDTO;
import com.lamnd.corebanking.entity.Transaction;
import com.lamnd.corebanking.repository.TransactionRepo;
import com.lamnd.corebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepo transactionRepo;

    @Override
    public void saveTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = Transaction.builder()
                .accountNumber(transactionDTO.getAccountNumber())
                .transactionType(transactionDTO.getTransactionType())
                .amount(transactionDTO.getAmount())
                .status("SUCCESS")
                .build();
        transactionRepo.save(transaction);
    }
}
