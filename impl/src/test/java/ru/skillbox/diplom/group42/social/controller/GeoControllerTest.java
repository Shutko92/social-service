package ru.skillbox.diplom.group42.social.controller;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.util.List;
import java.util.Objects;

public class GeoControllerTest extends AbstractIntegrationTest {
    @BeforeAll
    public static void initEmail(){
        setEmailForCreateAccount("geo@mail.ru");
    }

    @Test
    public void getCountriesShouldReturnListCountyDto() {
        ResponseEntity<List> response = template.exchange(
                "/api/v1/geo/country",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                List.class
        );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(
                MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString()
        );
        Assertions.assertTrue(response.hasBody());
    }

    @Test
    public void getCitiesShouldReturnListCountyDto() {
        ResponseEntity<List> response = template.exchange(
                "/api/v1/geo/country/{countryId}/city",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                List.class,
                1
        );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(
                MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString()
        );
        Assertions.assertTrue(response.hasBody());
    }
}
