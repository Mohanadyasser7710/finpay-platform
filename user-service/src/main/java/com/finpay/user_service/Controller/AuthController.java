package com.finpay.user_service.Controller;

import com.finpay.user_service.dto.AuthResponseDto;
import com.finpay.user_service.dto.LoginRequestDto;
import com.finpay.user_service.security.CustomUserDetails;
import com.finpay.user_service.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
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
}
