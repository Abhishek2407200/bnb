package com.airbnb.payload;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class AppUserDto {
    @NotEmpty
    @Size(min = 4, message = "Name Should be more than 4 characters")
    private String name;

    @NotEmpty
    @Size(min = 8, message = "username should be more than 8 characters")
    private String username;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    @Size(min = 8, message = "Password should be more than 8 characters")
    private String password;

    private String role;
}
