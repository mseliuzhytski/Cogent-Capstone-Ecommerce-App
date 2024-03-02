package com.cogent.ecommerce.repository;

import com.cogent.ecommerce.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseOrderJpaRepository extends JpaRepository<PurchaseOrder,Integer> {

    List<PurchaseOrder> findByAccountId(int accountId);

    Optional<PurchaseOrder> findByAccountIdAndProductId(int accountId,int productId);

    void deleteByAccountId(int accountId);

}
