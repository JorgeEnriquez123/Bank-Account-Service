package com.jorge.accounts.mapper;

import com.jorge.accounts.model.*;
import org.springframework.stereotype.Component;

@Component
public class FixedTermAccountMapper {
    public FixedTermAccount mapToFixedTermAccount(FixedTermAccountRequest fixedTermAccountRequest) {
        return FixedTermAccount.builder()
                .currencyType(Account.CurrencyType.valueOf(fixedTermAccountRequest.getCurrencyType().name()))
                .clientId(fixedTermAccountRequest.getClientId())
                .balance(fixedTermAccountRequest.getBalance())
                //FixedTermAccount-fields
                .allowedWithdrawal(fixedTermAccountRequest.getAllowedWithdrawal())
                .build();
    }

    public FixedTermAccountResponse mapToFixedTermAccountResponse(FixedTermAccount fixedTermAccount) {
        FixedTermAccountResponse fixedTermAccountResponse = new FixedTermAccountResponse();
        fixedTermAccountResponse.setId(fixedTermAccount.getId());
        fixedTermAccountResponse.setAccountNumber(fixedTermAccount.getAccountNumber());
        fixedTermAccountResponse.setAccountType(FixedTermAccountResponse.AccountTypeEnum.valueOf(fixedTermAccount.getAccountType().name()));
        fixedTermAccountResponse.setCurrencyType(FixedTermAccountResponse.CurrencyTypeEnum.valueOf(fixedTermAccount.getCurrencyType().name()));
        fixedTermAccountResponse.setBalance(fixedTermAccount.getBalance());
        fixedTermAccountResponse.setStatus(FixedTermAccountResponse.StatusEnum.valueOf(fixedTermAccount.getStatus().name()));
        fixedTermAccountResponse.setClientId(fixedTermAccount.getClientId());
        fixedTermAccountResponse.setMovementsThisMonth(fixedTermAccount.getMovementsThisMonth());
        //FixedTermAccount-fields
        fixedTermAccountResponse.setAllowedWithdrawal(fixedTermAccount.getAllowedWithdrawal());
        return fixedTermAccountResponse;
    }
}
