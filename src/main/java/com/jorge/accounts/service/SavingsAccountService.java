package com.jorge.accounts.service;

import com.jorge.accounts.model.SavingsAccountRequest;
import com.jorge.accounts.model.SavingsAccountResponse;
import com.jorge.accounts.model.SavingsAccountUpdateRequest;
import reactor.core.publisher.Mono;

public interface SavingsAccountService {
    Mono<SavingsAccountResponse> createSavingsAccount(SavingsAccountRequest savingsAccountRequest);
    Mono<SavingsAccountResponse> updateSavingsAccount(SavingsAccountUpdateRequest savingsAccountUpdateRequest);
}
