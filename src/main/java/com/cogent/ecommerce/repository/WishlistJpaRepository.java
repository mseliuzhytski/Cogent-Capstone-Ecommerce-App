package com.cogent.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cogent.ecommerce.model.Account;
import com.cogent.ecommerce.model.Product;
import com.cogent.ecommerce.model.SalesItem;
import com.cogent.ecommerce.model.Wishlist;

@Repository
public interface WishlistJpaRepository extends JpaRepository<Wishlist, Integer>{

	//List<SalesItem> findByAccountId(int accountId);
	boolean existsByProduct (Product item);
	boolean existsByAccount (Account account);
}
