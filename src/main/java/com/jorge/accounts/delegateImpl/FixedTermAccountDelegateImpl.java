package com.jorge.accounts.delegateImpl;

import com.jorge.accounts.api.AccountsApiDelegate;
import com.jorge.accounts.model.FixedTermAccountRequest;
import com.jorge.accounts.model.FixedTermAccountResponse;
import com.jorge.accounts.model.FixedTermAccountUpdateRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class FixedTermAccountDelegateImpl implements AccountsApiDelegate {
    @Override
    public Mono<FixedTermAccountResponse> createFixedTermAccount(Mono<FixedTermAccountRequest> fixedTermAccountRequest, ServerWebExchange exchange) {
        return AccountsApiDelegate.super.createFixedTermAccount(fixedTermAccountRequest, exchange);
    }

    @Override
    public Mono<FixedTermAccountResponse> updateFixedTermAccount(String accountNumber, Mono<FixedTermAccountUpdateRequest> fixedTermAccountUpdateRequest, ServerWebExchange exchange) {
        return AccountsApiDelegate.super.updateFixedTermAccount(accountNumber, fixedTermAccountUpdateRequest, exchange);
    }
}
