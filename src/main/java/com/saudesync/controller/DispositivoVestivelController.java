package com.saudesync.controller;

import com.saudesync.model.DispositivoVestivel;
import com.saudesync.repository.DispositivoVestivelRepository;
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
@RequestMapping("/saudesync/api/dispositivovestivel")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "DispositivoVestivel", description = "Dispositivos Vestíveis do Usuário")
public class DispositivoVestivelController {

    @Autowired
    DispositivoVestivelRepository dispositivoVestivelRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @PostMapping
    @Operation(
            summary = "Cadastro de Dispositivo Vestível",
            description = "Cadastra um novo dispositivo vestível"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dispositivo vestível cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou faltando")
    })
    public ResponseEntity<Object> cadastrarDispositivoVestivel(@RequestBody @Valid DispositivoVestivel dispositivoVestivel) {
        // Certifique-se de ter um usuário válido associado ao dispositivo vestível
        Usuario usuario = usuarioRepository.findById(dispositivoVestivel.getUsuario().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não Encontrado"));
        dispositivoVestivel.setUsuario(usuario);

        dispositivoVestivelRepository.save(dispositivoVestivel);
        return ResponseEntity.status(HttpStatus.CREATED).body(dispositivoVestivel);
    }

    @GetMapping
    @Operation(
            summary = "Listar Dispositivos Vestíveis",
            description = "Retorna todos os dispositivos vestíveis cadastrados"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dispositivos vestíveis listados com sucesso"),
            @ApiResponse(responseCode = "403", description = "Token inválido")
    })
    public ResponseEntity<List<DispositivoVestivel>> listarDispositivosVestiveis() {
        List<DispositivoVestivel> dispositivos = dispositivoVestivelRepository.findAll();
        return ResponseEntity.ok(dispositivos);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Detalhar Dispositivo Vestível",
            description = "Busca um dispositivo vestível por ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dispositivo vestível detalhado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não foi encontrado Dispositivo Vestível com esse ID"),
            @ApiResponse(responseCode = "403", description = "Token inválido")
    })
    public ResponseEntity<DispositivoVestivel> detalharDispositivoVestivel(@PathVariable Long id) {
        DispositivoVestivel dispositivo = dispositivoVestivelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dispositivo Vestível não Encontrado"));
        return ResponseEntity.ok(dispositivo);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar Dispositivo Vestível",
            description = "Atualiza um dispositivo vestível por ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dispositivo vestível atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou faltando"),
            @ApiResponse(responseCode = "404", description = "Não foi encontrado Dispositivo Vestível com esse ID"),
            @ApiResponse(responseCode = "403", description = "Token inválido")
    })
    public ResponseEntity<DispositivoVestivel> atualizarDispositivoVestivel(@PathVariable Long id, @RequestBody @Valid DispositivoVestivel dispositivoVestivel) {
        // Certifique-se de ter um usuário válido associado ao dispositivo vestível
        Usuario usuario = usuarioRepository.findById(dispositivoVestivel.getUsuario().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não Encontrado"));
        dispositivoVestivel.setUsuario(usuario);

        dispositivoVestivel.setId(id);
        dispositivoVestivelRepository.save(dispositivoVestivel);
        return ResponseEntity.ok(dispositivoVestivel);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deletar Dispositivo Vestível",
            description = "Deleta um dispositivo vestível por ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Dispositivo vestível deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não foi encontrado Dispositivo Vestível com esse ID"),
            @ApiResponse(responseCode = "403", description = "Token inválido")
    })
    public ResponseEntity<Object> deletarDispositivoVestivel(@PathVariable Long id) {
        dispositivoVestivelRepository.findById(id).ifPresentOrElse(
                dispositivoVestivelRepository::delete,
                () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dispositivo Vestível não Encontrado");
                }
        );
        return ResponseEntity.noContent().build();
    }
}
