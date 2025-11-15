package com.bookverse.service;

import com.bookverse.entity.User;
import com.bookverse.enums.ErrorCode;
import com.bookverse.exception.EntityNotFoundException;
import com.bookverse.repository.UserRepository;
import com.bookverse.utils.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
        return new CustomUserDetails(user);
    }
}
