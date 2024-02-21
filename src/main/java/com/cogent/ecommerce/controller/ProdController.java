package com.cogent.ecommerce.controller;

import com.cogent.ecommerce.model.Category;
import com.cogent.ecommerce.model.Product;
import com.cogent.ecommerce.repository.ProductJpaRepository;
import com.cogent.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProdController {


    @Autowired
    ProductService productService;

    //admin usage in final version
    //currently just to add products using postman
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody Product product){

        if(productService.getProductById(product.getId()).isPresent()){
            return ResponseEntity.badRequest().body("Product Exists");
        }
        productService.addProducts(product);
        return ResponseEntity.ok().body(product);
    }

    @PutMapping("/addCategory/{productId}/{categoryId}")
    public ResponseEntity<?> addCategoryToProduct(@PathVariable int productId, @PathVariable int categoryId){

        boolean addCheck = productService.addCategoryToProduct(productId,categoryId);

        if(!addCheck){
            return ResponseEntity.badRequest().body("Either Product does not exist or category does not exist");
        }

        return ResponseEntity.ok().body("Category added to product");
    }

    @GetMapping("/getAllProducts")
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/getProduct/{id}")
    public ResponseEntity<?> getProductById(@PathVariable int id){

        if(productService.getProductById(id).isEmpty()){
            return ResponseEntity.badRequest().body("No Product of that Id Exists");
        }
        return ResponseEntity.ok().body(productService.getProductById(id));
    }

    @DeleteMapping("/removeCategory/{productId}/{categoryId}")
    public ResponseEntity<?> removeCategoryFromProduct(@PathVariable int productId, @PathVariable int categoryId){

        return ResponseEntity.ok().body(productService.deleteCategoryFromProduct(productId,categoryId));
    }

    @DeleteMapping("/removeProduct/{id}")
    public ResponseEntity<?> removeProduct(@PathVariable int id){

//        if(productService.getProductById(id).isEmpty()){
//            return ResponseEntity.badRequest().body("Product at that id does not exist");
//        }
//
//        productService.deleteProduct(id);
        return ResponseEntity.ok().body(productService.deleteProduct(id));
    }
    //need to create categories contrller !!!


}
