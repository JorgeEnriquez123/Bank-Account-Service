package com.jorge.accounts.delegateImpl;

import com.jorge.accounts.api.AccountsApiDelegate;
import com.jorge.accounts.model.CheckingAccountRequest;
import com.jorge.accounts.model.CheckingAccountResponse;
import com.jorge.accounts.model.CheckingAccountUpdateRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CheckingAccountApiDelegateImpl implements AccountsApiDelegate {
    @Override
    public Mono<CheckingAccountResponse> createCheckingAccount(Mono<CheckingAccountRequest> checkingAccountRequest, ServerWebExchange exchange) {
        return AccountsApiDelegate.super.createCheckingAccount(checkingAccountRequest, exchange);
    }

    @Override
    public Mono<CheckingAccountResponse> updateCheckingAccount(String accountNumber, Mono<CheckingAccountUpdateRequest> checkingAccountUpdateRequest, ServerWebExchange exchange) {
        return AccountsApiDelegate.super.updateCheckingAccount(accountNumber, checkingAccountUpdateRequest, exchange);
    }
}
