package com.saudesync.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.saudesync.model.Credencial;
import com.saudesync.model.Token;
import com.saudesync.model.Usuario;
import com.saudesync.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${jwt.custom-secret}")
    private String customSecret;

    public Token generateToken(Credencial credencial) {
        Algorithm alg = Algorithm.HMAC256(customSecret);
        String jwt = JWT.create()
                .withSubject(credencial.email())
                .withIssuer("SaudeSync")
                .withExpiresAt(Instant.now().plus(4, ChronoUnit.HOURS))
                .sign(alg);
        return new Token(jwt, "JWT", "Bearer");
    }

    public Usuario validate(String token) {
        try {
            Algorithm alg = Algorithm.HMAC256(customSecret);
            String email = JWT.require(alg)
                    .withIssuer("SaudeSync")
                    .build()
                    .verify(token)
                    .getSubject();

            return usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new JWTVerificationException("Usuário não encontrado"));
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("Token inválido");
        }
    }

    public String getToken(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }

        return header.replace("Bearer ", "");
    }
}
