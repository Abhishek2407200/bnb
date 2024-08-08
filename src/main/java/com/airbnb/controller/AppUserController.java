package com.airbnb.controller;

import com.airbnb.payload.AppUserDto;
import com.airbnb.payload.AppUserResponse;
import com.airbnb.payload.JWTToken;
import com.airbnb.payload.LoginDto;
import com.airbnb.service.AppUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class AppUserController {
    private AppUserService service;

    public AppUserController(AppUserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> signUp(@Valid @RequestBody AppUserDto dto, BindingResult result){
        if (result.hasErrors()){
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(),HttpStatus.BAD_REQUEST);
        }
        String hashpw = BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt(10));
        dto.setPassword(hashpw);
        AppUserResponse user = service.createUser(dto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> verifyLogin(@RequestBody LoginDto loginDto){
        JWTToken token = service.verifyUser(loginDto);
        if (token != null){
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid UserName And Password", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping
    public String dummy(){
        return "Welcome to Air-Bnb!";
    }
}
