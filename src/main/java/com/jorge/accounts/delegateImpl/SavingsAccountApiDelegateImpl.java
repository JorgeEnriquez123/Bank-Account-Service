package com.jorge.accounts.delegateImpl;

import com.jorge.accounts.api.AccountsApiDelegate;
import com.jorge.accounts.model.SavingsAccountRequest;
import com.jorge.accounts.model.SavingsAccountResponse;
import com.jorge.accounts.model.SavingsAccountUpdateRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class SavingsAccountApiDelegateImpl implements AccountsApiDelegate {
    @Override
    public Mono<SavingsAccountResponse> createSavingsAccount(Mono<SavingsAccountRequest> savingsAccountRequest, ServerWebExchange exchange) {
        return AccountsApiDelegate.super.createSavingsAccount(savingsAccountRequest, exchange);
    }

    @Override
    public Mono<SavingsAccountResponse> updateSavingsAccount(String accountNumber, Mono<SavingsAccountUpdateRequest> savingsAccountUpdateRequest, ServerWebExchange exchange) {
        return AccountsApiDelegate.super.updateSavingsAccount(accountNumber, savingsAccountUpdateRequest, exchange);
    }
}
