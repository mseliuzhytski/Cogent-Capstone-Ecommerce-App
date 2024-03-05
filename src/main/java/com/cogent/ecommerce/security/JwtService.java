package com.cogent.ecommerce.security;

import com.cogent.ecommerce.User.CustomUser;
import com.cogent.ecommerce.User.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

//creates and checks jwt tokens

@Service
public class JwtService {

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;
//
//    @Autowired
//    private UserRepository userRepository;

    //HANDLING TOKEN CREATION:

    protected String createToken(Authentication auth) {

//        CustomUser user = userRepository.findByUsername(auth.getName()).orElse(null);
//        System.out.println(user.getId());

        var claims = JwtClaimsSet.builder().issuer("self").issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60*60))
                .subject(auth.getName())
                .claim("scope",createScope(auth))
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private Object createScope(Authentication auth) {
        return auth.getAuthorities().stream().map(x->x.getAuthority()).collect(Collectors.joining(" "));
    }


    public boolean isValidToken(String token) {
        try{
            jwtDecoder.decode(token);
            //System.out.println(jwtDecoder.decode(token).getClaims().get("sub"));
            return true;
        }catch (JwtException e){
            return false;
        }
    }

    public String getUsernameFromToken(String token){
        try{
            jwtDecoder.decode(token);
            //System.out.println(jwtDecoder.decode(token).getClaims().get("sub"));
            return jwtDecoder.decode(token).getClaims().get("sub").toString();
        }catch (JwtException e){
            return null;
        }
    }

    public String getRoleFromToken(String token){

        try {
            jwtDecoder.decode(token);
            return jwtDecoder.decode(token).getClaims().get("scope").toString();
        }catch (JwtException e){
            return null;
        }
    }

}
