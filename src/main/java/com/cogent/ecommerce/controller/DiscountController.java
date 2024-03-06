package com.cogent.ecommerce.controller;

import com.cogent.ecommerce.model.Account;
import com.cogent.ecommerce.model.Discount;
import com.cogent.ecommerce.security.AuthService;
import com.cogent.ecommerce.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/discount")
@CrossOrigin(origins = "http://localhost:4200")
public class DiscountController {

    @Autowired
    private DiscountService discountService;
    @Autowired
    AuthService authService;

    @GetMapping(value="/list")
    public List<Discount> getDiscounts() {
        return discountService.getAllDiscounts();
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<Discount> getDiscountById(@PathVariable int id) {
        Discount a = null;
        try {
            a = discountService.getDiscountById(id).get();
        } catch (DataIntegrityViolationException ex) {
            ex.printStackTrace();
        }
        if (a != null) {
            return ResponseEntity.status(HttpStatus.OK).body(a);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping(value="/")
    public ResponseEntity<Discount> createDiscount(@RequestHeader("Authorization") String authHeader,
                                                   @RequestBody Discount discount) {
        String token = authHeader.substring(7);
        if(!authService.checkIfAdmin(token)){
            return ResponseEntity.badRequest().body(null);
        }
        Discount a = null;
        try {
            a = discountService.addDiscount(discount);
        } catch (DataIntegrityViolationException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        if (a != null) {
            return ResponseEntity.status(HttpStatus.OK).body(a);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Account> deleteDiscountById(@RequestHeader("Authorization") String authHeader,
                                                      @PathVariable int id) {
        String token = authHeader.substring(7);
        if(!authService.checkIfAdmin(token)){
            return ResponseEntity.badRequest().body(null);
        }
        Discount a = null;
        try {
            a = discountService.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            ex.printStackTrace();
        }
        if (a != null) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<Discount> updateProduct(@RequestHeader("Authorization") String authHeader,
                                                  @PathVariable int id,
                                                 @RequestBody Discount discount) {
        String token = authHeader.substring(7);
        if(!authService.checkIfAdmin(token)){
            return ResponseEntity.badRequest().body(null);
        }
        Discount a = null;
        try {
            a = discountService.updateDiscount(discount, id);
        } catch (DataIntegrityViolationException ex) {
            ex.printStackTrace();
        }
        if (a != null) {
            return ResponseEntity.status(HttpStatus.OK).body(a);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping(value="/getByCode/{code}")
    public ResponseEntity<Discount> updateDiscount(@PathVariable String code) {
        Discount discount = discountService.getDiscountByCode(code);
        if (discount != null) {
            return ResponseEntity.status(HttpStatus.OK).body(discount);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
