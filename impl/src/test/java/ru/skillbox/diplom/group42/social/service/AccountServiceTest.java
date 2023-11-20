package ru.skillbox.diplom.group42.social.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import ru.skillbox.diplom.group42.social.service.dto.account.AccountDto;
import ru.skillbox.diplom.group42.social.service.dto.account.AccountSearchDto;
import ru.skillbox.diplom.group42.social.service.entity.account.Account;
import ru.skillbox.diplom.group42.social.service.entity.auth.User;
import ru.skillbox.diplom.group42.social.service.mapper.account.AccountMapper;
import ru.skillbox.diplom.group42.social.service.repository.account.AccountRepository;
import ru.skillbox.diplom.group42.social.service.repository.notification.NotificationSettingsRepository;
import ru.skillbox.diplom.group42.social.service.security.JwtUser;
import ru.skillbox.diplom.group42.social.service.service.account.AccountService;
import ru.skillbox.diplom.group42.social.service.utils.security.SecurityUtil;
import ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.createTestAccountDto;
import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.createUser;
import static ru.skillbox.diplom.group42.social.util.ServiceTestingDataFactory.createAccountSearchDto;
import static ru.skillbox.diplom.group42.social.util.ServiceTestingDataFactory.createJwtUser;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.TEST_ACCOUNT_ID;


