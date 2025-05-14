package com.example.CardOfMemory.service.Impl;

import com.example.CardOfMemory.config.jwt.JwtService;
import com.example.CardOfMemory.dto.auth.AuthenticationResponse;
import com.example.CardOfMemory.dto.auth.SignInRequest;
import com.example.CardOfMemory.dto.auth.SignUpRequest;
import com.example.CardOfMemory.entity.User;
import com.example.CardOfMemory.exceptions.AlreadyExistsException;
import com.example.CardOfMemory.exceptions.InvalidPasswordException;
import com.example.CardOfMemory.exceptions.NotFoundException;
import com.example.CardOfMemory.repository.UserRepository;
import com.example.CardOfMemory.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public AuthenticationResponse authenticate(SignUpRequest request) {
        log.info("Идет регистрация пользователя с почтой: {}", request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Пользователь с почтой " + request.getEmail() + " уже существует");
        }

        User users = new User();
        users.setName(request.getUsername());
        users.setEmail(request.getEmail());
        users.setPassword(request.getPassword());
        users.setRole(request.getRole());
        users.setCreatedAt(LocalDateTime.now());

        userRepository.save(users);

        String jwtToken = jwtService.generateToken(users.getEmail());
        log.info("Пользователь с почтой {} успешно зарегистрирован", request.getEmail());

        return new AuthenticationResponse(jwtToken, users.getName(), users.getRole());
    }

    @Override
    public AuthenticationResponse signIn(SignInRequest signInRequest) {
        log.info("Идет вход пользователя с почтой: {}", signInRequest.getEmail());

        User user = userRepository.findByEmail(signInRequest.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));

        if (user == null) {
            log.warn("Пользователь с почтой {} не найден", signInRequest.getEmail());
            throw new NotFoundException("User not found");
        }
        if (!user.getPassword().equals(signInRequest.getPassword())) {
            log.warn("Пароль пользователя с почтой {} не совпадает", signInRequest.getEmail());
            throw new InvalidPasswordException("П");
        }

        String jwtToken = jwtService.generateToken(user.getEmail());
        log.info("Пользователь с почтой {} успешно вошел в систему", signInRequest.getEmail());
        return new AuthenticationResponse(jwtToken, user.getName(), user.getRole());
    }
}
