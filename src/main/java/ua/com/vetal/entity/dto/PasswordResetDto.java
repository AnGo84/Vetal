package ua.com.vetal.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PasswordResetDto {

    @NotNull
    private String password;

    @NotNull
    private String confirmPassword;

    @NotNull
    private String token;

    private Long userId;

}
