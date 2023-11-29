package ru.skillbox.diplom.group42.social.service.mapper.friend;

import org.mapstruct.*;
import ru.skillbox.diplom.group42.social.service.dto.friend.FriendShortDto;
import ru.skillbox.diplom.group42.social.service.entity.account.Account;
import ru.skillbox.diplom.group42.social.service.entity.account.StatusCode;
import ru.skillbox.diplom.group42.social.service.entity.friend.Friend;

import java.time.ZonedDateTime;


@Mapper(componentModel = "spring", imports = ZonedDateTime.class)
public interface FriendMapper {

    FriendShortDto convertToDto(Friend friend);

    Friend convertToFriend(FriendShortDto friendShortDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Friend convertToFriend(FriendShortDto friendShortDto, @MappingTarget Friend friend);
    @Mapping(target = "friendId", source = "idTo")
    @Mapping(target = "statusCode", source = "statusCode")
    @Mapping(target = "previousStatusCode", source = "previousStatusCode")
    @Mapping(target = "rating", source = "rating")
    FriendShortDto convertToFriendShortDto(Friend friend);

    @Mapping(target ="photo", source = "photo")
    @Mapping(target = "firstName",source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "city",source = "city")
    @Mapping(target = "country",source = "country")
    @Mapping(target = "birthDate",source = "birthDate")
    Friend convertToFriend(Account account);

    @Mapping(target = "photo", expression = "java(account.getPhoto())")
    @Mapping(target = "firstName", expression = "java(account.getFirstName())")
    @Mapping(target = "lastName", expression = "java(account.getLastName())")
    @Mapping(target = "city", expression = "java(account.getCity())")
    @Mapping(target = "country", expression = "java(account.getCountry())")
    @Mapping(target = "birthDate", expression = "java(account.getBirthDate())")
    @Mapping(target = "statusCode", expression = "java(StatusCode.REQUEST_TO.toString())")
    @Mapping(target = "idTo", expression = "java(account.getId())")
    @Mapping(target = "rating", constant = "0")
    @Mapping(target = "isOnline", expression = "java(account.getIsOnline())")
    @Mapping(target = "previousStatusCode", expression = "java(account.getStatusCode() != null ? account.getStatusCode().toString() : null)")
    @Mapping(target = "idFrom", expression = "java(userFromId)")
    Friend convertToFriendTo(Account account, Long userFromId);

    @Mapping(target = "photo", expression = "java(account.getPhoto())")
    @Mapping(target = "firstName", expression = "java(account.getFirstName())")
    @Mapping(target = "lastName", expression = "java(account.getLastName())")
    @Mapping(target = "city", expression = "java(account.getCity())")
    @Mapping(target = "country", expression = "java(account.getCountry())")
    @Mapping(target = "birthDate", expression = "java(account.getBirthDate())")
    @Mapping(target = "statusCode", expression = "java(StatusCode.REQUEST_FROM.toString())")
    @Mapping(target = "idFrom", expression = "java(accountUserTo.getId())")
    @Mapping(target = "idTo", expression = "java(account.getId())")
    @Mapping(target = "id", expression = "java(account.getId())")
    @Mapping(target = "isDeleted", expression = "java(account.getIsDeleted())")
    @Mapping(target = "rating", constant = "0")
    @Mapping(target = "isOnline", expression = "java(account.getIsOnline())")
    @Mapping(target = "previousStatusCode", expression = "java(accountUserTo.getStatusCode() != null ? accountUserTo.getStatusCode().toString() : null)")
    Friend convertToFriendFrom(Account account, Account accountUserTo);

    @Mapping(target = "previousStatusCode", source = "previousStatusCode")
    @Mapping(target = "idFrom", source = "fromId")
    @Mapping(target = "idTo", source = "idTo")
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "id", expression = "java(item.getId())")
    @Mapping(target = "statusCode" , expression = "java(statusCode.toString())")
    @Mapping(target = "isOnline", source = "isOnLine")
    Friend convertFriendToFriend(Friend item, StatusCode previousStatusCode, Long fromId, Long idTo, StatusCode statusCode, Boolean isOnLine);
}
