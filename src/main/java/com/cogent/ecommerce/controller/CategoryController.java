package com.cogent.ecommerce.controller;

import com.cogent.ecommerce.model.Category;
import com.cogent.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/getCategory")
    public List<Category> getCategory() {
        return categoryService.getAllCategories();
    }
    @PostMapping("/getCategory")
    public ResponseEntity<?> addCategory(@RequestBody Category category) {
        if(categoryService.getCategoryById(category.getId()).isPresent()){
            return ResponseEntity.badRequest().body("Category Exists");
        }
        categoryService.addCategory(category);
        return ResponseEntity.ok().body(category);
    }

    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable int id){

        if(categoryService.getCategoryById(id).isEmpty()){
            return ResponseEntity.badRequest().body("Category does not exist");
        }
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().body("Deleted Category");

    }


}
