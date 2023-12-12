package ru.skillbox.diplom.group42.social.service.controller.account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group42.social.service.controller.base.BaseController;
import ru.skillbox.diplom.group42.social.service.dto.account.AccountDto;
import ru.skillbox.diplom.group42.social.service.dto.account.AccountSearchDto;
import ru.skillbox.diplom.group42.social.service.util.ConstantURL;

@Tag(name = "Аккаунт", description = "Операций с аккаунтом")
@RestController
@RequestMapping(ConstantURL.BASE_URL + "/account/")
public interface AccountController extends BaseController<AccountDto, AccountSearchDto> {

    @Operation(summary = "Получение аккаунта", description = "Позволяет получить аккаунт")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аккаунт успешно получен",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDto.class))}),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен", content = @Content)
    })
    @SecurityRequirement(name = "JWT")
    @GetMapping("me")
    ResponseEntity<AccountDto> getAccount();


    @Operation(summary = "Удаление аккаунта", description = "Позволяет удалить аккаунт")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аккаунт успешно удален",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен", content = @Content)
    })
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/me")
    ResponseEntity<String> deleteAccount();


    @Operation(summary = "Обновление аккаунта", description = "Позволяет обновить аккаунт")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аккаунт успешно обновлен",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDto.class))}),
            @ApiResponse(responseCode = "400", description = "Данные введены неверно", content = @Content),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен", content = @Content)
    })
    @SecurityRequirement(name = "JWT")
    @Override
    @PutMapping("/me")
    ResponseEntity<AccountDto> update(@RequestBody AccountDto dto);


    @Operation(summary = "Получение списка аккаунтов", description = "Позволяет получить список аккаунтов по заданным параметрам с использованием спецификации")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аккаунт найден",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountSearchDto.class))}),
            @ApiResponse(responseCode = "400", description = "Данные введены неверно", content = @Content),
            @ApiResponse(responseCode = "401", description = "Доступ запрещен", content = @Content)
    })
    @GetMapping(value = "search")
    ResponseEntity<Page<AccountDto>> search(AccountSearchDto dto, Pageable pageable);

    @GetMapping(value = "/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получение аккаунта по id", description = "Позволяет получить аккаунт по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аккаунт успешно получен",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountSearchDto.class))}),
            @ApiResponse(responseCode = "400", description = "Данные введены неверно", content = @Content),
            @ApiResponse(responseCode = "404", description = "Аккаунт не найден", content = @Content),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен", content = @Content)
    })
    ResponseEntity<AccountDto> getById(@PathVariable Long id);


    @SecurityRequirement(name = "JWT")
    @GetMapping(value = "search/statusCode")
    @Operation(summary = "Получение списка аккаунтов относительно запрашиваемого статуса", description = "Позволяет получать аккаунты относительно запрашиваемого статуса")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные успешно получены",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountSearchDto.class))}),
            @ApiResponse(responseCode = "400", description = "Данные введены неверно", content = @Content),
            @ApiResponse(responseCode = "400", description = "Доступ запрещен", content = @Content)
    })
    ResponseEntity<Page<AccountDto>> searchByStatus(AccountSearchDto dto, Pageable pageable);
}
