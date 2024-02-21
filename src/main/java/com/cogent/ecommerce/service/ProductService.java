package com.cogent.ecommerce.service;

import com.cogent.ecommerce.model.Category;
import com.cogent.ecommerce.model.Product;
import com.cogent.ecommerce.repository.CategoryJpaRepository;
import com.cogent.ecommerce.repository.ProductJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;


    public List<Product> getAllProducts(){
        return productJpaRepository.findAll();
    }

    public Optional<Product> getProductById(int id){
        return productJpaRepository.findById(id);
    }

    public Product addProducts(Product product){
        return productJpaRepository.save(product);
    }

    public void deleteProduct(int id){
        productJpaRepository.deleteById(id);
    }

    public boolean addCategoryToProduct(int productId, int categoryId){
        Product product = productJpaRepository.findById(productId).orElse(null);
        Category category = categoryJpaRepository.findById(categoryId).orElse(null);

        if(product!=null && category!=null){
            product.getCategoriesList().add(category);
            productJpaRepository.save(product);
            return true;
        }
        return false;
    }

    public void deleteCategoryFromProduct(int productId, int categoryId){
        Product product = productJpaRepository.findById(productId).orElse(null);
        Category category = categoryJpaRepository.findById(categoryId).orElse(null);

        if(product!=null && category!=null){
            product.getCategoriesList().remove(category);
            productJpaRepository.save(product);
        }
    }

}
