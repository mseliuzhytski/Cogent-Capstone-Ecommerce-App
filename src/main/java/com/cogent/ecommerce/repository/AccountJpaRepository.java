package com.cogent.ecommerce.repository;

import com.cogent.ecommerce.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountJpaRepository extends JpaRepository<Account, Integer> {
    public Account getAccountByUsername(String username);
}
