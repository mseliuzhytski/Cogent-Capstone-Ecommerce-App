package com.cogent.ecommerce.controller;

import com.cogent.ecommerce.model.PurchaseOrder;
import com.cogent.ecommerce.security.AuthService;
import com.cogent.ecommerce.security.JwtService;
import com.cogent.ecommerce.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@CrossOrigin
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/addToCart/{pid}/{quantity}")
    public ResponseEntity<?> addToCart(@RequestHeader("Authorization") String authHeader, @PathVariable int pid,@PathVariable int quantity){
        String token = authHeader.substring(7);
        String username = jwtService.getUsernameFromToken(token);
        int aid = authService.getAccountIdFromUsername(username);
        PurchaseOrder output = purchaseOrderService.addItemToCart(quantity,aid,pid);
        if(output==null){
            return ResponseEntity.badRequest().body("Product exists in cart");
        }
        return ResponseEntity.ok().body(output);
    }
    //TODO: Need to change cart logic to not allow adding duplicate products if a user already has that product --DONE
    //right now causes error in update method, because there can be duplicate products at same account
    //TODO: ALSO CHANGE EACH METHOD TO USE REQUEST HEADER AND NOT DIRECTLY TAKE USERNAME AS VARIABLE --DONE
    //realized it is a security risk
    @GetMapping("/getCart")
    public List<PurchaseOrder> getCart(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        String username = jwtService.getUsernameFromToken(token);
        int aid = authService.getAccountIdFromUsername(username);
        return purchaseOrderService.getPurchaseOrdersOfUser(aid);
    }

    @PutMapping("/updateCart/{pid}/{quantity}")
    public ResponseEntity<?> updateCart(@RequestHeader("Authorization") String authHeader, @PathVariable int pid,@PathVariable int quantity){
        String token = authHeader.substring(7);
        String username = jwtService.getUsernameFromToken(token);
        int aid = authService.getAccountIdFromUsername(username);
        return ResponseEntity.ok().body(purchaseOrderService.changeQuantity(quantity,aid,pid));
    }

    @DeleteMapping("/deleteItem/{pid}")
    public ResponseEntity<?> deleteItemFromCart(@RequestHeader("Authorization") String authHeader,@PathVariable int pid){
        String token = authHeader.substring(7);
        String username = jwtService.getUsernameFromToken(token);
        int aid = authService.getAccountIdFromUsername(username);
        return ResponseEntity.ok().body(purchaseOrderService.deleteCartItem(aid,pid));
    }

}
