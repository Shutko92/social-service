package ru.skillbox.diplom.group42.social.service.controller.account;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group42.social.service.controller.base.BaseController;
import ru.skillbox.diplom.group42.social.service.dto.account.AccountDto;
import ru.skillbox.diplom.group42.social.service.dto.account.AccountSearchDto;

@RestController
@RequestMapping("/api/v1/account/")
public interface AccountController extends BaseController<AccountDto, AccountSearchDto> {

    @GetMapping("me")
    ResponseEntity<AccountDto> getAccount(); // Получение

    @DeleteMapping("/me")
    ResponseEntity<String> deleteAccount(); // Удаление

    @Override
    @PutMapping("/me")
    ResponseEntity<AccountDto> update(@RequestBody AccountDto dto); // Обновление

    @GetMapping(value = "/{id}")
    ResponseEntity<AccountDto> getById(@PathVariable Long id); // Получение по id
}
