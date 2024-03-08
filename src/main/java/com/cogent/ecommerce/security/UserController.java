package com.cogent.ecommerce.security;

import com.cogent.ecommerce.User.CustomUser;
import com.cogent.ecommerce.model.Account;
import com.cogent.ecommerce.security.AuthService;
import com.cogent.ecommerce.security.RegisterRequest;
import com.cogent.ecommerce.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/signUp")
    public ResponseEntity<Boolean> createUser(@RequestBody RegisterRequest user) {
        boolean register = authService.registerUser(user);
        if (!register) return ResponseEntity.badRequest().body(false);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/createAdmin")
    public ResponseEntity<Boolean> createAdmin(@RequestBody RegisterRequest admin) {
        boolean register = authService.registerAdmin(admin);
        if (!register) return ResponseEntity.badRequest().body(false);
        return ResponseEntity.ok(true);
    }

    //this is checked by the security filter chain ->
    // if the username and password are correct this will allow this request to go through
    // else this method will not be allowed to be accessed
    @PostMapping("/auth")
    public TokenResponse getAuth(Authentication auth) {
        System.out.println(auth);
        return new TokenResponse(jwtService.createToken(auth));
    }

    @GetMapping("/checkToken")
    public ResponseEntity<Boolean> checkTokenValidity(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);

//        System.out.println("AUTH HEADER: " +authHeader);
//        System.out.println("TOKEN: "+token );


        boolean checkValid = jwtService.isValidToken(token);
//        System.out.println(checkValid);

        if (!checkValid) return ResponseEntity.ok(false);
        return ResponseEntity.ok(true);
    }
    @GetMapping("/checkAdmin")
    public ResponseEntity<Boolean> checkIfUserIsAdmin(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        return ResponseEntity.ok().body(authService.checkIfAdmin(token));
    }

    //same as check token but returns username from the token subject
    @GetMapping("/getUsername")
    public ResponseEntity<?> getUsernameFromToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String username = jwtService.getUsernameFromToken(token);
        if (username == null) return ResponseEntity.badRequest().body(null);
        return ResponseEntity.ok().body(username);
    }


    @GetMapping("/resetPass/{email}")
    public ResponseEntity<Boolean> checkIfUserExistsByEmail(@PathVariable String email) {
        CustomUser user = authService.getUserByEmail(email).orElse(null);
        if (user == null) return ResponseEntity.ok().body(false);
        //now set reset token and expiry time
        authService.createResetToken(user);
        authService.createEmail(user.getEmail(), user.getResetToken());
        return ResponseEntity.ok().body(true);
    }

    @PutMapping("/resetPass/{token}/{email}")
    public ResponseEntity<?> changePassword(@PathVariable String token, @PathVariable String email,
                                            @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return ResponseEntity.ok().body(authService.changePassword(token, email, resetPasswordRequest));
    }
}

