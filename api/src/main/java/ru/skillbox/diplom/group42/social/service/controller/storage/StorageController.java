package ru.skillbox.diplom.group42.social.service.controller.storage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.group42.social.service.dto.storage.StorageDto;
import ru.skillbox.diplom.group42.social.service.util.ConstantURL;

import java.io.IOException;

@Tag(name = "Хранилище", description = "Операций с хранилищем")
@RestController
@RequestMapping(value = ConstantURL.BASE_URL + "/storage",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public interface StorageController {

    @Operation(summary = "Отправка файлов в хранилище", description = "Позволяет отправить файлы в хранилище")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Файл успешно загружен",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StorageDto.class))}),
            @ApiResponse(responseCode = "400", description = "Неварный тип файла", content = @Content),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен", content = @Content),
            @ApiResponse(responseCode = "413", description = "Объект запроса слишком большой", content = @Content)
    })
    @SecurityRequirement(name = "JWT")
    @PostMapping
    ResponseEntity<StorageDto> sendingToStorage(@RequestParam("file") MultipartFile imageFile) throws IOException;
}
