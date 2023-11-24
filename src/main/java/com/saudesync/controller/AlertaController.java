package com.saudesync.controller;

import com.saudesync.model.Alerta;
import com.saudesync.repository.AlertaRepository;
import com.saudesync.model.Usuario;
import com.saudesync.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/saudesync/api/alerta")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Alerta", description = "Alertas para o Usuário")
public class AlertaController {

    @Autowired
    AlertaRepository alertaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @PostMapping
    @Operation(
            summary = "Cadastro de Alerta",
            description = "Cadastra um novo alerta"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Alerta cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou faltando")
    })
    public ResponseEntity<Object> cadastrarAlerta(@RequestBody @Valid Alerta alerta) {
        // Certifique-se de ter um usuário válido associado ao alerta
        Usuario usuario = usuarioRepository.findById(alerta.getUsuario().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não Encontrado"));
        alerta.setUsuario(usuario);

        alertaRepository.save(alerta);
        return ResponseEntity.status(HttpStatus.CREATED).body(alerta);
    }

    @GetMapping
    @Operation(
            summary = "Listar Alertas",
            description = "Retorna todos os alertas cadastrados"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alertas listados com sucesso"),
            @ApiResponse(responseCode = "403", description = "Token inválido")
    })
    public ResponseEntity<List<Alerta>> listarAlertas() {
        List<Alerta> alertas = alertaRepository.findAll();
        return ResponseEntity.ok(alertas);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Detalhar Alerta",
            description = "Busca um alerta por ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alerta detalhado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não foi encontrado Alerta com esse ID"),
            @ApiResponse(responseCode = "403", description = "Token inválido")
    })
    public ResponseEntity<Alerta> detalharAlerta(@PathVariable Long id) {
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alerta não Encontrado"));
        return ResponseEntity.ok(alerta);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar Alerta",
            description = "Atualiza um alerta por ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alerta atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou faltando"),
            @ApiResponse(responseCode = "404", description = "Não foi encontrado Alerta com esse ID"),
            @ApiResponse(responseCode = "403", description = "Token inválido")
    })
    public ResponseEntity<Alerta> atualizarAlerta(@PathVariable Long id, @RequestBody @Valid Alerta alerta) {
        // Certifique-se de ter um usuário válido associado ao alerta
        Usuario usuario = usuarioRepository.findById(alerta.getUsuario().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não Encontrado"));
        alerta.setUsuario(usuario);

        alerta.setId(id);
        alertaRepository.save(alerta);
        return ResponseEntity.ok(alerta);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deletar Alerta",
            description = "Deleta um alerta por ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Alerta deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não foi encontrado Alerta com esse ID"),
            @ApiResponse(responseCode = "403", description = "Token inválido")
    })
    public ResponseEntity<Object> deletarAlerta(@PathVariable Long id) {
        alertaRepository.findById(id).ifPresentOrElse(
                alertaRepository::delete,
                () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alerta não Encontrado");
                }
        );
        return ResponseEntity.noContent().build();
    }
}
