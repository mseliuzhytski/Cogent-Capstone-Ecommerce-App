package com.cogent.ecommerce.repository;

import com.cogent.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCrudRepository extends JpaRepository<Product, Integer> {

}
