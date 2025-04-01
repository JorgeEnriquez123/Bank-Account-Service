package com.jorge.accounts.service;

import com.jorge.accounts.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface AccountService {
    Flux<Account> getAllAccounts();
    Mono<Account> getAccountByAccountNumber(String accountNumber);
    Mono<Account> createAccount(Account account);
    Mono<Account> updateAccountByAccountNumber(String accountNumber, Account account);
    Mono<Void> deleteAccountByAccountNumber(String accountNumber);

    /*Mono<Account> depositByAccountNumber(String accountNumber, DepositRequest depositRequest);
    Mono<Account> withdrawByAccountNumber(String accountNumber, WithdrawalRequest withdrawalRequest);
    Mono<BalanceResponse> getBalanceByAccountNumber(String accountNumber);
    Flux<TransactionResponse> getTransactionsByAccountNumber(String accountNumber);*/
}
