package com.saudesync.service;

import com.saudesync.model.ResultForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ServicoCriativo {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${saudesync.ml.host}")
    private String mlHost;

    @Value("${saudesync.ml.port}")
    private String mlPort;

    public ResultForm generateResult(MultipartFile multipartFile) throws IOException, InterruptedException {

        log.info("Convertendo arquivo multipartes");
        String path = "src/main/resources/" + multipartFile.getOriginalFilename();

        File file = new File(path);

        log.info("Salvando arquivo: " + path);
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(multipartFile.getBytes());
        }

        String endpoint = mlHost + ":" + mlPort + "/api/runmodel";
        log.info("Fazendo requisição no endpoint: " + endpoint);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.LEGACY);
        builder.addBinaryBody("pic", file);
        HttpEntity entity = builder.build();

        HttpPost post = new HttpPost(endpoint);
        post.setEntity(entity);

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(post)) {
            String responseString = new BasicHttpClientResponseHandler().handleResponse(response);
            log.info(responseString);

            log.info("Deletando arquivo temporário de: " + path);
            Files.delete(Path.of(path));

            return objectMapper.readValue(responseString, ResultForm.class);
        }
    }
}
