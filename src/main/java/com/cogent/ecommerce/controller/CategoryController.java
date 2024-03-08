package com.cogent.ecommerce.controller;

import com.cogent.ecommerce.model.Category;
import com.cogent.ecommerce.security.AuthService;
import com.cogent.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin
public class CategoryController {

    //TODO: FOR SECURITY THESE NEED TO ONLY BE ACCESSIBLE TO ADMINS--DONE
    @Autowired
    CategoryService categoryService;

    @Autowired
    AuthService authService;

    @GetMapping("/getCategory")
    public List<Category> getCategory() {
        return categoryService.getAllCategories();
    }
    @PostMapping("/addCategory")
    public ResponseEntity<?> addCategory(@RequestHeader("Authorization") String authHeader,@RequestBody Category category) {

        String token = authHeader.substring(7);
        if(!authService.checkIfAdmin(token)){
            return ResponseEntity.badRequest().body("Not Authorized");
        }

        if(categoryService.getCategoryByName(category.getName()).isPresent()){
            return ResponseEntity.badRequest().body("Category Exists");
        }
        if(categoryService.getCategoryById(category.getId()).isPresent()){
            return ResponseEntity.badRequest().body("Category Exists");
        }

        return ResponseEntity.ok().body(categoryService.addCategory(category));
    }

    //will not work until bidirectional many to many added-->next task so we can remove categories and it will remove product ref to that cat;
    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<?> deleteCategory(@RequestHeader("Authorization") String authHeader,@PathVariable int id){
        String token = authHeader.substring(7);
        if(!authService.checkIfAdmin(token)){
            return ResponseEntity.badRequest().body("Not Authorized");
        }
        return ResponseEntity.ok().body(categoryService.deleteCategory(id));

    }

    @PutMapping("/updateCategory/{id}/{name}")
    public ResponseEntity<?> updateCategory(@RequestHeader("Authorization") String authHeader,@PathVariable int id, @PathVariable String name){
        String token = authHeader.substring(7);
        if(!authService.checkIfAdmin(token)){
            return ResponseEntity.badRequest().body("Not Authorized");
        }
        return ResponseEntity.ok().body(categoryService.updateCategory(id, name));
    }

}
