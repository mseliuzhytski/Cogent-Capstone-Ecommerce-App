package com.cogent.ecommerce.repository;

import com.cogent.ecommerce.model.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class AccountRepository {

    @PersistenceContext
    private EntityManager em;

    public Account saveAccount(Account account) {
        em.persist(account);
        return account;
    }
    public Account getById(int id) {
        Account account = em.find(Account.class, id);
        return account;
    }


}
