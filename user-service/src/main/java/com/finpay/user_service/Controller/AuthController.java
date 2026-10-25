package com.finpay.user_service.Controller;

import com.finpay.user_service.dto.AuthResponseDto;
import com.finpay.user_service.dto.LoginRequestDto;
import com.finpay.user_service.dto.RegisterRequestDto;
import com.finpay.user_service.dto.RegisterResponseDto;
import com.finpay.user_service.security.CustomUserDetails;
import com.finpay.user_service.security.JwtTokenProvider;
import com.finpay.user_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;
    @PostMapping("/Login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )
        );

        String token=jwtTokenProvider.createToken(authentication);
        CustomUserDetails user=(CustomUserDetails) authentication.getPrincipal();
        String role=user.getAuthorities().iterator().next().getAuthority();
        return ResponseEntity.ok(new AuthResponseDto(
                token,
                user.getId(),
                user.getUsername(),
                role
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@Valid @RequestBody RegisterRequestDto dto){
        RegisterResponseDto newDto=authService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newDto);
    }


}
