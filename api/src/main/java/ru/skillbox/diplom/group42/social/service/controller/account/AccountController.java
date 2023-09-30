package ru.skillbox.diplom.group42.social.service.controller.account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group42.social.service.controller.base.BaseController;
import ru.skillbox.diplom.group42.social.service.dto.account.AccountDto;
import ru.skillbox.diplom.group42.social.service.dto.account.AccountSearchDto;
@Tag(name = "Аккаунт", description = "Операции для работы с аккаунтом")
@RestController
@RequestMapping("/api/v1/account/")
public interface AccountController extends BaseController<AccountDto, AccountSearchDto> {

    @Operation(summary = "Получение аккаунта", description = "Позволяет получить аккаунт")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аккаунт успешно получен",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен", content = @Content)
    })
    @SecurityRequirement(name = "JWT")
    @GetMapping("me")
    ResponseEntity<AccountDto> getAccount();


    @Operation(summary = "Удаление аккаунта", description = "Позволяет удалить аккаунт")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аккаунт успешно удален",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен", content = @Content)
    })
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/me")
    ResponseEntity<String> deleteAccount(); // Удаление


    @Operation(summary = "Обновление аккаунта", description = "Позволяет обновить аккаунт")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аккаунт успешно обновлен",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Данные введены неверно", content = @Content),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен", content = @Content)
    })
    @SecurityRequirement(name = "JWT")
    @Override
    @PutMapping("/me")
    ResponseEntity<AccountDto> update(@RequestBody AccountDto dto);


    @Operation(summary = "Получение аккаунта по id", description = "Позволяет получить аккаунт по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аккаунт успешно получен",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Данные введены неверно", content = @Content),
            @ApiResponse(responseCode = "404", description = "Аккаунт не найден", content = @Content),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен", content = @Content)
    })
    @SecurityRequirement(name = "JWT")
    @GetMapping(value = "/{id}")
    ResponseEntity<AccountDto> getById(@PathVariable Long id);
}
