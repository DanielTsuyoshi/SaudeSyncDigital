package com.saudesync.controller;

import com.saudesync.model.DadoSaude;
import com.saudesync.repository.DadoSaudeRepository;
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
@RequestMapping("/saudesync/api/dadosaude")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "DadoSaude", description = "Dados de Saúde do Usuário")
public class DadoSaudeController {

    @Autowired
    DadoSaudeRepository dadoSaudeRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @PostMapping
    @Operation(
            summary = "Cadastro de Dado de Saúde",
            description = "Cadastra um novo dado de saúde"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dado de saúde cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou faltando")
    })
    public ResponseEntity<Object> cadastrarDadoSaude(@RequestBody @Valid DadoSaude dadoSaude) {
        // Certifique-se de ter um usuário válido associado ao dado de saúde
        Usuario usuario = usuarioRepository.findById(dadoSaude.getUsuario().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não Encontrado"));
        dadoSaude.setUsuario(usuario);

        dadoSaudeRepository.save(dadoSaude);
        return ResponseEntity.status(HttpStatus.CREATED).body(dadoSaude);
    }

    @GetMapping
    @Operation(
            summary = "Listar Dados de Saúde",
            description = "Retorna todos os dados de saúde cadastrados"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados de saúde listados com sucesso"),
            @ApiResponse(responseCode = "403", description = "Token inválido")
    })
    public ResponseEntity<List<DadoSaude>> listarDadosSaude() {
        List<DadoSaude> dadosSaude = dadoSaudeRepository.findAll();
        return ResponseEntity.ok(dadosSaude);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Detalhar Dado de Saúde",
            description = "Busca um dado de saúde por ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dado de saúde detalhado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não foi encontrado Dado de Saúde com esse ID"),
            @ApiResponse(responseCode = "403", description = "Token inválido")
    })
    public ResponseEntity<DadoSaude> detalharDadoSaude(@PathVariable Long id) {
        DadoSaude dadoSaude = dadoSaudeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dado de Saúde não Encontrado"));
        return ResponseEntity.ok(dadoSaude);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar Dado de Saúde",
            description = "Atualiza um dado de saúde por ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dado de saúde atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou faltando"),
            @ApiResponse(responseCode = "404", description = "Não foi encontrado Dado de Saúde com esse ID"),
            @ApiResponse(responseCode = "403", description = "Token inválido")
    })
    public ResponseEntity<DadoSaude> atualizarDadoSaude(@PathVariable Long id, @RequestBody @Valid DadoSaude dadoSaude) {
        // Certifique-se de ter um usuário válido associado ao dado de saúde
        Usuario usuario = usuarioRepository.findById(dadoSaude.getUsuario().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não Encontrado"));
        dadoSaude.setUsuario(usuario);

        dadoSaude.setId(id);
        dadoSaudeRepository.save(dadoSaude);
        return ResponseEntity.ok(dadoSaude);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deletar Dado de Saúde",
            description = "Deleta um dado de saúde por ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Dado de saúde deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não foi encontrado Dado de Saúde com esse ID"),
            @ApiResponse(responseCode = "403", description = "Token inválido")
    })
    public ResponseEntity<Object> deletarDadoSaude(@PathVariable Long id) {
        dadoSaudeRepository.findById(id).ifPresentOrElse(
                dadoSaudeRepository::delete,
                () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dado de Saúde não Encontrado");
                }
        );
        return ResponseEntity.noContent().build();
    }
}
