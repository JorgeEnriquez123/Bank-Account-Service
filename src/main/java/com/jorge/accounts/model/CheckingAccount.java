package com.jorge.accounts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;

import java.math.BigDecimal;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TypeAlias("checkingAccount")
public class CheckingAccount extends Account{
    private BigDecimal maintenanceFee;
}
