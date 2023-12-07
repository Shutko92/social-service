package ru.skillbox.diplom.group42.social.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.skillbox.diplom.group42.social.service.dto.storage.StorageDto;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StorageControllerTest extends AbstractIntegrationTest{

    @BeforeAll
    public static void initEmail(){
        setEmailForCreateAccount("storage@mail.ru");
    }

    @Test
    void sendingToStorage() {
        File file = new File("./src/main/resources/java-logo-vector-1.svg");
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("file", new FileSystemResource(file));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        ResponseEntity<?> response = template.postForEntity("/api/v1/storage", new HttpEntity<>(parameters, headers), StorageDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}