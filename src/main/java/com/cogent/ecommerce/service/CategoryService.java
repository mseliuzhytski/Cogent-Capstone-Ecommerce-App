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
public class CategoryService {


    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    public List<Category> getAllCategories(){
        return categoryJpaRepository.findAll();
    }

    public Optional<Category> getCategoryById(int id){
        return categoryJpaRepository.findById(id);
    }

    public Category addCategory(Category category){
        return categoryJpaRepository.save(category);
    }

    public boolean deleteCategory(int id){

        Category categoryToRemove = categoryJpaRepository.findById(id).orElse(null);


        if(categoryToRemove!=null) {
            Set<Product> products = categoryToRemove.getProductList();
            for (Product product : products) {
                product.getCategoriesList().remove(categoryToRemove);
                productJpaRepository.save(product);
            }
            categoryJpaRepository.deleteById(id);
            return true;
        }

        return false;
    }

}
