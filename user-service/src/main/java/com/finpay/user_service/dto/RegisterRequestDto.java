package com.finpay.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {

    @Email(message = "must be email format")
    @NotBlank(message = "email cannot be blank")
    @Size(max = 254)
    private String email;
    @Size(min = 8,max = 26,message = "password must be at least 8 chars and at most 26")
    @NotBlank(message = "password cannot be blank")
    @ToString.Exclude
    private String password;

}
