package com.example.demo.service.security;

import com.example.demo.dtos.security.CustomUserDetails;
import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.utils.constants.CommonMessage;
import com.example.demo.utils.customexceptions.RequiredResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        User newUser;
        if(user.isEmpty())
        {
            throw new RequiredResourceNotFoundException(CommonMessage.USER_NOT_FOUND);
        }else{
            newUser = user.get();
        }

        return CustomUserDetails.build(newUser);
    }
}
