package com.cogent.ecommerce.service;

import com.cogent.ecommerce.model.Account;
import com.cogent.ecommerce.model.Discount;
import com.cogent.ecommerce.repository.AccountJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountJpaRepository accountJpaRepository;

    public Optional<Account> getAccountById(int id){
        return accountJpaRepository.findById(id);
    }

    public Account addAccount(Account account){
        return accountJpaRepository.save(account);
    }

    public Account addDiscountToAccount(int id, Discount discount){

        Account account = accountJpaRepository.findById(id).orElse(null);

        if(account!=null){
            account.setDiscount(discount);
            return accountJpaRepository.save(account);
        }
        return null;
    }


}
