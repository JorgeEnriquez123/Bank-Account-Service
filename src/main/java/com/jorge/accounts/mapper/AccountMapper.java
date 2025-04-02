package com.jorge.accounts.mapper;

import com.jorge.accounts.model.Account;
import com.jorge.accounts.model.AccountRequest;
import com.jorge.accounts.model.AccountResponse;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public Account mapToAccount(AccountRequest accountRequest) {
        return Account.builder()
                .accountType(Account.AccountType.valueOf(accountRequest.getAccountType().name()))
                .currencyType(Account.CurrencyType.valueOf(accountRequest.getCurrencyType().name()))
                .balance(accountRequest.getBalance())
                .status(Account.AccountStatus.valueOf(accountRequest.getStatus().name()))
                .customerDni(accountRequest.getCustomerDni())
                .build();
    }

    public AccountResponse mapToAccountResponse(Account account) {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setId(account.getId());
        accountResponse.setAccountNumber(account.getAccountNumber());
        accountResponse.setAccountType(AccountResponse.AccountTypeEnum.valueOf(account.getAccountType().name()));
        accountResponse.setCurrencyType(AccountResponse.CurrencyTypeEnum.valueOf(account.getCurrencyType().name()));
        accountResponse.setBalance(account.getBalance());
        accountResponse.setStatus(AccountResponse.StatusEnum.valueOf(account.getStatus().name()));
        accountResponse.setCreatedAt(account.getCreatedAt());
        accountResponse.customerDni(account.getCustomerDni());
        accountResponse.setMovementsThisMonth(account.getMovementsThisMonth());
        accountResponse.setMonthlyMovementsLimit(account.getMonthlyMovementsLimit());
        accountResponse.setMaintenanceFee(account.getMaintenanceFee());
        accountResponse.setHolders(account.getHolders());
        accountResponse.setAuthorizedSigners(account.getAuthorizedSigners());
        accountResponse.setAllowedWithdrawal(account.getAllowedWithdrawal());
        return accountResponse;
    }
}
