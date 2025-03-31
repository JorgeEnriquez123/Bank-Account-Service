package com.jorge.accounts;

import com.jorge.accounts.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccountsApplication {
	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}
}
