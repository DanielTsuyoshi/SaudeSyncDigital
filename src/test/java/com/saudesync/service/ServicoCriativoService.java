package com.saudesync.service;

import com.saudesync.model.ResultForm;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ServicoCriativoTest {

    @Autowired
    ServicoCriativo servicoCriativo;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Test
    // # Testa o generateResult
    // — Cria um MultipartFile simulado
    // — Faz a chamada no serviço de ML com o MultipartFile
    // — Verifica se um resultado foi retornado
    // — Verifica se o resultado é esperado
    public void testGenerateResult() throws InterruptedException {
        // Criando um MultipartFile simulado
        MultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "text/plain", "Test Content".getBytes());

        ResultForm resultForm = servicoCriativo.generateResult(multipartFile);

        assertNotNull(resultForm);
        // Modifique conforme o resultado esperado
        assertEquals("resultadoEsperado", resultForm.getResult());
    }
}
