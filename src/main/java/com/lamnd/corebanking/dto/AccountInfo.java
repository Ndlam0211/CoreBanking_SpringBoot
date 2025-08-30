package com.lamnd.corebanking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo {
    @Schema(description = "User Account Name", example = "John Doe")
    private String accountName;
    @Schema(description = "User Account Number", example = "2025123456")
    private String accountNumber;
    @Schema(description = "User Account Balance", example = "1000.00")
    private BigDecimal accountBalance;
}
