package com.lamnd.corebanking.service;

import com.lamnd.corebanking.dto.request.CreditDebitRequest;
import com.lamnd.corebanking.dto.request.EnquiryRequest;
import com.lamnd.corebanking.dto.request.UserRequest;
import com.lamnd.corebanking.dto.response.BankResponse;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    String nameEnquiry(EnquiryRequest request);
    BankResponse balanceEnquiry(EnquiryRequest request);
    BankResponse creditAccount(CreditDebitRequest request);
    BankResponse debitAccount(CreditDebitRequest request);
}
