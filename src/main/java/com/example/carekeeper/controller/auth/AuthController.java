package com.example.carekeeper.controller;

import com.example.carekeeper.service.auth.AuthService;
import com.example.carekeeper.controller.dto.LoginRequest;
import com.example.carekeeper.controller.dto.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/autenticacao")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint para login de usuários.
     *
     * Recebe um JSON com email e senha:
     * {
     *   "email": "usuario@exemplo.com",
     *   "password": "senha123"
     * }
     *
     * Retorna:
     * - 200 OK + { "token": "<JWT>" } se login válido
     * - 401 Unauthorized se login inválido
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.getEmail(), request.getPassword());

        if (token == null) return ResponseEntity.status(401).build();

        return ResponseEntity.ok(new LoginResponse(token));
    }

    /**
     * Endpoint para logout de usuários.
     *
     * Este endpoint não invalida o token JWT no servidor (stateless), mas informa ao
     * cliente que deve descartar o token localmente.
     *
     * Retorna 200 OK sempre.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(name = "Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        authService.invalidateToken(token);

        return ResponseEntity.ok().build();
    }
}
