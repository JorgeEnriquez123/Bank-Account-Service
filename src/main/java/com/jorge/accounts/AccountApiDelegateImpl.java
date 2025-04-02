package com.jorge.accounts;

import com.jorge.accounts.api.AccountsApiDelegate;
import com.jorge.accounts.model.*;
import com.jorge.accounts.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AccountApiDelegateImpl implements AccountsApiDelegate {
    private final AccountService accountService;

    @Override
    public Mono<AccountResponse> createAccount(Mono<AccountRequest> accountRequest, ServerWebExchange exchange) {
        return accountRequest.flatMap(accountService::createAccount);
    }

    @Override
    public Mono<Void> deleteAccountByAccountNumber(String accountNumber, ServerWebExchange exchange) {
        return accountService.deleteAccountByAccountNumber(accountNumber);
    }

    @Override
    public Mono<AccountResponse> getAccountByAccountNumber(String accountNumber, ServerWebExchange exchange) {
        return accountService.getAccountByAccountNumber(accountNumber);
    }

    @Override
    public Flux<AccountResponse> getAllAccounts(ServerWebExchange exchange) {
        return accountService.getAllAccounts();
    }

    @Override
    public Mono<AccountResponse> updateAccountByAccountNumber(String accountNumber, Mono<UpdateAccountRequest> updateAccountRequest, ServerWebExchange exchange) {
        return updateAccountRequest.flatMap(request ->
                accountService.updateAccountByAccountNumber(accountNumber, request));
    }

    /*@Override
    public Mono<AccountResponse> depositByAccountNumber(String accountNumber, Mono<DepositRequest> depositRequest, ServerWebExchange exchange) {
        return depositRequest.flatMap(request ->
                operationService.depositByAccountNumber(accountNumber, request));
    }

    @Override
    public Mono<AccountResponse> withdrawByAccountNumber(String accountNumber, Mono<WithdrawalRequest> withdrawalRequest, ServerWebExchange exchange) {
        return withdrawalRequest.flatMap(request ->
                operationService.withdrawByAccountNumber(accountNumber, request));
    }

    @Override
    public Mono<BalanceResponse> getBalanceByAccountNumber(String accountNumber, ServerWebExchange exchange) {
        return operationService.getBalanceByAccountNumber(accountNumber);
    }

    @Override
    public Mono<BalanceResponse> updateBalanceByAccountNumber(String accountNumber, Mono<BalanceUpdateRequest> balanceUpdateRequest, ServerWebExchange exchange) {
        return balanceUpdateRequest.flatMap(request ->
                operationService.updateBalanceByAccountNumber(accountNumber, request.getBalance()));
    }

    @Override
    public Flux<TransactionResponse> getTransactionsByAccountNumber(String accountNumber, ServerWebExchange exchange) {
        return operationService.getTransactionsByAccountNumber(accountNumber);
    }
     */
}
