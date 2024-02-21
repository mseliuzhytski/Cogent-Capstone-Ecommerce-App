package com.cogent.ecommerce.repository;

import com.cogent.ecommerce.model.Category;
import com.cogent.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CategoryJpaRepository extends JpaRepository<Category,Integer> {

}
