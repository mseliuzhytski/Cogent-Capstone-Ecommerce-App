package com.cogent.ecommerce.security;

import com.cogent.ecommerce.User.CustomUser;
import com.cogent.ecommerce.security.AuthService;
import com.cogent.ecommerce.security.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/signUp")
    public ResponseEntity<Boolean> createUser(@RequestBody RegisterRequest user){

        System.out.println(user.getPassword());
        System.out.println(user.getUsername());
        System.out.println(user.getEmail());

        boolean register = authService.registerUser(user);
        if(!register) return ResponseEntity.badRequest().body(false);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/auth")
    public TokenResponse getAuth(Authentication auth){
        System.out.println(auth);
        return new TokenResponse(jwtService.createToken(auth));
    }

    @GetMapping("/checkToken")
    public ResponseEntity<Boolean> checkTokenValidity(@RequestHeader("Authorization") String authHeader){
        String token = authHeader;

        System.out.println("About to check validity");
        boolean checkValid = jwtService.isValidToken(token);
        System.out.println("Is Valid token " + checkValid);

        if(!checkValid) return ResponseEntity.ok(false);
        return ResponseEntity.ok(true);
    }

}
