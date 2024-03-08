package com.cogent.ecommerce.controller;

import com.cogent.ecommerce.model.Category;
import com.cogent.ecommerce.model.Product;
import com.cogent.ecommerce.repository.ProductJpaRepository;
import com.cogent.ecommerce.security.AuthService;
import com.cogent.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProdController {

    //TODO: FOR SECURITY THESE NEED TO ONLY BE ACCESSIBLE TO ADMINS

    @Autowired
    ProductService productService;
    @Autowired
    AuthService authService;

    //admin usage in final version
    //currently just to add products using postman
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestHeader("Authorization") String authHeader,@RequestBody Product product){

        String token = authHeader.substring(7);
        if(!authService.checkIfAdmin(token)){
            return ResponseEntity.badRequest().body("Not Authorized");
        }

        if(productService.getProductById(product.getId()).isPresent()){
            return ResponseEntity.badRequest().body("Product Exists");
        }
        productService.addProducts(product);
        return ResponseEntity.ok().body(product);
    }

    @PutMapping("/addCategory/{productId}/{categoryId}")
    public ResponseEntity<?> addCategoryToProduct(@RequestHeader("Authorization") String authHeader,@PathVariable int productId, @PathVariable int categoryId){

        String token = authHeader.substring(7);
        if(!authService.checkIfAdmin(token)){
            return ResponseEntity.badRequest().body("Not Authorized");
        }

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
    public ResponseEntity<?> removeCategoryFromProduct(@RequestHeader("Authorization") String authHeader,@PathVariable int productId, @PathVariable int categoryId){

        String token = authHeader.substring(7);
        if(!authService.checkIfAdmin(token)){
            return ResponseEntity.badRequest().body("Not Authorized");
        }

        return ResponseEntity.ok().body(productService.deleteCategoryFromProduct(productId,categoryId));
    }

    @DeleteMapping("/removeProduct/{id}")
    public ResponseEntity<?> removeProduct(@RequestHeader("Authorization") String authHeader,@PathVariable int id){

        String token = authHeader.substring(7);
        if(!authService.checkIfAdmin(token)){
            return ResponseEntity.badRequest().body("Not Authorized");
        }
//        if(productService.getProductById(id).isEmpty()){
//            return ResponseEntity.badRequest().body("Product at that id does not exist");
//        }
//
//        productService.deleteProduct(id);
        return ResponseEntity.ok().body(productService.deleteProduct(id));
    }
    //need to create categories contrller !!!

//    @PostMapping("/addWithCategories")
//    public ResponseEntity<?> addProductWithCategories(@RequestBody ProductWithCategoryDTO productWithCategories){
//        return ResponseEntity.ok().body(productService.addProductWithCategories(productWithCategories.getProduct(),
//                productWithCategories.getCategories()));
//    }

}
