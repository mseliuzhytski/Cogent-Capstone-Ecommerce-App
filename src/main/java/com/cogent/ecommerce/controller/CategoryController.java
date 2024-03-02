package com.cogent.ecommerce.controller;

import com.cogent.ecommerce.model.Category;
import com.cogent.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {

    //TODO: FOR SECURITY THESE NEED TO ONLY BE ACCESSIBLE TO ADMINS
    @Autowired
    CategoryService categoryService;

    @GetMapping("/getCategory")
    public List<Category> getCategory() {
        return categoryService.getAllCategories();
    }
    @PostMapping("/addCategory")
    public ResponseEntity<?> addCategory(@RequestBody Category category) {
        if(categoryService.getCategoryById(category.getId()).isPresent()){
            return ResponseEntity.badRequest().body("Category Exists");
        }

        return ResponseEntity.ok().body(categoryService.addCategory(category));
    }

    //will not work until bidirectional many to many added-->next task so we can remove categories and it will remove product ref to that cat;
    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable int id){

        return ResponseEntity.ok().body(categoryService.deleteCategory(id));

    }


}
