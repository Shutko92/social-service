package ru.skillbox.diplom.group42.social.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skillbox.diplom.group42.social.service.dto.account.AccountDto;
import ru.skillbox.diplom.group42.social.service.dto.account.AccountOnlineDto;
import ru.skillbox.diplom.group42.social.service.entity.account.Account;
import ru.skillbox.diplom.group42.social.service.entity.auth.User;
import ru.skillbox.diplom.group42.social.service.mapper.account.AccountMapper;

import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.*;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.ONLINE_ACCOUNT_ID;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.TEST_ACCOUNT_ID;

public class AccountMapperTest {
    private final Account testAccount = createTestAccount(TEST_ACCOUNT_ID);
    private final AccountDto testAccountDto = createTestAccountDto(TEST_ACCOUNT_ID);
    private final AccountOnlineDto accountOnlineDto = createAccountOnlineDto(ONLINE_ACCOUNT_ID);
    private final AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);

    @Test
    public void funConvertToAccountDtoIsCorrect() {
        AccountDto accountDto = accountMapper.convertToAccountDto(accountOnlineDto, testAccountDto);
        Assertions.assertEquals(accountOnlineDto.getId(), accountDto.getId());
        Assertions.assertEquals(accountOnlineDto.getLastOnlineTime(), accountDto.getLastOnlineTime());
        Assertions.assertEquals(accountOnlineDto.getIsOnline(), accountDto.getIsOnline());
    }

    @Test
    public void funUserToAccountIsCorrect(){
        User user = createUser("TEST_USER");
        Account expectedAccount = accountCloneFromUser(user);
        Account mappedAccount = accountMapper.userToAccount(user);
        expectedAccount.setIsBlocked(mappedAccount.getIsBlocked());
        expectedAccount.setRegDate(mappedAccount.getRegDate());
        expectedAccount.setCreatedOn(mappedAccount.getCreatedOn());
        expectedAccount.setUpdatedOn(mappedAccount.getUpdatedOn());
        Assertions.assertEquals(expectedAccount, mappedAccount);
    }

    @Test
    public void funConvertToAccountIsCorrect(){
        Account actualAccount = accountMapper.convertToAccount(testAccountDto, testAccount);
        Assertions.assertEquals(actualAccount, testAccount);
    }
}
