package ru.skillbox.diplom.group42.social.service.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group42.social.service.dto.auth.AuthenticateDto;
import ru.skillbox.diplom.group42.social.service.dto.auth.AuthenticateResponseDto;
import ru.skillbox.diplom.group42.social.service.dto.auth.RegistrationDto;
import ru.skillbox.diplom.group42.social.service.dto.captcha.CaptchaDto;

@Tag(name = "Аутентификация", description = "Регистрация и авторизация пользователя")
@RestController
@RequestMapping("/api/v1/auth/")
public interface AuthController {

    @Operation(summary = "Регистрация пользователя", description = "Позволяет зарегистрировать пользователя")
    @ApiResponses(value = {
           @ApiResponse(responseCode = "200", description = "Регистрация прошла успешно", content = @Content),
           @ApiResponse(responseCode = "400", description = "Данные введены неверно", content = @Content)
    })
    @PostMapping("register")
    void register(@RequestBody RegistrationDto registrationDto);


    @Operation(summary = "Авторизация пользователя", description = "Позволяет авторизовать пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Авторизация прошла успешно",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticateResponseDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Данные введены неверно", content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content),
    })
    @PostMapping("login")
    ResponseEntity<AuthenticateResponseDto> login(@RequestBody AuthenticateDto authenticateDto);


    @Operation(summary = "Выход из аккаунта", description = "Позволяет выйти из аккаунта")
    @ApiResponse(responseCode = "200", description = "Выход из аккаунта выполнен успешно", content = @Content)
    @PostMapping("logout")
    void logout();


    @Operation(summary = "Получение изображения капчи", description = "Позволяет получить изображение капчи")
    @ApiResponse(responseCode = "200", description = "Изображение капчи успешно получено",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CaptchaDto.class))
    })
    @GetMapping("captcha")
    ResponseEntity<CaptchaDto> getCaptchaImage();
}
