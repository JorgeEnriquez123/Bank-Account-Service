package com.jorge.accounts.mapper;

import com.jorge.accounts.model.Account;
import com.jorge.accounts.model.SavingsAccount;
import com.jorge.accounts.model.SavingsAccountRequest;
import com.jorge.accounts.model.SavingsAccountResponse;
import org.springframework.stereotype.Component;

@Component
public class SavingsAccountMapper {
    public SavingsAccount mapToSavingsAccount(SavingsAccountRequest savingsAccountRequest) {
        return SavingsAccount.builder()
                .currencyType(Account.CurrencyType.valueOf(savingsAccountRequest.getCurrencyType().name()))
                .clientId(savingsAccountRequest.getClientId())
                .balance(savingsAccountRequest.getBalance())
                .monthlyMovementsLimit(savingsAccountRequest.getMonthlyMovementsLimit())
                .build();
    }

    public SavingsAccountResponse mapToSavingsAccountResponse(SavingsAccount savingsAccount) {
        SavingsAccountResponse savingsAccountResponse = new SavingsAccountResponse();
        savingsAccountResponse.setId(savingsAccount.getId());
        savingsAccountResponse.setAccountNumber(savingsAccount.getAccountNumber());
        savingsAccountResponse.setAccountType(SavingsAccountResponse.AccountTypeEnum.valueOf(savingsAccount.getAccountType().name()));
        savingsAccountResponse.setCurrencyType(SavingsAccountResponse.CurrencyTypeEnum.valueOf(savingsAccount.getCurrencyType().name()));
        savingsAccountResponse.setBalance(savingsAccount.getBalance());
        savingsAccountResponse.setStatus(SavingsAccountResponse.StatusEnum.valueOf(savingsAccount.getStatus().name()));
        savingsAccountResponse.setClientId(savingsAccount.getClientId());
        savingsAccountResponse.setMovementsThisMonth(savingsAccount.getMovementsThisMonth());
        //SavingsAccount-fields
        savingsAccountResponse.setMonthlyMovementsLimit(savingsAccount.getMonthlyMovementsLimit());
        return savingsAccountResponse;
    }
}
