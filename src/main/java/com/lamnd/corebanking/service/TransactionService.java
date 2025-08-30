package com.lamnd.corebanking.service;

import com.lamnd.corebanking.dto.TransactionDTO;

public interface TransactionService {
    void saveTransaction(TransactionDTO transactionDTO);
}
