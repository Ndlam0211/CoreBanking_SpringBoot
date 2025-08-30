package com.lamnd.corebanking.service.impl;

import com.lamnd.corebanking.config.AppConstant;
import com.lamnd.corebanking.dto.AccountInfo;
import com.lamnd.corebanking.dto.EmailDetails;
import com.lamnd.corebanking.dto.TransactionDTO;
import com.lamnd.corebanking.dto.request.CreditDebitRequest;
import com.lamnd.corebanking.dto.request.EnquiryRequest;
import com.lamnd.corebanking.dto.request.TransferRequest;
import com.lamnd.corebanking.dto.request.UserRequest;
import com.lamnd.corebanking.dto.response.BankResponse;
import com.lamnd.corebanking.entity.User;
import com.lamnd.corebanking.repository.UserRepo;
import com.lamnd.corebanking.service.EmailService;
import com.lamnd.corebanking.service.TransactionService;
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

    @Autowired
    private TransactionService transactionService;

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

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        // check if account exists by the proviced account number
        boolean isAccountExist = userRepo.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AppConstant.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AppConstant.ACCOUNT_NOT_FOUND_MESSAGE)
                    .build();
        }

        // fetch user details
        User foundUser = userRepo.findByAccountNumber(request.getAccountNumber());

        // return success response with account details
        return BankResponse.builder()
                .responseCode(AppConstant.ACCOUNT_FOUND_CODE)
                .responseMessage(AppConstant.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(foundUser.getFirstName() + " " + foundUser.getLastName())
                        .accountNumber(foundUser.getAccountNumber())
                        .accountBalance(foundUser.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        // check if account exists by the proviced account number
        boolean isAccountExist = userRepo.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return AppConstant.ACCOUNT_NOT_FOUND_MESSAGE;
        }

        User foundUser = userRepo.findByAccountNumber(request.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getLastName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        // check if account exists by the proviced account number
        boolean isAccountExist = userRepo.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AppConstant.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AppConstant.ACCOUNT_NOT_FOUND_MESSAGE)
                    .build();
        }

        // fetch user details and update account balance
        User userToCredit = userRepo.findByAccountNumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        userRepo.save(userToCredit);

        // save transaction
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("CREDIT")
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(transactionDTO);

        // return success response with updated account details
        return BankResponse.builder()
                .responseCode(AppConstant.ACCOUNT_CREDITED_SUCCESS_CODE)
                .responseMessage(AppConstant.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName())
                        .accountNumber(userToCredit.getAccountNumber())
                        .accountBalance(userToCredit.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        // check if account exists by the proviced account number
        boolean isAccountExist = userRepo.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AppConstant.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AppConstant.ACCOUNT_NOT_FOUND_MESSAGE)
                    .build();
        }

        // fetch user details
        User userToDebit = userRepo.findByAccountNumber(request.getAccountNumber());

        // check if user has sufficient balance
        if (userToDebit.getAccountBalance().compareTo(request.getAmount()) < 0) {
            return BankResponse.builder()
                    .responseCode(AppConstant.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AppConstant.INSUFFICIENT_BALANCE_MESSAGE)
                    .build();
        }

        // update account balance if not insufficient balance
        userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
        userRepo.save(userToDebit);

        // save transaction
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountNumber(userToDebit.getAccountNumber())
                .transactionType("DEBIT")
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(transactionDTO);

        // return success response with updated account details
        return BankResponse.builder()
                .responseCode(AppConstant.ACCOUNT_DEBITED_SUCCESS_CODE)
                .responseMessage(AppConstant.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName())
                        .accountNumber(userToDebit.getAccountNumber())
                        .accountBalance(userToDebit.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse transfer(TransferRequest request) {
        // check if destination account exists
        boolean isDestAccountExist = userRepo.existsByAccountNumber(request.getDestinationAccountNumber());
        if (!isDestAccountExist) {
            return BankResponse.builder()
                    .responseCode(AppConstant.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AppConstant.ACCOUNT_NOT_FOUND_MESSAGE)
                    .build();
        }

        // get source account details
        User sourceUser = userRepo.findByAccountNumber(request.getSourceAccountNumber());
        // check if user has sufficient balance
        if (sourceUser.getAccountBalance().compareTo(request.getAmount()) < 0) {
            return BankResponse.builder()
                    .responseCode(AppConstant.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AppConstant.INSUFFICIENT_BALANCE_MESSAGE)
                    .build();
        }

        // update account balance if not insufficient balance
        sourceUser.setAccountBalance(sourceUser.getAccountBalance().subtract(request.getAmount()));
        userRepo.save(sourceUser);

        // save transaction for debit
        TransactionDTO debitTransaction = TransactionDTO.builder()
                .accountNumber(sourceUser.getAccountNumber())
                .transactionType("DEBIT")
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(debitTransaction);

        // get destination account details and update balance
        User destUser = userRepo.findByAccountNumber(request.getDestinationAccountNumber());
        destUser.setAccountBalance(destUser.getAccountBalance().add(request.getAmount()));
        userRepo.save(destUser);

        // save transaction for credit
        TransactionDTO creditTransaction = TransactionDTO.builder()
                .accountNumber(destUser.getAccountNumber())
                .transactionType("CREDIT")
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(creditTransaction);

        return BankResponse.builder()
                .responseCode(AppConstant.TRANSFER_SUCCESS_CODE)
                .responseMessage(AppConstant.TRANSFER_SUCCESS_MESSAGE)
                .build();
    }
}
