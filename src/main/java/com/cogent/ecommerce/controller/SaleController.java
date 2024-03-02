package com.cogent.ecommerce.controller;

import com.cogent.ecommerce.model.SalesItem;
import com.cogent.ecommerce.security.AuthService;
import com.cogent.ecommerce.security.JwtService;
import com.cogent.ecommerce.service.PurchaseOrderService;
import com.cogent.ecommerce.service.SalesItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sale")
@CrossOrigin(origins = "http://localhost:4200")
public class SaleController {
    @Autowired
    private SalesItemService salesItemService;
    @Autowired
    private PurchaseOrderService purchaseOrderService;
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtService jwtService;

    //also remove cart
    @PostMapping("/makeSale")
    public ResponseEntity<?> createNewSale(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody List<SalesItemDTO> salesItems){
        String token = authHeader.substring(7);
        String username = jwtService.getUsernameFromToken(token);
        int aid = authService.getAccountIdFromUsername(username);
        List<SalesItem> list = salesItemService.addNewSalesItem(aid,salesItems);
        purchaseOrderService.clearCart(aid);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/getAllSales")
    public ResponseEntity<?> getAllSales(){
        return ResponseEntity.ok().body(salesItemService.getAllSales());
    }

    @GetMapping("/getSalesOfUser")
    public ResponseEntity<?> getSalesOfUser(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        String username = jwtService.getUsernameFromToken(token);
        int aid = authService.getAccountIdFromUsername(username);
        return ResponseEntity.ok().body(salesItemService.getAllSalesOfUser(aid));
    }

}
