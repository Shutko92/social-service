package ru.skillbox.diplom.group42.social.service.controller.account;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group42.social.service.dto.account.AccountDto;
import ru.skillbox.diplom.group42.social.service.dto.account.AccountSearchDto;
import ru.skillbox.diplom.group42.social.service.service.account.AccountService;

@RestController
@RequiredArgsConstructor
public class AccountControllerImpl implements AccountController {

    private final AccountService accountService;

    @Override
    public ResponseEntity<AccountDto> getAccount() {
        return ResponseEntity.ok(accountService.getAccount());
    }

    @Override
    public ResponseEntity<AccountDto> getById(Long id) {
        return new ResponseEntity<>(accountService.getAccountById(id), HttpStatus.OK);
    }

    @Hidden
    @Override
    public ResponseEntity<Page<AccountDto>> getAll(AccountSearchDto searchDto, Pageable page) {
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    public ResponseEntity<Page<AccountDto>> search(AccountSearchDto dto, Pageable pageable) {
        return ResponseEntity.ok(accountService.searchAccount(dto, pageable));
    }


    @Override
    public ResponseEntity<Page<AccountDto>> searchByStatus(AccountSearchDto dto, Pageable pageable) {
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<AccountDto> update(AccountDto accountDto) {
        return new ResponseEntity<>(accountService.updateAccount(accountDto), HttpStatus.OK);
    }

    @Hidden
    @Override
    public ResponseEntity<AccountDto> create(AccountDto dto) {
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    public ResponseEntity<String> deleteAccount() {
        return new ResponseEntity<>(accountService.deleteAccount(), HttpStatus.OK);
    }

    @Hidden
    @Override
    public ResponseEntity<String> deleteById(Long id) {
        accountService.deleteById(id);
        return ResponseEntity.ok("User with id: " + id + " deleted");
    }
}
