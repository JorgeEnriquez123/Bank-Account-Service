package com.jorge.accounts.delegateImpl;

import com.jorge.accounts.api.AccountsApiDelegate;
import com.jorge.accounts.model.*;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class AccountApiDelegateImpl implements AccountsApiDelegate {
    @Override
    public Mono<Void> deleteAccountByAccountNumber(String accountNumber, ServerWebExchange exchange) {
        return AccountsApiDelegate.super.deleteAccountByAccountNumber(accountNumber, exchange);
    }

    @Override
    public Mono<AccountResponse> depositByAccountNumber(String accountNumber, Mono<DepositRequest> depositRequest, ServerWebExchange exchange) {
        return AccountsApiDelegate.super.depositByAccountNumber(accountNumber, depositRequest, exchange);
    }

    @Override
    public Mono<BalanceResponse> getAccountBalanceByAccountNumber(String accountNumber, ServerWebExchange exchange) {
        return AccountsApiDelegate.super.getAccountBalanceByAccountNumber(accountNumber, exchange);
    }

    @Override
    public Mono<AccountResponse> getAccountByAccountNumber(String accountNumber, ServerWebExchange exchange) {
        return AccountsApiDelegate.super.getAccountByAccountNumber(accountNumber, exchange);
    }

    @Override
    public Flux<AccountResponse> getAllAccounts(ServerWebExchange exchange) {
        return AccountsApiDelegate.super.getAllAccounts(exchange);
    }

    @Override
    public Flux<TransactionResponse> getTransactionsByAccountNumber(String accountNumber, ServerWebExchange exchange) {
        return AccountsApiDelegate.super.getTransactionsByAccountNumber(accountNumber, exchange);
    }

    @Override
    public Mono<AccountResponse> withdrawByAccountNumber(String accountNumber, Mono<WithdrawalRequest> withdrawalRequest, ServerWebExchange exchange) {
        return AccountsApiDelegate.super.withdrawByAccountNumber(accountNumber, withdrawalRequest, exchange);
    }
}
