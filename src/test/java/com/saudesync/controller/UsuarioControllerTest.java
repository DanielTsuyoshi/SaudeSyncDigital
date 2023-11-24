package com.saudesync.controller;

import com.saudesync.model.Credencial;
import com.saudesync.model.Token;
import com.saudesync.model.Usuario;
import com.saudesync.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsuarioControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder encoder;

    private final ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();

    private final Logger log = LoggerFactory.getLogger(getClass());

    @BeforeEach
    public void setUp() {
        usuarioRepository.deleteAll(); // Limpa o banco antes de cada teste
    }

    @AfterEach
    public void tearDown() {
        usuarioRepository.deleteAll(); // Limpa o banco após cada teste
    }

    private Token createToken() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Nome Teste");
        usuario.setCpf("01699531064");
        usuario.setEmail("teste@email.com");
        usuario.setSenha("senhaTeste");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> requestEntityCadastro = new HttpEntity<>(objectMapper.writeValueAsString(usuario), headers);
        ResponseEntity<String> responseCadastro = restTemplate.exchange(
                "http://localhost:" + port + "/saudesync/api/usuario",
                HttpMethod.POST,
                requestEntityCadastro,
                String.class);

        Credencial credencial = new Credencial(usuario.getEmail(), usuario.getSenha());
        HttpEntity<String> requestEntityLogin = new HttpEntity<>(objectMapper.writeValueAsString(credencial), headers);
        ResponseEntity<String> responseLogin = restTemplate.exchange(
                "http://localhost:" + port + "/saudesync/api/usuario/login",
                HttpMethod.POST,
                requestEntityLogin,
                String.class);

        return objectMapper.readValue(responseLogin.getBody(), Token.class);
    }

    @Test
    void withCreateToken_shouldCreateUsuarioAndToken() throws Exception {
        // Cria um token e um usuário
        var token = createToken();
        var usuario = usuarioRepository.findById(1L);

        log.info(token.toString());
        log.info(usuario.toString());

        assertNotNull(token);
        assertNotNull(usuario);
    }

    @Test
    void givenCredentials_whenLogin_shouldReturnToken() throws Exception {
        // Dado credenciais válidas, verifica se o token é retornado após o login
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        Usuario usuario = new Usuario();
        usuario.setNome("Nome Teste");
        usuario.setCpf("01699531064");
        usuario.setEmail("teste@email.com");
        usuario.setSenha(encoder.encode("senhaTeste"));
        usuarioRepository.save(usuario);

        Credencial credentials = new Credencial("teste@email.com", "senhaTeste");

        String requestBody = objectMapper.writeValueAsString(credentials);

        log.info(requestBody);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        String baseUrl = "http://localhost:" + port + "/saudesync/api/usuario/login";
        ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.POST, requestEntity, String.class);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(objectMapper.readValue(response.getBody(), Token.class));
    }

    @Test
    void givenValidUsuario_whenCreateUsuario_shouldReturnCreatedUsuario() {
        // Dado um usuário válido, verifica se o usuário é criado corretamente
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        Usuario usuario = new Usuario();
        usuario.setNome("Nome Teste");
        usuario.setCpf("01699531064");
        usuario.setEmail("teste@email.com");
        usuario.setSenha("senhaTeste");

        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(usuario), headers);
        ResponseEntity<Usuario> response = restTemplate.exchange(
                "http://localhost:" + port + "/saudesync/api/usuario",
                HttpMethod.POST,
                requestEntity,
                Usuario.class);

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(usuario.getNome(), response.getBody().getNome());
        assertEquals(usuario.getEmail(), response.getBody().getEmail());
    }

    @Test
    void givenExistingUsuario_whenGetUsuarioById_shouldReturnUsuario() {
        // Dado um usuário existente, verifica se é possível recuperá-lo pelo ID
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        Usuario usuario = new Usuario();
        usuario.setNome("Nome Teste");
        usuario.setCpf("01699531064");
        usuario.setEmail("teste@email.com");
        usuario.setSenha("senhaTeste");
        Usuario savedUsuario = usuarioRepository.save(usuario);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Usuario> response = restTemplate.exchange(
                "http://localhost:" + port + "/saudesync/api/usuario/" + savedUsuario.getId(),
                HttpMethod.GET,
                requestEntity,
                Usuario.class);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(savedUsuario.getNome(), response.getBody().getNome());
        assertEquals(savedUsuario.getEmail(), response.getBody().getEmail());
    }

    @Test
    void givenNonExistingUsuarioId_whenGetUsuarioById_shouldReturnNotFound() {
        // Dado um ID de usuário inexistente, verifica se a resposta é 404
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/saudesync/api/usuario/999",
                HttpMethod.GET,
                requestEntity,
                Void.class);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void givenExistingUsuario_whenUpdateUsuario_shouldReturnUpdatedUsuario() {
        // Dado um usuário existente, verifica se é possível atualizá-lo corretamente
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        Usuario usuario = new Usuario();
        usuario.setNome("Nome Teste");
        usuario.setCpf("01699531064");
        usuario.setEmail("teste@email.com");
        usuario.setSenha("senhaTeste");
        Usuario savedUsuario = usuarioRepository.save(usuario);

        savedUsuario.setNome("Novo Nome");
        savedUsuario.setEmail("novo@email.com");

        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(savedUsuario), headers);
        ResponseEntity<Usuario> response = restTemplate.exchange(
                "http://localhost:" + port + "/saudesync/api/usuario/" + savedUsuario.getId(),
                HttpMethod.PUT,
                requestEntity,
                Usuario.class);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Novo Nome", response.getBody().getNome());
        assertEquals("novo@email.com", response.getBody().getEmail());
    }

    @Test
    void givenExistingUsuario_whenDeleteUsuario_shouldReturnNoContent() {
        // Dado um usuário existente, verifica se é possível excluí-lo corretamente
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        Usuario usuario = new Usuario();
        usuario.setNome("Nome Teste");
        usuario.setCpf("01699531064");
        usuario.setEmail("teste@email.com");
        usuario.setSenha("senhaTeste");
        Usuario savedUsuario = usuarioRepository.save(usuario);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/saudesync/api/usuario/" + savedUsuario.getId(),
                HttpMethod.DELETE,
                requestEntity,
                Void.class);

        assertEquals(204, response.getStatusCode().value());
        assertFalse(usuarioRepository.findById(savedUsuario.getId()).isPresent());
    }

    @Test
    void getAllUsuarios_shouldReturnListOfUsuarios() {
        // Verifica se é possível recuperar a lista de usuários
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        Usuario usuario1 = new Usuario();
        usuario1.setNome("Nome Teste 1");
        usuario1.setCpf("01699531064");
        usuario1.setEmail("teste1@email.com");
        usuario1.setSenha("senhaTeste1");

        Usuario usuario2 = new Usuario();
        usuario2.setNome("Nome Teste 2");
        usuario2.setCpf("01234567890");
        usuario2.setEmail("teste2@email.com");
        usuario2.setSenha("senhaTeste2");

        usuarioRepository.saveAll(List.of(usuario1, usuario2));

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Usuario[]> response = restTemplate.exchange(
                "http://localhost:" + port + "/saudesync/api/usuario",
                HttpMethod.GET,
                requestEntity,
                Usuario[].class);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
    }
}
