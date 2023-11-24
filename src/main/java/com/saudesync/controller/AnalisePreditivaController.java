package com.saudesync.controller;

import com.saudesync.model.AnalisePreditiva;
import com.saudesync.repository.AnalisePreditivaRepository;
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
@RequestMapping("/saudesync/api/analisepreditiva")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "AnalisePreditiva", description = "Análise Preditiva para o Usuário")
public class AnalisePreditivaController {

    @Autowired
    AnalisePreditivaRepository analisePreditivaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @PostMapping
    @Operation(
            summary = "Cadastro de Análise Preditiva",
            description = "Cadastra uma nova análise preditiva"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Análise preditiva cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou faltando")
    })
    public ResponseEntity<Object> cadastrarAnalisePreditiva(@RequestBody @Valid AnalisePreditiva analisePreditiva) {
        // Certifique-se de ter um usuário válido associado à análise preditiva
        Usuario usuario = usuarioRepository.findById(analisePreditiva.getUsuario().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não Encontrado"));
        analisePreditiva.setUsuario(usuario);

        analisePreditivaRepository.save(analisePreditiva);
        return ResponseEntity.status(HttpStatus.CREATED).body(analisePreditiva);
    }

    @GetMapping
    @Operation(
            summary = "Listar Análises Preditivas",
            description = "Retorna todas as análises preditivas cadastradas"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Análises preditivas listadas com sucesso"),
            @ApiResponse(responseCode = "403", description = "Token inválido")
    })
    public ResponseEntity<List<AnalisePreditiva>> listarAnalisesPreditivas() {
        List<AnalisePreditiva> analisesPreditivas = analisePreditivaRepository.findAll();
        return ResponseEntity.ok(analisesPreditivas);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Detalhar Análise Preditiva",
            description = "Busca uma análise preditiva por ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Análise preditiva detalhada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não foi encontrada Análise Preditiva com esse ID"),
            @ApiResponse(responseCode = "403", description = "Token inválido")
    })
    public ResponseEntity<AnalisePreditiva> detalharAnalisePreditiva(@PathVariable Long id) {
        AnalisePreditiva analisePreditiva = analisePreditivaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Análise Preditiva não Encontrada"));
        return ResponseEntity.ok(analisePreditiva);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar Análise Preditiva",
            description = "Atualiza uma análise preditiva por ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Análise preditiva atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou faltando"),
            @ApiResponse(responseCode = "404", description = "Não foi encontrada Análise Preditiva com esse ID"),
            @ApiResponse(responseCode = "403", description = "Token inválido")
    })
    public ResponseEntity<AnalisePreditiva> atualizarAnalisePreditiva(@PathVariable Long id, @RequestBody @Valid AnalisePreditiva analisePreditiva) {
        // Certifique-se de ter um usuário válido associado à análise preditiva
        Usuario usuario = usuarioRepository.findById(analisePreditiva.getUsuario().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não Encontrado"));
        analisePreditiva.setUsuario(usuario);

        analisePreditiva.setId(id);
        analisePreditivaRepository.save(analisePreditiva);
        return ResponseEntity.ok(analisePreditiva);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deletar Análise Preditiva",
            description = "Deleta uma análise preditiva por ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Análise preditiva deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não foi encontrada Análise Preditiva com esse ID"),
            @ApiResponse(responseCode = "403", description = "Token inválido")
    })
    public ResponseEntity<Object> deletarAnalisePreditiva(@PathVariable Long id) {
        analisePreditivaRepository.findById(id).ifPresentOrElse(
                analisePreditivaRepository::delete,
                () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Análise Preditiva não Encontrada");
                }
        );
        return ResponseEntity.noContent().build();
    }
}
