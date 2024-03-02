package com.cogent.ecommerce.service;

import com.cogent.ecommerce.model.Account;
import com.cogent.ecommerce.model.Product;
import com.cogent.ecommerce.repository.AccountJpaRepository;
import com.cogent.ecommerce.repository.ProductJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountJpaRepository accountRepo;

    public List<Account> getAllAccounts(){
        return accountRepo.findAll();
    }

    public Account getAccountById(int id){
        return accountRepo.findById(id).get();
    }

    public Account addAccount(Account account){
        return accountRepo.save(account);
    }

    public Account getAccountByUsername(String username) {
        return accountRepo.getAccountByUsername(username);
    }

    public Account deleteById(int id) {
        Optional<Account> account = accountRepo.findById(id);
        if (account.isPresent()) {
            accountRepo.delete(account.get());
            return account.get();
        } else {
            return null;
        }
    }

    public Account updateAccount(Account account, int id) {
        Optional<Account> oldAccount = accountRepo.findById(id);
        if (oldAccount.isPresent()) {
            account.setId(id);
            accountRepo.save(account);
            return account;
        } else {
            return null;
        }
    }


}
