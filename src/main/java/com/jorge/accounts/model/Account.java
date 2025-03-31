package com.jorge.accounts.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "accounts")
@TypeAlias("account")
public abstract class Account {
    @Id
    private String id;
    private String accountNumber;
    private AccountType accountType;
    private CurrencyType currencyType;
    private BigDecimal balance;
    private AccountStatus status;
    private LocalDateTime createdAt;
    private String clientId;

    public enum AccountType{
        AHORRO, CORRIENTE, PLAZO_FIJO
    }

    public enum AccountStatus{
        ACTIVE, CLOSED, BLOCKED
    }

    public enum CurrencyType{
        PEN, USD
    }
}
