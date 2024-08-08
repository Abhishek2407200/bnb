package com.airbnb.service;

import com.airbnb.payload.AppUserDto;
import com.airbnb.payload.AppUserResponse;
import com.airbnb.payload.JWTToken;
import com.airbnb.payload.LoginDto;

public interface AppUserService {
    //create user
    AppUserResponse createUser(AppUserDto appUserDto);

    //get user by email
    AppUserResponse getUserByEmail(String email);

    //get user by username
    AppUserResponse getUserByUsername(String username);

    //verify Login user
    JWTToken verifyUser(LoginDto loginDto);
}
