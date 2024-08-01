package com.airbnb.payload;


import lombok.Data;

@Data
public class AppUserResponse {
    private Long id;
    private String name;
    private String username;
    private String email;
}
