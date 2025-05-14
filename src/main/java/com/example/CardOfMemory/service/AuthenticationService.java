package com.example.CardOfMemory.service;

import com.example.CardOfMemory.dto.auth.AuthenticationResponse;
import com.example.CardOfMemory.dto.auth.SignInRequest;
import com.example.CardOfMemory.dto.auth.SignUpRequest;
import jakarta.validation.Valid;

public interface AuthenticationService {
    AuthenticationResponse authenticate(@Valid SignUpRequest signUpRequest);

    AuthenticationResponse signIn(SignInRequest signInRequest);
}
