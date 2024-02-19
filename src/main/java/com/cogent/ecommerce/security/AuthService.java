package com.cogent.ecommerce.security;

import com.cogent.ecommerce.User.CustomUser;
import com.cogent.ecommerce.User.Role;
import com.cogent.ecommerce.User.UserRepository;
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

    //boolean since it is just registering not creating a token
    public boolean register(RegisterRequest request){

        if(userRepository.existsByUsername(request.getUsername())){
            return false;
        }
        if(userRepository.existsByEmail(request.getEmail())){
            return false;
        }

        var user = CustomUser.builder().email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);

//        var jwtToken = jwtService.generateToken(user);

        return true;
    }

    public TokenResponse authenticate(Authentication auth){

        return new TokenResponse(jwtService.createToken(auth));

    }



}
