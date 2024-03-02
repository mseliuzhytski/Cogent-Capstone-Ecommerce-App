package com.cogent.ecommerce.repository;

import com.cogent.ecommerce.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountJpaRepository extends JpaRepository<Account, Integer> {

    public Account getAccountByUsername(String username);

}
