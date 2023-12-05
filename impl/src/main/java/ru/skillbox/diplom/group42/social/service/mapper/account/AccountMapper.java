package ru.skillbox.diplom.group42.social.service.mapper.account;

import org.mapstruct.*;
import ru.skillbox.diplom.group42.social.service.dto.account.AccountDto;
import ru.skillbox.diplom.group42.social.service.dto.account.AccountOnlineDto;
import ru.skillbox.diplom.group42.social.service.entity.account.Account;
import ru.skillbox.diplom.group42.social.service.entity.auth.User;

import java.time.ZonedDateTime;

@Mapper(componentModel = "spring", imports = ZonedDateTime.class)
public interface AccountMapper {
    AccountDto convertToDto(Account account);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Account convertToAccount(AccountDto accountDto, @MappingTarget Account account);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "lastOnlineTime", source = "lastOnlineTime")
    AccountDto convertToAccountDto(AccountOnlineDto accountOnlineDto, @MappingTarget AccountDto accountDto);

    @Mapping(target = "isBlocked", constant = "false")
    @Mapping(target = "regDate", expression = "java(ZonedDateTime.now())")
    @Mapping(target = "createdOn", expression = "java(ZonedDateTime.now())")
    @Mapping(target = "updatedOn", expression = "java(ZonedDateTime.now())")
    Account userToAccount(User user);
}