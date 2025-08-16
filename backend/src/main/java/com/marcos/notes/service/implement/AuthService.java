package com.marcos.notes.service.implement;

import com.marcos.notes.exception.RegistrationException;
import com.marcos.notes.model.AuthResponse;
import com.marcos.notes.dto.LoginDTO;
import com.marcos.notes.dto.RegisterDTO;
import com.marcos.notes.model.User;
import com.marcos.notes.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginDTO request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
            String token = jwtService.getToken(user);
            return AuthResponse.builder()
                    .token(token)
                    .build();
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Email o Contraseña incorrecta");
        }
    }

    public AuthResponse register(RegisterDTO request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("El email ya está en uso");
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .lastName(request.getLastname())
                .build();

        userRepository.save(user);

        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();

    }

}
