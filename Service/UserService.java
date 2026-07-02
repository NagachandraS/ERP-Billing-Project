package com.example.oilbilling.services;

import com.example.oilbilling.model.Users;
import com.example.oilbilling.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository)
    {
        this.userRepository=userRepository;
    }

public void createDefaultUserIfNotExist()
{
    if(userRepository.count()==0)
    {
        Users admin = new Users("Admin", "Welcome12345");
        userRepository.save(admin);
    }
}
public boolean ValidateAdminUser(Users users)

{
 return userRepository.findUserByUserNameAndPassWord(users.getUserName(),users.getPassWord()).isPresent();
}
}
