package com.finpay.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {

    @Email(message = "must be email format")
    @NotBlank(message = "email cannot be blank")
    private String email;
    @Size(min = 8,max = 26,message = "password must be more than 8 chars and less than 26")
    @NotBlank(message = "password cannot be blank")
    private String password;

}