@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountMapper accountMapper;
    @Mock
    private NotificationSettingsRepository notificationSettingsRepository;
    private AccountService accountService;
    private final Account account = MappingTestingDataFactory.createTestAccount(TEST_ACCOUNT_ID);


    @BeforeEach
    public void beforeMethod(TestInfo info) {
        accountService = new AccountService(accountRepository, accountMapper, notificationSettingsRepository);
        if (
                info.getDisplayName().equals("getAccountByIdShouldThrowException") ||
                        info.getDisplayName().equals("updateAccountShouldInvokeAccountRepositoryFindById") ||
                        info.getDisplayName().equals("funCreateAccountInvokeAccountMapperUserToAccount") ||
                        info.getDisplayName().equals("funCreateAccountInvokeAccountAccountRepositorySave") ||
                        info.getDisplayName().equals("fundDeleteByIdShouldInvokerAccountRepositoryDeleteById") ||
                        info.getDisplayName().equals("funDeleteAccountShouldInvokeAccountRepositoryFindById") ||
                        info.getDisplayName().equals("funSearchAccountShouldInvokeAccountRepositoryFindAll") ||
                        info.getDisplayName().equals("funSearchAccountShouldInvokeAccountRepositoryFindAllAuthorNUll")
        ) {
            return;
        }
        when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(accountMapper.convertToDto(account)).thenReturn(new AccountDto());
    }

    @Test
    public void funGetAccountShouldReturnAccountType(){
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);
            AccountDto accountDto = accountService.getAccount();
            assert accountDto != null;
        }
    }
    @Test
    @DisplayName("funSearchAccountShouldInvokeAccountRepositoryFindAll")
    public void funSearchAccountShouldInvokeAccountRepositoryFindAll() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            JwtUser user = createJwtUser();
            utilities.when(SecurityUtil::getJwtUserFromSecurityContext).thenReturn(user);
            List<AccountDto> dtoList = new ArrayList<>();
            Page<AccountDto> dtoPage = new PageImpl<>(dtoList);
            when(accountRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(dtoPage);
            AccountSearchDto searchDto = createAccountSearchDto();
            accountService.searchAccount(searchDto, Pageable.ofSize(2));
            verify(accountRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        }
    }
    @Test
    @DisplayName("funSearchAccountShouldInvokeAccountRepositoryFindAllAuthorNUll")
    public void funSearchAccountShouldInvokeAccountRepositoryFindAllAuthorNUll() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            JwtUser user = createJwtUser();
            utilities.when(SecurityUtil::getJwtUserFromSecurityContext).thenReturn(user);
            List<AccountDto> dtoList = new ArrayList<>();
            Page<AccountDto> dtoPage = new PageImpl<>(dtoList);
            when(accountRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(dtoPage);
            AccountSearchDto searchDto = createAccountSearchDto();
            searchDto.setAuthor(null);
            accountService.searchAccount(searchDto, Pageable.ofSize(2));
            verify(accountRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        }
    }

    @Test
    @DisplayName("fundDeleteByIdShouldInvokerAccountRepositoryDeleteById")
    public void fundDeleteByIdShouldInvokerAccountRepositoryDeleteById() {
        accountService.deleteById(TEST_ACCOUNT_ID);
        verify(accountRepository, times(1)).deleteById(TEST_ACCOUNT_ID);
    }

    @Test
    @DisplayName("funDeleteAccountShouldInvokeAccountRepositoryFindById")
    public void funDeleteAccountShouldInvokeAccountRepositoryFindById() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);
            when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Optional.of(account));
            accountService.deleteAccount();
            verify(accountRepository, times(1)).findById(TEST_ACCOUNT_ID);
        }
    }

    @Test
    @DisplayName("funCreateAccountInvokeAccountMapperUserToAccount")
    public void funCreateAccountInvokeAccountMapperUserToAccount() {
        User user = createUser("TEST USER");
        when(accountMapper.userToAccount(user)).thenReturn(account);
        accountService.createAccount(user);
        verify(accountMapper, times(1)).userToAccount(user);
    }

    @Test
    @DisplayName("funCreateAccountInvokeAccountAccountRepositorySave")
    public void funCreateAccountInvokeAccountAccountRepositorySave() {
        User user = createUser("TEST USER");
        when(accountMapper.userToAccount(user)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);
        accountService.createAccount(user);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    @DisplayName("updateAccountShouldInvokeAccountRepositoryFindById")
    public void updateAccountShouldInvokeAccountRepositoryFindById() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);

            AccountDto accountDto = createTestAccountDto(TEST_ACCOUNT_ID);
            accountService.updateAccount(accountDto);
            verify(accountRepository, times(1)).findById(TEST_ACCOUNT_ID);
        }
    }

    @Test
    public void updateAccountShouldInvokeAccountRepositorySave() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);
            AccountDto accountDto = createTestAccountDto(TEST_ACCOUNT_ID);
            when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Optional.of(account));
            when(accountMapper.convertToAccount(accountDto, account)).thenReturn(account);
            accountService.updateAccount(accountDto);
            verify(accountRepository, times(1)).save(account);
        }
    }

    @Test
    public void getAccountShouldInvokeAccountMapperConvertToAccount() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);
            AccountDto accountDto = createTestAccountDto(TEST_ACCOUNT_ID);
            when(accountMapper.convertToAccount(accountDto, account)).thenReturn(account);
            accountService.updateAccount(accountDto);
            verify(accountMapper, times(1)).convertToAccount(accountDto, account);
        }
    }

    @Test
    public void getAccountShouldInvokeAccountMapperConvertToDto() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);
            AccountDto accountDto = createTestAccountDto(TEST_ACCOUNT_ID);
            when(accountMapper.convertToAccount(accountDto, account)).thenReturn(account);
            when(accountMapper.convertToDto(account)).thenReturn(accountDto);
            accountService.updateAccount(accountDto);
            verify(accountMapper, times(1)).convertToDto(account);
        }
    }

    @Test
    public void getAccountShouldInvokeAccountRepositoryFindById() {
        try (MockedStatic<SecurityUtil> utilities = Mockito.mockStatic(SecurityUtil.class)) {
            utilities.when(SecurityUtil::getJwtUserIdFromSecurityContext).thenReturn(TEST_ACCOUNT_ID);
            accountService.getAccountById(TEST_ACCOUNT_ID);
            verify(accountRepository, times(1)).findById(TEST_ACCOUNT_ID);
        }
    }

    @Test
    public void getAccountByIdShouldInvokeAccountRepositoryFindById() {
        accountService.getAccountById(TEST_ACCOUNT_ID);
        verify(accountRepository, times(1)).findById(TEST_ACCOUNT_ID);
    }

    @Test
    public void getAccountByIdShouldInvokeAccountMapperConvertToDto() {
        accountService.getAccountById(TEST_ACCOUNT_ID);
        verify(accountMapper, times(1)).convertToDto(account);
    }

    @Test //expected = BadCredentialsException.class
    @DisplayName("getAccountByIdShouldThrowException")
    public void getAccountByIdShouldThrowException() {
        Throwable thrown = assertThrows(BadCredentialsException.class, () -> {
            accountService.getAccountById(TEST_ACCOUNT_ID);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    public void getAccountByIdShouldReturnAccountDtoType() {
        AccountDto accountDto = accountService.getAccountById(TEST_ACCOUNT_ID);
        assert accountDto != null;
    }
}
