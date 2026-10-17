package com.finpay.user_service.dto;

import java.util.UUID;

public record AuthResponseDto(String token,
                              String type,
                              UUID id,
                              String email,
                              String role) {
    public AuthResponseDto(String token,UUID id ,String email,String role){
        this(token,"Bearer",id,email,role);
    }


}

