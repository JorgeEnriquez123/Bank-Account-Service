package com.jorge.accounts.service.impl;

import com.jorge.accounts.mapper.AccountMapper;
import com.jorge.accounts.model.*;
import com.jorge.accounts.repository.AccountRepository;
import com.jorge.accounts.service.OperationService;
import com.jorge.accounts.webclient.client.TransactionClient;
import com.jorge.accounts.webclient.model.TransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {
    private final TransactionClient transactionClient;
    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;

    @Override
    public Mono<AccountResponse> depositByAccountNumber(String accountNumber, DepositRequest depositRequest) {
        return accountRepository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account with account number: " + accountNumber + " not found")))
                .flatMap(account -> {
                    if(account.getCurrencyType() != Account.CurrencyType.valueOf(depositRequest.getCurrencyType().name())){
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "The deposit's currency does not match the account's"));
                    }
                    // Process Movements limit and Update fee status if applicable
                    return processAccountMovement(account);
                })
                .flatMap(account -> {
                    BigDecimal depositAmount = depositRequest.getAmount();

                    // Apply commission fee if active
                    if (account.getIsCommissionFeeActive()) {
                        depositAmount = depositAmount.subtract(account.getMovementCommissionFee());

                        if(depositAmount.compareTo(BigDecimal.ZERO) < 0){
                            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                    "Commission fee is higher than deposit amount"));
                        }
                    }
                    // Update balance
                    account.setBalance(account.getBalance().add(depositAmount));

                    return accountRepository.save(account)
                            .flatMap(savedAccount -> {
                                TransactionRequest transactionRequest = new TransactionRequest();
                                transactionRequest.setAccountNumber(accountNumber);
                                transactionRequest.setAmount(depositRequest.getAmount());
                                transactionRequest.setCurrencyType(TransactionRequest.CurrencyType.valueOf(depositRequest.getCurrencyType().name()));
                                transactionRequest.setTransactionType(TransactionRequest.TransactionType.DEPOSIT);
                                transactionRequest.setStatus(TransactionRequest.TransactionStatus.COMPLETED);
                                transactionRequest.setDescription("Deposit to account " + accountNumber);
                                if(savedAccount.getIsCommissionFeeActive()) transactionRequest.setFee(savedAccount.getMovementCommissionFee());
                                // No credit, since it's a deposit
                                transactionRequest.setCreditId(null);

                                return transactionClient.createTransaction(transactionRequest)
                                        .thenReturn(savedAccount);
                            });
                })
                .map(accountMapper::mapToAccountResponse);
    }

    @Override
    public Mono<AccountResponse> withdrawByAccountNumber(String accountNumber, WithdrawalRequest withdrawalRequest) {
        return accountRepository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account with account number: " + accountNumber + " not found")))
                .flatMap(account -> {
                    if(account.getCurrencyType() != Account.CurrencyType.valueOf(withdrawalRequest.getCurrencyType().name())){
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "The withdrawal's currency does not match the account's"));
                    }
                    // Process Movements limit and Update fee status if applicable
                    return processAccountMovement(account);
                })
                .flatMap(account -> {
                    BigDecimal withdrawalAmount = withdrawalRequest.getAmount();

                    // Apply commission fee if active
                    if (account.getIsCommissionFeeActive()) {
                        withdrawalAmount = withdrawalAmount.add(account.getMovementCommissionFee()); // Add to withdrawal, since it's taken from balance
                    }

                    if(account.getBalance().compareTo(withdrawalAmount) < 0){
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Insufficient balance for the withdrawal"));
                    }

                    if (account.getAccountType() == Account.AccountType.FIXED_TERM) {
                        if (account.getAllowedWithdrawal().isAfter(LocalDate.now())) {
                            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                    "Withdrawal not allowed until " + account.getAllowedWithdrawal()));
                        }
                    }

                    account.setBalance(account.getBalance().subtract(withdrawalAmount));

                    return accountRepository.save(account)
                            .flatMap(savedAccount -> {
                                TransactionRequest transactionRequest = new TransactionRequest();
                                transactionRequest.setAccountNumber(accountNumber);
                                transactionRequest.setAmount(withdrawalRequest.getAmount());
                                transactionRequest.setCurrencyType(TransactionRequest.CurrencyType.valueOf(withdrawalRequest.getCurrencyType().name()));
                                transactionRequest.setTransactionType(TransactionRequest.TransactionType.WITHDRAWAL);
                                transactionRequest.setStatus(TransactionRequest.TransactionStatus.COMPLETED);
                                transactionRequest.setDescription("Withdrawal from account " + accountNumber);
                                if(savedAccount.getIsCommissionFeeActive()) transactionRequest.setFee(savedAccount.getMovementCommissionFee());
                                // No credit, since it's a withdrawal
                                transactionRequest.setCreditId(null);

                                return transactionClient.createTransaction(transactionRequest)
                                        .thenReturn(savedAccount);
                            });
                })
                .map(accountMapper::mapToAccountResponse);
    }

    private Mono<Account> processAccountMovement(Account account) {
        // Specific validations based on account type
        if (account.getAccountType() == Account.AccountType.SAVINGS) {
            if (account.getMovementsThisMonth() >= account.getMonthlyMovementsLimit()) {
                return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Monthly movement limit reached for savings account"));
            }
        }
        // Increase movement count
        account.setMovementsThisMonth(account.getMovementsThisMonth() + 1);

        // Check if commission fee should be activated
        if (!account.getIsCommissionFeeActive() && account.getMovementsThisMonth().equals(account.getMaxMovementsFeeFreeThisMonth())) {
            account.setIsCommissionFeeActive(true);
        }
        return Mono.just(account);
    }

    @Override
    public Mono<BalanceResponse> getBalanceByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account with account number: " + accountNumber + " not found")))
                .map(account -> {
                    BalanceResponse balanceResponse = new BalanceResponse();
                    balanceResponse.setAccountNumber(account.getAccountNumber());
                    balanceResponse.setBalance(account.getBalance());
                    return balanceResponse;
                });
    }

    @Override
    public Mono<BalanceResponse> updateBalanceByAccountNumber(String accountNumber, BigDecimal balance) {
        return accountRepository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account with account number: " + accountNumber + " not found")))
                .flatMap(account -> {
                    account.setBalance(balance);
                    return accountRepository.save(account);
                })
                .map(account -> {
                    BalanceResponse balanceResponse = new BalanceResponse();
                    balanceResponse.setAccountType(BalanceResponse.AccountTypeEnum.valueOf(account.getAccountType().name()));
                    balanceResponse.setCurrencyType(BalanceResponse.CurrencyTypeEnum.valueOf(account.getCurrencyType().name()));
                    balanceResponse.setAccountNumber(account.getAccountNumber());
                    balanceResponse.setBalance(account.getBalance());
                    return balanceResponse;
                });
    }

    @Override
    public Flux<TransactionResponse> getTransactionsByAccountNumber(String accountNumber) {
        return transactionClient.getTransactionsByAccountNumber(accountNumber);
    }
}
