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

    private final AccountUtils accountUtils;
    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;

    private final Integer DEFAULT_MOVEMENT_AMOUNT = 0;
    // SAVINGS
    private final Integer DEFAULT_SAVINGS_ACCOUNT_MONTHLY_MOVEMENT_LIMIT = 30;      // EXAMPLE
    // CHECKINGS
    private final BigDecimal DEFAULT_MAINTENANCE_FEE = BigDecimal.valueOf(10.00);   // EXAMPLE

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
        if(accountRequest.getAccountType() != AccountRequest.AccountTypeEnum.CHECKING){
            return Mono.error(
                    new ResponseStatusException(HttpStatus.BAD_REQUEST, "Business customer can't create a " + accountRequest.getAccountType().name() + " account"));
        }
        return accountRepository.save(accountCreationSetUp(accountRequest));
    }

    @Override
    public Mono<Void> deleteAccountByAccountNumber(String accountNumber) {
        return accountRepository.deleteByAccountNumber(accountNumber);
    }

    @Override
    public Mono<AccountResponse> updateAccountByAccountNumber(String accountNumber, UpdateAccountRequest updateRequest) {
        return accountRepository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account with account number: " + accountNumber + " not found")))
                .flatMap(existingAccount ->
                        accountRepository.save(updateAccountFromRequest(existingAccount, updateRequest)))
                .map(accountMapper::mapToAccountResponse);
    }


    private Account updateAccountFromRequest(Account existingAccount, UpdateAccountRequest updateRequest) {
        existingAccount.setAccountNumber(updateRequest.getAccountNumber());
        existingAccount.setAccountType(Account.AccountType.valueOf(updateRequest.getAccountType().name()));
        existingAccount.setCurrencyType(Account.CurrencyType.valueOf(updateRequest.getCurrencyType().name()));
        existingAccount.setBalance(updateRequest.getBalance());
        existingAccount.setStatus(Account.AccountStatus.valueOf(updateRequest.getStatus().name()));
        existingAccount.setCustomerDni(updateRequest.getCustomerDni());
        existingAccount.setMovementsThisMonth(updateRequest.getMovementsThisMonth());

        switch (updateRequest.getAccountType()) {
            case SAVINGS:
                existingAccount.setMonthlyMovementsLimit(updateRequest.getMonthlyMovementsLimit());
                break;
            case CHECKING:
                existingAccount.setMaintenanceFee(updateRequest.getMaintenanceFee());
                existingAccount.setHolders(updateRequest.getHolders());
                existingAccount.setAuthorizedSigners(updateRequest.getAuthorizedSigners());
                break;
            case FIXED_TERM:
                existingAccount.setAllowedWithdrawal(updateRequest.getAllowedWithdrawal());
                break;
            default:
                throw new IllegalArgumentException("Unsupported update for type: " + updateRequest.getAccountType());
        }

        return existingAccount;
    }

    public Account accountCreationSetUp(AccountRequest accountRequest) {
        Account account = accountMapper.mapToAccount(accountRequest);
        account.setCreatedAt(LocalDateTime.now());
        account.setAccountNumber(accountUtils.generateAccountNumber());
        account.setMovementsThisMonth(DEFAULT_MOVEMENT_AMOUNT);

        switch (accountRequest.getAccountType()) {
            case SAVINGS:
                account.setMonthlyMovementsLimit(DEFAULT_SAVINGS_ACCOUNT_MONTHLY_MOVEMENT_LIMIT);
                break;
            case CHECKING:
                account.setMaintenanceFee(DEFAULT_MAINTENANCE_FEE);
                account.setHolders(List.of(accountRequest.getCustomerDni()));  // Add Customer's DNI as initial holder
                account.setAuthorizedSigners(account.getAuthorizedSigners());
                break;
            case FIXED_TERM:
                account.setAllowedWithdrawal(accountRequest.getAllowedWithdrawal());
                break;
            default:
                // Handle any unexpected account types
                throw new IllegalArgumentException("Unsupported account type: " + accountRequest.getAccountType());
        }
        return account;
    }
}
