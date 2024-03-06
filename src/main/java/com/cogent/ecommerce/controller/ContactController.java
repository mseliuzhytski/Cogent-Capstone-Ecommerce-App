package com.cogent.ecommerce.controller;

import com.cogent.ecommerce.security.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contact")
@CrossOrigin(origins = "http://localhost:4200")
public class ContactController {

    @Autowired
    AuthService authService;

    @PostMapping("/sendMessage")
    public ResponseEntity<?> sendMessage(@RequestBody ContactDTO contactDTO){
        return ResponseEntity.ok().body(authService.sendEmailToManagement(contactDTO));
    }

}
