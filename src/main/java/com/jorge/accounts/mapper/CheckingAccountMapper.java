package com.jorge.accounts.mapper;

import com.jorge.accounts.model.*;
import org.springframework.stereotype.Component;

@Component
public class CheckingAccountMapper {
    public CheckingAccount mapToCheckingAccount(CheckingAccountRequest checkingAccountRequest) {
        return CheckingAccount.builder()
                .currencyType(Account.CurrencyType.valueOf(checkingAccountRequest.getCurrencyType().name()))
                .clientId(checkingAccountRequest.getClientId())
                .balance(checkingAccountRequest.getBalance())
                //CheckingAccount-fields
                .maintenanceFee(checkingAccountRequest.getMaintenanceFee())
                .build();
    }

    public CheckingAccountResponse mapToCheckingAccountResponse(CheckingAccount checkingAccount) {
        CheckingAccountResponse checkingAccountResponse = new CheckingAccountResponse();
        checkingAccountResponse.setId(checkingAccount.getId());
        checkingAccountResponse.setAccountNumber(checkingAccount.getAccountNumber());
        checkingAccountResponse.setAccountType(CheckingAccountResponse.AccountTypeEnum.valueOf(checkingAccount.getAccountType().name()));
        checkingAccountResponse.setCurrencyType(CheckingAccountResponse.CurrencyTypeEnum.valueOf(checkingAccount.getCurrencyType().name()));
        checkingAccountResponse.setBalance(checkingAccount.getBalance());
        checkingAccountResponse.setStatus(CheckingAccountResponse.StatusEnum.valueOf(checkingAccount.getStatus().name()));
        checkingAccountResponse.setClientId(checkingAccount.getClientId());
        checkingAccountResponse.setMovementsThisMonth(checkingAccount.getMovementsThisMonth());
        //CheckingAccount-fields
        checkingAccountResponse.setMaintenanceFee(checkingAccount.getMaintenanceFee());
        return checkingAccountResponse;
    }
}
