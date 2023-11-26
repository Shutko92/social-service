package ru.skillbox.diplom.group42.social.service.mapper.friend;

import org.mapstruct.*;
import ru.skillbox.diplom.group42.social.service.dto.friend.FriendShortDto;
import ru.skillbox.diplom.group42.social.service.entity.account.Account;
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
}
