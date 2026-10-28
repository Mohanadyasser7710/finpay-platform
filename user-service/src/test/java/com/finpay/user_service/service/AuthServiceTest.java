package com.finpay.user_service.service;

import com.finpay.user_service.dto.RegisterRequestDto;
import com.finpay.user_service.dto.RegisterResponseDto;
import com.finpay.user_service.entity.Role;
import com.finpay.user_service.entity.UserEntity;
import com.finpay.user_service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository repo;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private AuthService service;

    @Test
    void registerUser_validInput_returnsCreatedUser(){
        RegisterRequestDto request = new RegisterRequestDto("Ali@mail.com","secret123");
        when(repo.existsByEmail("ali@mail.com")).thenReturn(false);
        when(encoder.encode("secret123")).thenReturn("hashed-password");

        UserEntity savedUser = new UserEntity();
        savedUser.setId(UUID.randomUUID());
        savedUser.setEmail("ali@mail.com");
        savedUser.setPasswordHash("hashed-password");
        savedUser.setRole(Role.USER);
        savedUser.setCreatedAt(LocalDateTime.now());
        when(repo.saveAndFlush(any(UserEntity.class))).thenReturn(savedUser);

        RegisterResponseDto response=service.registerUser(request);
        assertNotNull(response.getId());
        assertEquals("ali@mail.com", response.getEmail());
        assertEquals("USER", response.getRole());
        verify(repo).saveAndFlush(any(UserEntity.class));


    }
}
