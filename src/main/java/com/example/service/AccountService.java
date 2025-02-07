package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    /**
     * Add a new account to the database.
     * 
     * The registration will be successful if and only if the username is not 
     * blank, the password is at least 4 characters long, and an Account with 
     * that username does not already exist
     *
     * @param account an object representing a new account.
     * @return the newly added account if the add operation was successful, including the account_id. 
     */
    public Account insertAccount(Account account) {
        if (account.getUsername().length() > 0 && account.getPassword().length() >= 4) {
            return accountRepository.save(account);
        }

        return null;
    }


    /**
     * Retrieve a specific account using its username.
     *
     * @param username an account username
     * @return the account object, null if username does not exist
     */
    public Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }
    

    /**
     * Verify Login given account object without account id.
     * 
     * The login will be successful if and only if the username and password provided 
     * in the request body JSON match a real account existing on the database. 
     *
     * @param username an account username
     * @return the account object, null if username does not exist
     */
    public Account login(Account account) {
        Account acc = accountRepository.findByUsername(account.getUsername());
        if (acc != null && acc.getUsername().equals(account.getUsername()) 
                        && acc.getPassword().equals(account.getPassword())) {
            return acc;
        }

        return null;
    }
}
