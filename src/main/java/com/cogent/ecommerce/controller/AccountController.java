package com.cogent.ecommerce.controller;

import com.cogent.ecommerce.model.Account;
import com.cogent.ecommerce.model.Product;
import com.cogent.ecommerce.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping(value="/account/list")
    public List<Account> getAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping(value="/account/{id}")
    public ResponseEntity<Account> getProductById(@PathVariable int id) {
        Account a = null;
        try {
            a = accountService.getAccountById(id);
        } catch (DataIntegrityViolationException ex) {
            ex.printStackTrace();
        }
        if (a != null) {
            return ResponseEntity.status(HttpStatus.OK).body(a);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping(value="/account")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account a = null;
        try {
            a = accountService.addAccount(account);
        } catch (DataIntegrityViolationException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        if (a != null) {
            return ResponseEntity.status(HttpStatus.OK).body(a);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping(value="/account/{id}")
    public ResponseEntity<Account> deleteAccountById(@PathVariable int id) {
        Account a = null;
        try {
            a = accountService.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            ex.printStackTrace();
        }
        if (a != null) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping(value="/account/{id}")
    public ResponseEntity<Account> updateProduct(@PathVariable int id,
                                                 @RequestBody Account newAccount) {
        Account a = null;
        try {
            a = accountService.updateAccount(newAccount, id);
        } catch (DataIntegrityViolationException ex) {
            ex.printStackTrace();
        }
        if (a != null) {
            return ResponseEntity.status(HttpStatus.OK).body(a);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
