package com.jorge.accounts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TypeAlias("savingsAccount")
public class SavingsAccount extends Account{
    private Integer movementsThisMonth;
    private Integer monthlyMovementsLimit;
}
