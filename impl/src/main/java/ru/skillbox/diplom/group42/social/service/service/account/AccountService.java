package ru.skillbox.diplom.group42.social.service.service.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group42.social.service.dto.account.AccountDto;
import ru.skillbox.diplom.group42.social.service.dto.account.AccountSearchDto;
import ru.skillbox.diplom.group42.social.service.entity.account.Account;
import ru.skillbox.diplom.group42.social.service.entity.account.Account_;
import ru.skillbox.diplom.group42.social.service.entity.auth.User;
import ru.skillbox.diplom.group42.social.service.entity.auth.User_;
import ru.skillbox.diplom.group42.social.service.entity.notification.NotificationSettings;
import ru.skillbox.diplom.group42.social.service.mapper.account.AccountMapper;
import ru.skillbox.diplom.group42.social.service.repository.account.AccountRepository;
import ru.skillbox.diplom.group42.social.service.repository.notification.NotificationSettingsRepository;
import ru.skillbox.diplom.group42.social.service.utils.security.SecurityUtil;

import java.time.ZonedDateTime;
import java.util.Collections;

import static ru.skillbox.diplom.group42.social.service.utils.SpecificationUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final NotificationSettingsRepository notificationSettingsRepository;

    public AccountDto getAccount() {
        Long id = SecurityUtil.getJwtUserIdFromSecurityContext();
        log.info("AccountService method getAccount() executed. Account id: {}", id);

        return getAccountById(id);
    }

    public AccountDto getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new BadCredentialsException("AccountService method getAccountById(): User with id " + id + " does not exist"));

        log.info("AccountService method getAccountById(Long id): Getting account with id: {}", account.getId());
        return accountMapper.convertToDto(account);
    }

    public Page<AccountDto> searchAccount(AccountSearchDto accountSearchDto, Pageable page) {
        log.info("AccountService method search(AccountSearchDto accountSearchDto, Pageable page) executed" + accountSearchDto.toString());
        if (accountSearchDto.getAuthor() != null) {
            return accountRepository.findAll(getSpecificationByAuthor(accountSearchDto), page).map(accountMapper::convertToDto);
        } else {
            return accountRepository.findAll(getSpecificationByOtherFields(accountSearchDto), page).map(accountMapper::convertToDto);
        }
    }

    public AccountDto updateAccount(AccountDto accountDto) {
        log.debug("Entering AccountService method Account updateAccount");
        Long userId = SecurityUtil.getJwtUserIdFromSecurityContext();

        Account accountToSave = accountMapper.convertToAccount(
                accountDto,
                accountRepository.findById(userId).orElse(
                        new Account()
                ));
        accountRepository.save(accountToSave);

        log.info("AccountService method update(AccountDto accountDto) executed. Account " + accountToSave + " updated");
        return accountMapper.convertToDto(accountToSave);
    }

    public void createAccount(User user) {
        log.debug("Entering AccountService method createAccount(User user)");
        log.info("AccountService method createAccount(User user)");
        Account account = accountMapper.userToAccount(user);
         accountRepository.save(account);

        createNotificationsSettings(account.getId());

        log.info("AccountService method createAccount(User user): Account {} successfully saved",
                user.toString()
        );

        log.debug("Exiting AccountService method createAccount(User user)");
    }

    private static Specification<Account> getSpecificationByAuthor(AccountSearchDto accountSearchDto) {
        log.debug("Entering getSpecificationByAuthor method");
        return getBaseSpecification(accountSearchDto)
                .and(notIn(Account_.id, accountSearchDto.getBlockedByIds(), true))
                .and(likeToLower(Account_.firstName, accountSearchDto.getAuthor(), true)
                        .and(notIn(User_.email, Collections.singleton(SecurityUtil.getJwtUserFromSecurityContext().getEmail()), true))
                        .or(likeToLower(Account_.lastName, accountSearchDto.getAuthor(), true)
                                .and(notIn(User_.email, Collections.singleton(SecurityUtil.getJwtUserFromSecurityContext().getEmail()), true))));

    }

    private static Specification<Account> getSpecificationByOtherFields(AccountSearchDto dto) {
        log.debug("Entering AccountService getSpecificationByAuthor method");

        return getBaseSpecification(dto)
                .and(notIn(Account_.id, dto.getBlockedByIds(), true))
                .and(in(Account_.id, dto.getIds(), true))
                .and(likeToLower(Account_.firstName, dto.getFirstName(), true)
                        .and(likeToLower(Account_.lastName, dto.getLastName(), true)
                                .and(notIn(User_.email, Collections.singleton(SecurityUtil.getJwtUserFromSecurityContext().getEmail()), true))))
                .and(equal(Account_.country, dto.getCountry(), true))
                .and(equal(Account_.city, dto.getCity(), true))
                .and(between(Account_.birthDate,
                        dto.getAgeTo() == null ? null : ZonedDateTime.now().minusYears(dto.getAgeTo()),
                        dto.getAgeFrom() == null ? null : ZonedDateTime.now().minusYears(dto.getAgeFrom()), true));
    }

    public void deleteById(Long id) {
        log.debug("Entering AccountService method deleteById(), id: {}", id);
        log.info("AccountService method deleteById(Long id) executed, id: {}", id);
        accountRepository.deleteById(id);
    }

    public String deleteAccount() {
        log.debug("Entering AccountService method deleteAccount()");
        Long id = SecurityUtil.getJwtUserIdFromSecurityContext();
        Account account = accountRepository.findById(id).get();
        deleteById(account.getId());

        log.info("AccountService method deleteAccount(), Account with id: {} deleted.", id);
        return "Account with id: " + id + " deleted.";
    }


    private void createNotificationsSettings(Long accountId) {
        NotificationSettings notificationSettings = new NotificationSettings();
        notificationSettings.setAccountId(accountId);
        notificationSettings.setEnableMessage(true);
        notificationSettings.setEnablePost(true);
        notificationSettings.setEnableFriendBirthday(true);
        notificationSettings.setEnableSendEmailMessage(true);
        notificationSettings.setEnableCommentComment(true);
        notificationSettings.setEnableFriendRequest(true);
        notificationSettings.setEnablePostComment(true);
        notificationSettings.setIsDeleted(false);
        notificationSettingsRepository.save(notificationSettings);

    }

}