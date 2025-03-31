package com.jorge.accounts.repository;

import com.jorge.accounts.model.FixedTermAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FixedTermRepository extends ReactiveMongoRepository<FixedTermAccount, String> {
}
