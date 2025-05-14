package com.example.CardOfMemory.dto.auth;

import com.example.CardOfMemory.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Запрос на регистрацию пользователя")
public class SignUpRequest {
    @Schema(description = "Имя пользователя", example = "johndoe")
    @NotBlank
    private String username;
    @Schema(description = "Email пользователя", example = "john@example.com")
    @NotBlank
    @Email
    private String email;
    @Schema(description = "Пароль пользователя", example = "password123")
    @NotBlank
    private String password;
    @Schema(description = "Роль пользователя", example = "USER")
    @NotBlank
    private Role role;
}
