package com.finpay.user_service.service;

import com.finpay.user_service.dto.RegisterRequestDto;
import com.finpay.user_service.dto.RegisterResponseDto;
import com.finpay.user_service.entity.Role;
import com.finpay.user_service.entity.UserEntity;
import com.finpay.user_service.exception.DuplicateEmailException;
import com.finpay.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    @Transactional
    public RegisterResponseDto registerUser(RegisterRequestDto dto){
        String email=dto.getEmail().trim().toLowerCase(Locale.ROOT);

        if(repo.existsByEmail(email)){
            throw new DuplicateEmailException();
        }
        UserEntity newUser= new UserEntity();
        newUser.setEmail(email);
        String hashedPassword=encoder.encode(dto.getPassword());
        newUser.setPasswordHash(hashedPassword);
        newUser.setRole(Role.USER);
        UserEntity saved;
        try {
            saved=repo.saveAndFlush(newUser);
        } catch (DataIntegrityViolationException e){
            String rootCause=e.getMostSpecificCause().getMessage();
            if(rootCause!=null&&rootCause.contains("uk_finpay_users_email")){
                throw new DuplicateEmailException();
            }
            throw e;
        }
        log.info("user created with id {}",saved.getId());
        return mapToDto(saved);
    }

    private RegisterResponseDto mapToDto(UserEntity user){
        RegisterResponseDto dto=new RegisterResponseDto();
        dto.setId(user.getId());
        dto.setRole(user.getRole().name());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

}
