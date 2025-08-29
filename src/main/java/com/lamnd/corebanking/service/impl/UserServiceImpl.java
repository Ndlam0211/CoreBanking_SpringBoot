package com.lamnd.corebanking.service.impl;

import com.lamnd.corebanking.config.AppConstant;
import com.lamnd.corebanking.dto.AccountInfo;
import com.lamnd.corebanking.dto.EmailDetails;
import com.lamnd.corebanking.dto.request.UserRequest;
import com.lamnd.corebanking.dto.response.BankResponse;
import com.lamnd.corebanking.entity.User;
import com.lamnd.corebanking.repository.UserRepo;
import com.lamnd.corebanking.service.EmailService;
import com.lamnd.corebanking.service.UserService;
import com.lamnd.corebanking.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailSevice;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        // check if email already exists
        if (userRepo.existsByEmail(userRequest.getEmail())) {
            // return email exists response
            return BankResponse.builder()
                    .responseCode(AppConstant.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AppConstant.ACCOUNT_EXISTS_MESSAGE)
                    .build();
        }

        // map UserRequest to User entity
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .gender(userRequest.getGender())
                .dateOfBirth(userRequest.getDateOfBirth())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .build();

        // save user to database
        User savedUser = userRepo.save(newUser);

        // send welcome email (omitted for brevity)
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Welcome to Core Banking System")
                .messageBody("Dear " + savedUser.getFirstName() + ",\n\nYour account has been created successfully.\nYour account number: " + savedUser.getAccountNumber() + "\n\nThank you for choosing our bank.\n\nBest regards,\nCore Banking Team")
                .build();
        emailSevice.sendEmail(emailDetails);


        // return success response
        return BankResponse.builder()
                .responseCode(AppConstant.ACCOUNT_CREATED_CODE)
                .responseMessage(AppConstant.ACCOUNT_CREATED_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountBalance(savedUser.getAccountBalance())
                        .build())
                .build();
    }
}
