package ru.skillbox.diplom.group42.social.service.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group42.social.service.dto.auth.AuthenticateDto;
import ru.skillbox.diplom.group42.social.service.dto.auth.AuthenticateResponseDto;
import ru.skillbox.diplom.group42.social.service.dto.auth.PasswordChangeDto;
import ru.skillbox.diplom.group42.social.service.dto.auth.RegistrationDto;
import ru.skillbox.diplom.group42.social.service.dto.captcha.CaptchaDto;
import ru.skillbox.diplom.group42.social.service.dto.email.recovery.EmailRecoveryDto;
import ru.skillbox.diplom.group42.social.service.util.ConstantURL;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "Аутентификация", description = "Регистрация и авторизация пользователя")
@RestController
@RequestMapping(ConstantURL.BASE_URL + "/auth/")
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
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticateResponseDto.class))}),
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
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CaptchaDto.class))
            })
    @GetMapping("captcha")
    ResponseEntity<CaptchaDto> getCaptchaImage();

    @Operation(summary = "Запрос на изменение почты", description = "Отправляет запрос на изменение почты")
    @ApiResponse(responseCode = "200", description = "Запрос на изменение почты отправлен", content = @Content)
    @ApiResponse(responseCode = "500", description = "Ошибка сервера при отправке почтового сообщения", content = @Content)
    @SecurityRequirement(name = "JWT")
    @PostMapping("change-email-link")
    void handlerRequestChangeEmail();

    @Operation(summary = "Подтверждение изменения почты по ссылке", description = "Получает подверждение о смене почты")
    @ApiResponse(responseCode = "200", description = "Email успешно обновлен", content = @Content)
    @ApiResponse(responseCode = "406", description = "Ссылка на смену email не действительна", content = @Content)
    @SecurityRequirement(name = "JWT")
    @PutMapping("email")
    HttpStatus confirmChangeEmail(HttpServletRequest request, @RequestBody EmailRecoveryDto dto);


    @Operation(summary = "Запрос на изменение пароля", description = "Отправляет запрос на изменение пароля")
    @ApiResponse(responseCode = "200", description = "Запрос на изменение пароля отправлен", content = @Content)
    @SecurityRequirement(name = "JWT")
    @PostMapping("change-password-link")
    HttpStatus handlerRequestChangePassword(@RequestBody PasswordChangeDto dto);
}
