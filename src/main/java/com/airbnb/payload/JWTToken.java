package com.airbnb.payload;

import lombok.Data;

@Data
public class JWTToken {

    private String type;
    private String token;
}

