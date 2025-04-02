package com.jorge.accounts.service.impl;

import com.jorge.accounts.model.*;
import com.jorge.accounts.repository.AccountRepository;
import com.jorge.accounts.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SavingsAccountServiceImpl {
    private final AccountUtils accountUtils;
    private final AccountRepository accountRepository;

    private final Account.AccountType ACCOUNT_TYPE = Account.AccountType.SAVINGS;
    private final Integer DEFAULT_MOVEMENT_AMOUNT = 0;
    private final Integer DEFAULT_SAVINGS_ACCOUNT_MONTHLY_MOVEMENT_LIMIT = 30; // EXAMPLE

    /*@Override
    public Mono<SavingsAccountResponse> createSavingsAccount(SavingsAccountRequest savingsAccountRequest) {
        SavingsAccount savingsAccount = savingsAccountMapper.mapToSavingsAccount(savingsAccountRequest);
        savingsAccount.setAccountNumber(accountUtils.generateAccountNumber());
        savingsAccount.setAccountType(ACCOUNT_TYPE);
        savingsAccount.setStatus(Account.AccountStatus.ACTIVE);
        savingsAccount.setCreatedAt(LocalDateTime.now());
        savingsAccount.setMovementsThisMonth(DEFAULT_MOVEMENT_AMOUNT);
        savingsAccount.setMonthlyMovementsLimit(DEFAULT_SAVINGS_ACCOUNT_MONTHLY_MOVEMENT_LIMIT);
        return savingsAccountRepository.save(savingsAccount)
                .map(savingsAccountMapper::mapToSavingsAccountResponse);
    }

    @Override
    public Mono<SavingsAccountResponse> updateSavingsAccount(String accountNumber, SavingsAccountUpdateRequest savingsAccountUpdateRequest) {
        return accountRepository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Account with account number " + accountNumber + " not found")))
                .flatMap(existingAccount -> {
                    SavingsAccount existingSavingsAccount = (SavingsAccount) existingAccount;
                    return accountRepository.save(updateSavingsAccountFromRequest(existingSavingsAccount, savingsAccountUpdateRequest));
                })
                .map(savingsAccountMapper::mapToSavingsAccountResponse);
    }

    public SavingsAccount updateSavingsAccountFromRequest(SavingsAccount existingSavingsAccount, SavingsAccountUpdateRequest savingsAccountUpdateRequest) {
        existingSavingsAccount.setCurrencyType(Account.CurrencyType.valueOf(savingsAccountUpdateRequest.getCurrencyType().name()));
        existingSavingsAccount.setBalance(savingsAccountUpdateRequest.getBalance());
        existingSavingsAccount.setStatus(Account.AccountStatus.valueOf(savingsAccountUpdateRequest.getStatus().name()));
        existingSavingsAccount.setCreatedAt(LocalDateTime.now());
        return existingSavingsAccount;
    }*/
}
