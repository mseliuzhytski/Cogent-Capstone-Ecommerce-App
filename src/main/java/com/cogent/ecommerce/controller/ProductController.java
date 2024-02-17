package com.cogent.ecommerce.controller;

import com.cogent.ecommerce.model.Product;
import com.cogent.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping(value="/product/list")
    public List<Product> getProducts() {
        return productRepository.getAllProducts();
    }

    @GetMapping(value="/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        Product p = null;
        try {
            p = productRepository.getById(id);
        } catch (DataIntegrityViolationException ex) {
        }
        if (p != null) {
            return ResponseEntity.status(HttpStatus.OK).body(p);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping(value="/product/")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product p = null;
        try {
            p = productRepository.saveProduct(product);
        } catch (DataIntegrityViolationException ex) {
        }
        if (p != null) {
            return ResponseEntity.status(HttpStatus.OK).body(p);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping(value="/product/{id}")
    public ResponseEntity<Product> deleteProductById(@PathVariable int id) {
        Product p = null;
        try {
            p = productRepository.deleteProductById(id);
        } catch (DataIntegrityViolationException ex) {
            ex.printStackTrace();
        }
        if (p != null) {
            return ResponseEntity.status(HttpStatus.OK).body(p);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping(value="/product/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id,
                                                 @RequestBody Product newProduct) {
        Product p = null;
        try {
            p = productRepository.updateProduct(newProduct, id);
        } catch (DataIntegrityViolationException ex) {
        }
        if (p != null) {
            return ResponseEntity.status(HttpStatus.OK).body(p);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
