package com.cogent.ecommerce.service;

import com.cogent.ecommerce.model.Category;
import com.cogent.ecommerce.model.Product;
import com.cogent.ecommerce.repository.CategoryJpaRepository;
import com.cogent.ecommerce.repository.ProductJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public boolean deleteProduct(int id){

        Product productToRemove = productJpaRepository.findById(id).orElse(null);

        if(productToRemove!=null){

            Set<Category> categories = productToRemove.getCategoriesList();
            for(Category category:categories){
                category.getProductList().remove(productToRemove);
                categoryJpaRepository.save(category);
            }
            productJpaRepository.deleteById(id);
            return true;
        }

        return false;
    }

    public boolean addCategoryToProduct(int productId, int categoryId){
        Product product = productJpaRepository.findById(productId).orElse(null);
        Category category = categoryJpaRepository.findById(categoryId).orElse(null);

        if(product!=null && category!=null){
            product.getCategoriesList().add(category);
            category.getProductList().add(product);
            productJpaRepository.save(product);
            categoryJpaRepository.save(category);
            return true;
        }
        return false;
    }

    public boolean deleteCategoryFromProduct(int productId, int categoryId){
        Product product = productJpaRepository.findById(productId).orElse(null);
        Category category = categoryJpaRepository.findById(categoryId).orElse(null);

        if(product!=null && category!=null){
            product.getCategoriesList().remove(category);
            category.getProductList().remove(product);
            productJpaRepository.save(product);
            categoryJpaRepository.save(category);
            return true;
        }

        return false;
    }

}
