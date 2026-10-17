package com.finpay.user_service.security;


import com.finpay.user_service.entity.UserEntity;
import com.finpay.user_service.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@NullMarked
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        UserEntity user=repo.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("user with this email was not found"));
        return new CustomUserDetails(user);
    }
}
