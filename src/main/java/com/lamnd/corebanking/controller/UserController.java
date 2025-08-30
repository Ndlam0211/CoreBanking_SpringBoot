package com.lamnd.corebanking.controller;

import com.lamnd.corebanking.dto.request.CreditDebitRequest;
import com.lamnd.corebanking.dto.request.EnquiryRequest;
import com.lamnd.corebanking.dto.request.TransferRequest;
import com.lamnd.corebanking.dto.request.UserRequest;
import com.lamnd.corebanking.dto.response.BankResponse;
import com.lamnd.corebanking.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Account Management APIs", description = "APIs for managing user accounts and transactions")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    @Operation(
            summary = "Create User Account",
            description = "Creates a new user account with the provided details.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Account created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "409", description = "Account with given email already exists")
            }
    )
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }

    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.nameEnquiry(enquiryRequest);
    }

    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.balanceEnquiry(enquiryRequest);
    }

    @PostMapping("/credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest request) {
        return userService.creditAccount(request);
    }

    @PostMapping("/debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest request) {
        return userService.debitAccount(request);
    }

    @PostMapping("/transfer")
    public BankResponse transfer(@RequestBody TransferRequest request) {
        return userService.transfer(request);
    }
}
