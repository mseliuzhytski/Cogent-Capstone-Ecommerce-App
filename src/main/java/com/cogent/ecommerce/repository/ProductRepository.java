package com.cogent.ecommerce.repository;

import com.cogent.ecommerce.model.Product;
import com.cogent.ecommerce.model.PurchaseOrder;
import com.cogent.ecommerce.model.Wishlist;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public class ProductRepository {

    @Autowired
    private ProductJpaRepository dao;
    @PersistenceContext
    private EntityManager em;

    public List<Product> getAllProducts() {
        return dao.findAll();
    }

    public Product deleteProductById(int id) {
        Optional<Product> product = dao.findById(id);
        if (product.isPresent()) {
            em.remove(product.get());
            return product.get();
        } else {
            return null;
        }
    }

    public Product saveProduct(Product product) {
        em.persist(product);
        return product;
    }

    public void saveProducts(List<Product> products) {
        for (Product product : products) {
            em.persist(product);
        }
    }

    public Product updateProduct(Product product, int id) {
        product.setId(id);
        em.merge(product);
        return product;
    }

    public PurchaseOrder savePurchaseOrder(PurchaseOrder purchaseOrder) {
        try {
            em.persist(purchaseOrder);
        } catch (DataAccessException ex) {
            System.err.println(ex.getMessage());
            return null;
        }
        return purchaseOrder;
    }

    public Wishlist saveWishlist(Wishlist wishlist) {
        try {
            em.persist(wishlist);
            em.flush();
        } catch (DataAccessException ex) {
            System.err.println(ex.getMessage());
            return null;
        }
        return wishlist;
    }

    public Product getById(int id) {
        Product product = em.find(Product.class, id);
        return product;
    }

}
