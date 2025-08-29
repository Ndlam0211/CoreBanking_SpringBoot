package com.lamnd.corebanking.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lamnd.corebanking.dto.AccountInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankResponse {
    private String responseCode;
    private String responseMessage;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AccountInfo accountInfo;
}
