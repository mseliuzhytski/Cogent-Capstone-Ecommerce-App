package com.cogent.ecommerce.security;

import com.cogent.ecommerce.User.CustomUser;
import com.cogent.ecommerce.User.Role;
import com.cogent.ecommerce.User.UserRepository;
import com.cogent.ecommerce.model.Account;
import com.cogent.ecommerce.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AccountService accountService;

    //boolean since it is just registering not creating a token
    public boolean register(RegisterRequest request, Role role) {

        if(userRepository.existsByUsername(request.getUsername())){
            return false;
        }
        if(userRepository.existsByEmail(request.getEmail())){
            return false;
        }

        var user = CustomUser.builder().email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();
        userRepository.save(user);
        boolean isUser = user.getRole().equals(Role.USER);
        boolean isAdmin = user.getRole().equals(Role.ADMIN);

        Account account = Account.builder().id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .isUser(isUser)
                .isAdmin(isAdmin).build();

        Account newAccount = accountService.addAccount(account);
        if (newAccount == null) {
            System.err.println("Could not create account class from CustomUser");
            return false;
        }
//        var jwtToken = jwtService.generateToken(user);

        return true;
    }

    public boolean registerUser(RegisterRequest request) {
        return register(request, Role.USER);
    }

    public boolean registerAdmin(RegisterRequest request) {
        return register(request, Role.ADMIN);
    }

    public TokenResponse authenticate(Authentication auth){

        return new TokenResponse(jwtService.createToken(auth));

    }



}
