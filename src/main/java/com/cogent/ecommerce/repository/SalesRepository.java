package com.cogent.ecommerce.repository;

import com.cogent.ecommerce.model.SalesItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class SalesRepository {

    @PersistenceContext
    private EntityManager em;

    public SalesItem saveSalesItem(SalesItem salesItem) {
        em.persist(salesItem);
        return salesItem;
    }

}
