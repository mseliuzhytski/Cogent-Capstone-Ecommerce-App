package com.cogent.ecommerce.security;

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

@Service
public class JwtService {

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;

    //HANDLING TOKEN CREATION:

    protected String createToken(Authentication auth) {
        var claims = JwtClaimsSet.builder().issuer("self").issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60*20))
                .subject(auth.getName())
                .claim("scope",createScope(auth)).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private Object createScope(Authentication auth) {
        return auth.getAuthorities().stream().map(x->x.getAuthority()).collect(Collectors.joining(" "));
    }


    public boolean isValidToken(String token) {
        try{
            jwtDecoder.decode(token);
            return true;
        }catch (JwtException e){
            return false;
        }
    }


}
