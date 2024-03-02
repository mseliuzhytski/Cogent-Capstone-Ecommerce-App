package com.cogent.ecommerce.repository;

import com.cogent.ecommerce.model.SalesItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesItemJpaRepository extends JpaRepository<SalesItem,Integer> {

    List<SalesItem> findByAccountId(int accountId);

}
