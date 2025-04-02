package com.jorge.accounts.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@Document(collection = "accounts")
public class Account {
    @Id
    private String id;
    private String accountNumber;
    private AccountType accountType;
    private CurrencyType currencyType;
    private BigDecimal balance;
    private AccountStatus status;
    private LocalDateTime createdAt;
    private String clientId;
    private Integer movementsThisMonth;

    //SavingsAccount fields
    private Integer monthlyMovementsLimit;

    //CheckingAccount fields
    private BigDecimal maintenanceFee;

    //FixedTerm fields
    private LocalDate allowedWithdrawal;

    public enum AccountType{
        SAVINGS, CHECKING, FIXED_TERM
    }

    public enum AccountStatus{
        ACTIVE, CLOSED, BLOCKED
    }

    public enum CurrencyType{
        PEN, USD
    }
}
