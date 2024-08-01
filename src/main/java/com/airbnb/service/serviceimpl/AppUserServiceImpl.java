package com.airbnb.service.serviceimpl;

import com.airbnb.entity.AppUser;
import com.airbnb.exceptions.PasswordNotMatchException;
import com.airbnb.exceptions.ResourceNotFoundException;
import com.airbnb.exceptions.UserExistsException;
import com.airbnb.payload.AppUserDto;
import com.airbnb.payload.AppUserResponse;
import com.airbnb.payload.LoginDto;
import com.airbnb.repository.AppUserRepository;
import com.airbnb.service.AppUserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserServiceImpl implements AppUserService {

    private AppUserRepository appUserRepository;
    private ModelMapper mapper;

    public AppUserServiceImpl(AppUserRepository appUserRepository, ModelMapper mapper) {
        this.appUserRepository = appUserRepository;
        this.mapper = mapper;
    }

    @Override
    public AppUserResponse createUser(AppUserDto appUserDto) {
        Optional<AppUser> byEmail = appUserRepository.findByEmail(appUserDto.getEmail());
        Optional<AppUser> byUsername = appUserRepository.findByUsername(appUserDto.getUsername());
        if(byEmail.isPresent() || byUsername.isPresent()){
            throw new UserExistsException("User already exists");
        }
        AppUser appUser = mapToEntity(appUserDto);
        AppUser savedUser = appUserRepository.save(appUser);
        return mapper.map(savedUser,AppUserResponse.class);
    }

    @Override
    public AppUserResponse getUserByEmail(String email) {
        AppUser appUser = appUserRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Could not find user with email " + email));
        return mapper.map(appUser,AppUserResponse.class);
    }

    @Override
    public AppUserResponse getUserByUsername(String username) {
        AppUser appUser = appUserRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Could not find user with username " + username));
        return mapper.map(appUser,AppUserResponse.class);
    }

    @Override
    public boolean verifyUser(LoginDto loginDto) {
        Optional<AppUser> byUsernameOrEmail = appUserRepository.findByUsernameOrEmail(loginDto.getUsername(), loginDto.getEmail());
        if (byUsernameOrEmail.isPresent()) {
            AppUser user = byUsernameOrEmail.get();
            boolean checkpw = BCrypt.checkpw(loginDto.getPassword(), user.getPassword());
            if (!checkpw){
                throw new PasswordNotMatchException("Please Enter valid password");
            }
            return checkpw;
        }
        return false;
    }


    AppUserDto mapToDto(AppUser user){
        return mapper.map(user, AppUserDto.class);
    }

    AppUser mapToEntity(AppUserDto userDto){
        return mapper.map(userDto, AppUser.class);
    }
}
