package com.cogent.ecommerce.repository;

import com.cogent.ecommerce.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountJpaRepository extends JpaRepository<Discount, Integer> {
    public Discount getDiscountByDiscountCode(String discountCode);
}
