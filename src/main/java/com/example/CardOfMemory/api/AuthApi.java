package com.example.CardOfMemory.api;

import com.example.CardOfMemory.dto.auth.AuthenticationResponse;
import com.example.CardOfMemory.dto.auth.SignInRequest;
import com.example.CardOfMemory.dto.auth.SignUpRequest;
import com.example.CardOfMemory.service.Impl.AuthenticationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Сервис аутентификации")
@RequiredArgsConstructor
public class AuthApi {
    private final AuthenticationServiceImpl authenticationService;

    @PostMapping("/sign-up")
    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Регистрация нового пользователя с предоставленными данными регистрации, " +
                    "такими как имя пользователя, почта и пароль. " +
                    "Этот конечный точка создает новый аккаунт пользователя в системе."
    )
    public AuthenticationResponse signUp(
            @Parameter(description = "Sign-up request containing user details", required = true)
            @Valid @RequestBody SignUpRequest signUpRequest) {
        return authenticationService.authenticate(signUpRequest);
    }

    @PostMapping("/sign-in")
    @Operation(
            summary = "Аутентификация пользователя",
            description = "Аунтифицирует пользователя и возвращает ответ с информацией об аутентификации," +
                    " включая токен доступа и другие связанные данные. " +
                    "Пользователь должен предоставить действительные учетные данные для входа," +
                    " например, почта и пароль, чтобы быть аутентифицированным."
    )
    public AuthenticationResponse signIn(
            @Parameter(description = "Sign-in request containing user credentials", required = true)
            @RequestBody SignInRequest signInRequest) {
        return authenticationService.signIn(signInRequest);
    }
}