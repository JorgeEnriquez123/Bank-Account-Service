package com.jorge.accounts.service;

import com.jorge.accounts.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {
    Flux<Account> getAllAccounts();
    Mono<Account> getAccountByAccountNumber(String accountNumber);
    Mono<Account> createAccount(Account account);
    Mono<Account> updateAccountByAccountNumber(String accountNumber, Account account);
    Mono<Void> deleteAccountByAccountNumber(String accountNumber);
}
