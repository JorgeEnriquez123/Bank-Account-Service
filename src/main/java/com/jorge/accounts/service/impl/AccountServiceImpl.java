package com.jorge.accounts.service.impl;

import com.jorge.accounts.webclient.client.CustomerClient;
import com.jorge.accounts.webclient.model.CustomerResponse;
import com.jorge.accounts.mapper.AccountMapper;
import com.jorge.accounts.model.*;
import com.jorge.accounts.repository.AccountRepository;
import com.jorge.accounts.service.AccountService;
import com.jorge.accounts.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final CustomerClient customerClient;
    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;
    private final AccountUtils accountUtils;

    @Override
    public Flux<AccountResponse> getAllAccounts() {
        return accountRepository.findAll()
                .map(accountMapper::mapToAccountResponse);
    }

    @Override
    public Mono<AccountResponse> getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account with account number: " + accountNumber + " not found")))
                .map(accountMapper::mapToAccountResponse);
    }

    @Override
    public Mono<AccountResponse> createAccount(AccountRequest accountRequest) {
        Mono<CustomerResponse> customerResponse = customerClient.getCustomerByDni(accountRequest.getCustomerDni());
        return customerResponse.flatMap(customer ->
                switch (customer.getCustomerType()) {
                    case PERSONAL -> personalCustomerValidation(customer, accountRequest);
                    case BUSINESS -> businessCustomerValidation(accountRequest);
                    default -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported customer type"));
                }).map(accountMapper::mapToAccountResponse);
    }

    private Mono<Account> personalCustomerValidation(CustomerResponse customer, AccountRequest accountRequest) {
        return accountRepository.findByCustomerDniAndAccountType(customer.getDni(),
                        Account.AccountType.valueOf(accountRequest.getAccountType().name()))
                .flatMap(account -> Mono.<Account>error(new ResponseStatusException(HttpStatus.CONFLICT,
                        "Customer with dni: " + customer.getDni() + " already has a " + accountRequest.getAccountType().name() + " account")))
                .switchIfEmpty(Mono.defer(() -> {
                    Account newAccount = accountCreationSetUp(accountRequest);
                    return accountRepository.save(newAccount);
                }));
    }

    private Mono<Account> businessCustomerValidation(AccountRequest accountRequest) {
        if (accountRequest.getAccountType() != AccountRequest.AccountTypeEnum.CHECKING) {
            return Mono.error(
                    new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Business customer can't create a " + accountRequest.getAccountType().name() + " account"));
        }
        return accountRepository.save(accountCreationSetUp(accountRequest));
    }

    @Override
    public Mono<Void> deleteAccountByAccountNumber(String accountNumber) {
        return accountRepository.deleteByAccountNumber(accountNumber);
    }

    @Override
    public Mono<AccountResponse> updateAccountByAccountNumber(String accountNumber, AccountRequest accountRequest) {
        return accountRepository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account with account number: " + accountNumber + " not found")))
                .flatMap(existingAccount -> {
                    existingAccount.setAccountType(Account.AccountType.valueOf(accountRequest.getAccountType().name()));
                    existingAccount.setCurrencyType(Account.CurrencyType.valueOf(accountRequest.getCurrencyType().name()));
                    existingAccount.setBalance(accountRequest.getBalance());
                    existingAccount.setStatus(Account.AccountStatus.valueOf(accountRequest.getStatus().name()));
                    existingAccount.setCustomerDni(accountRequest.getCustomerDni());
                    existingAccount.setMovementsThisMonth(accountRequest.getMovementsThisMonth());
                    existingAccount.setMaxMovementsThisMonth(accountRequest.getMaxMovementsThisMonth());
                    existingAccount.setMovementCommissionFee(accountRequest.getMovementCommissionFee());

                    switch (accountRequest.getAccountType()) {
                        case SAVINGS:
                            existingAccount.setMonthlyMovementsLimit(accountRequest.getMonthlyMovementsLimit());
                            break;
                        case CHECKING:
                            existingAccount.setMaintenanceFee(accountRequest.getMaintenanceFee());
                            existingAccount.setHolders(accountRequest.getHolders());
                            existingAccount.setAuthorizedSigners(accountRequest.getAuthorizedSigners());
                            break;
                        case FIXED_TERM:
                            existingAccount.setAllowedWithdrawal(accountRequest.getAllowedWithdrawal());
                            break;
                        default:
                            return Mono.error(new IllegalArgumentException("Unsupported update for type: " + accountRequest.getAccountType()));
                    }

                    return accountRepository.save(existingAccount);
                })
                .map(accountMapper::mapToAccountResponse);
    }

    public Account accountCreationSetUp(AccountRequest accountRequest) {
        Account account = accountMapper.mapToAccount(accountRequest);

        account.setAccountNumber(accountUtils.generateAccountNumber());
        account.setCreatedAt(LocalDateTime.now());

        return account;
    }
}
