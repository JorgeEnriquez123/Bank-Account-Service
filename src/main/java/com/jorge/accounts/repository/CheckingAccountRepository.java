package com.jorge.accounts.repository;

import com.jorge.accounts.model.CheckingAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckingAccountRepository extends ReactiveMongoRepository<CheckingAccount, String> {
}
