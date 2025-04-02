package com.jorge.accounts.service;

import com.jorge.accounts.model.*;
import reactor.core.publisher.Mono;

public interface FixedTermAccountService {
    Mono<FixedTermAccountResponse> createFixedTermAccount(FixedTermAccountRequest fixedTermAccountRequest);
    Mono<FixedTermAccountResponse> updateFixedTermAccount(FixedTermAccountUpdateRequest fixedTermAccountUpdateRequest);
}
