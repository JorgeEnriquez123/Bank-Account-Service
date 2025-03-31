package com.jorge.accounts.repository;

import com.jorge.accounts.model.SavingsAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsAccountRepository extends ReactiveMongoRepository<SavingsAccount, String> {
}
