package com.jorge.accounts.service;

import com.jorge.accounts.model.*;
import reactor.core.publisher.Mono;

public interface CheckingAccountService {
    Mono<CheckingAccountResponse> createCheckingAccount(CheckingAccountRequest checkingAccountRequest);
    Mono<CheckingAccountResponse> updateCheckingAccount(CheckingAccountUpdateRequest checkingAccountUpdateRequest);
}
