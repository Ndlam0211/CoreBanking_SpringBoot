package com.lamnd.corebanking.service;

import com.lamnd.corebanking.dto.request.UserRequest;
import com.lamnd.corebanking.dto.response.BankResponse;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
}
