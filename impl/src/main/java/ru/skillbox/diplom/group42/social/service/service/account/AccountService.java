package ru.skillbox.diplom.group42.social.service.service.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group42.social.service.dto.account.AccountDto;
import ru.skillbox.diplom.group42.social.service.entity.account.Account;
import ru.skillbox.diplom.group42.social.service.entity.auth.User;
import ru.skillbox.diplom.group42.social.service.mapper.account.AccountMapper;
import ru.skillbox.diplom.group42.social.service.repository.account.AccountRepository;
import ru.skillbox.diplom.group42.social.service.utils.security.SecurityUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountDto getAccount() {
        Long id = SecurityUtil.getJwtUserIdFromSecurityContext();
        return getAccountById(id);
    }

    public void createAccount(User user) {
        log.info("IN createAccount(User user) in AccountService");
        Account account = accountMapper.userToAccount(user);
        accountRepository.save(account);

        log.info("Account {} - {} {} [id: {}] successfully saved",
                account.getEmail(),
                account.getFirstName(),
                account.getLastName(),
                account.getId()
        );
    }

    public AccountDto getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new BadCredentialsException("User with id " + id + " does not exist"));

        log.info("IN AccountDto getAccountById getting account {}", account.getId());
        return accountMapper.convertToDto(account);
    }

    public AccountDto updateAccount(AccountDto accountDto) {
        Long userId = SecurityUtil.getJwtUserIdFromSecurityContext();

        Account accountToSave = accountMapper.convertToAccount(
                accountDto,
                accountRepository.findById(userId).orElse(
                        new Account()
                        ));
        accountRepository.save(accountToSave);
        log.info("Account " + accountToSave + " updated");
        return accountMapper.convertToDto(accountToSave);
    }

    public String deleteAccount() {
        Long id = SecurityUtil.getJwtUserIdFromSecurityContext();
        Account account = accountRepository.findById(id).get();
        deleteById(account.getId());

        log.info("Account with id: " + id + " deleted.");
        return "Account with id: " + id + " deleted.";
    }

    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }
}
