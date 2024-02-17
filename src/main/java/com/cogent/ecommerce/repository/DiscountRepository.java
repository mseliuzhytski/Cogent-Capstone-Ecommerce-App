package com.cogent.ecommerce.repository;

import com.cogent.ecommerce.model.Discount;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class DiscountRepository {

    @PersistenceContext
    private EntityManager em;

    public Discount saveDiscount(Discount discount) {
        em.persist(discount);
        return discount;
    }

}
