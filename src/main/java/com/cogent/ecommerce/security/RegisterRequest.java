package com.cogent.ecommerce.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
//THIS IS USED AS JSON PAYLOAD READER
    private String email;
    private String username;
    private String password;

}
