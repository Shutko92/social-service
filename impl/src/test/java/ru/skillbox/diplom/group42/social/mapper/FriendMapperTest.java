package ru.skillbox.diplom.group42.social.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skillbox.diplom.group42.social.service.dto.friend.FriendShortDto;
import ru.skillbox.diplom.group42.social.service.entity.account.Account;
import ru.skillbox.diplom.group42.social.service.entity.friend.Friend;
import ru.skillbox.diplom.group42.social.service.mapper.friend.FriendMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.createTestAccount;
import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.createTestFriend;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.TEST_ACCOUNT_ID;

class FriendMapperTest {
    FriendMapper mapper = Mappers.getMapper(FriendMapper.class);

    @Test
    void convertToDtoIsCorrect() {
        Friend testFriend = createTestFriend();
        FriendShortDto friendShortDto = mapper.convertToDto(testFriend);
        assertEquals(testFriend.getId(), friendShortDto.getId());
        assertEquals(testFriend.getStatusCode(), friendShortDto.getStatusCode());
        assertEquals(testFriend.getRating(), friendShortDto.getRating());
        assertEquals(testFriend.getPreviousStatusCode(), friendShortDto.getPreviousStatusCode());
        assertEquals(testFriend.getIsDeleted(), friendShortDto.getIsDeleted());
    }

    @Test
    void convertToFriendShortDtoIsCorrect() {
        Friend testFriend = createTestFriend();
        FriendShortDto friendShortDto = mapper.convertToFriendShortDto(testFriend);
        assertEquals(testFriend.getId(), friendShortDto.getId());
        assertEquals(testFriend.getIdTo(), friendShortDto.getFriendId());
        assertEquals(testFriend.getStatusCode(), friendShortDto.getStatusCode());
        assertEquals(testFriend.getRating(), friendShortDto.getRating());
        assertEquals(testFriend.getPreviousStatusCode(), friendShortDto.getPreviousStatusCode());
        assertEquals(testFriend.getIsDeleted(), friendShortDto.getIsDeleted());
    }

    @Test
    void convertToFriendIsCorrect() {
        Account testAccount = createTestAccount(TEST_ACCOUNT_ID);
        Friend friend = mapper.convertToFriend(testAccount);
        assertEquals(testAccount.getId(), friend.getId());
        assertEquals(testAccount.getPhoto(), friend.getPhoto());
        assertEquals(testAccount.getCity(), friend.getCity());
        assertEquals(testAccount.getCountry(), friend.getCountry());
        assertEquals(testAccount.getStatusCode(), friend.getStatusCode());
        assertEquals(testAccount.getBirthDate(), friend.getBirthDate());
        assertEquals(testAccount.getIsDeleted(), friend.getIsDeleted());
    }
}