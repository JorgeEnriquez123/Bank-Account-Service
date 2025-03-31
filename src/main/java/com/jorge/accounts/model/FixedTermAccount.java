package com.jorge.accounts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;

import java.time.LocalDate;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TypeAlias("fixedTermAccount")
public class FixedTermAccount extends Account{
    private LocalDate allowedWithdrawal;
}
